package com.redditclone.user_service.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redditclone.user_service.dtos.*;
import com.redditclone.user_service.exceptions.RedditAppException;
import com.redditclone.user_service.models.RefreshToken;
import com.redditclone.user_service.models.User;
import com.redditclone.user_service.models.VerificationToken;
import com.redditclone.user_service.rabbitmq.NotificationProducer;
import com.redditclone.user_service.repositories.RefreshTokenRepository;
import com.redditclone.user_service.repositories.UserRepository;
import com.redditclone.user_service.repositories.VerificationTokenRepository;
import com.redditclone.user_service.utils.RegistrationMessage;
import com.redditclone.user_service.validation.UserValidation;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Value("${service_url}")
    private String serviceUrl;

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.expirationms}")
    private Long EXPIRATION_MS; // 1 hour

    @Value("${jwt.refresh-token.secret}")
    private String REFRESH_TOKEN_SECRET_KEY;

    @Value("${jwt.refresh-token.expirationms}")
    private Long REFRESH_TOKEN_EXPIRATION_MS;

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final UserValidation userValidation;
    private final RefreshTokenRepository refreshTokenRepository;
    private JwtService jwtService;
    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    private final NotificationProducer notificationProducer;

    @PostConstruct
    private void initJwtService() {
        this.jwtService = JwtService.getInstance(
                SECRET_KEY,
                EXPIRATION_MS,
                REFRESH_TOKEN_SECRET_KEY,
                REFRESH_TOKEN_EXPIRATION_MS
        );
    }

    @Transactional
    public void signup(RegisterObject registerObject) {
        userValidation.validateUserDTO(registerObject);
        boolean userExists = userValidation.validateUserNonExistent(registerObject.getUsername(), registerObject.getEmail());
        String encodedPassword = passwordEncoder.encode(registerObject.getPassword());
//        User user = new User(registerObject.getUsername(), encodedPassword, registerObject.getEmail(), Instant.now(), false);
        User user;
        String token;

        if (!userExists) {
            user = User.builder()
                    .username(registerObject.getUsername())
                    .password(encodedPassword)
                    .email(registerObject.getEmail())
                    .createdAt(Instant.now())
                    .activated(false)
                    .bio(registerObject.getBio())
                    .fullName(registerObject.getFullName())
                    .build();
            userRepository.save(user);
            token = generateVerificationToken(user);
            log.info("New user registered: {}", user.getUsername());
        } else {
            user = userRepository.findByUsername(registerObject.getUsername())
                    .orElseThrow(() -> new RedditAppException("User Not Found with name: " + registerObject.getUsername()));
            if (registerObject.getBio() != null) {
                user.setBio(registerObject.getBio());
                userRepository.save(user);
            }
            token = updateVerificationToken(user);
            log.info("Existing user re-requested activation: {}", user.getUsername());
        }

        mailService.sendMail(new NotificationEmail(
                "Reddit App Account Activation",
                user.getEmail(),
                new RegistrationMessage(serviceUrl, token).toString()
        ));
        log.info("Verification email sent to {}", user.getEmail());
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken(token, user);
        verificationTokenRepository.save(verificationToken);
        return token;
    }

    private String updateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByUser(user);
        if (verificationToken.isEmpty()) {
            throw new RedditAppException("Cannot access activation right now!");
        }
        VerificationToken updatedVerificationToken = new VerificationToken(token, verificationToken.get().getUser());
        updatedVerificationToken.setId(verificationToken.get().getId());
        verificationTokenRepository.save(updatedVerificationToken);
        return token;
    }

    @Transactional
    public void activateAccount(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        verificationToken.orElseThrow(() -> new RedditAppException("Invalid verification token"));
        if (verificationToken.get().getExpiryDate().isBefore(Instant.now())) {
            throw new RedditAppException("Verification token expired");
        }
        verificationTokenRepository.deleteByToken(token);
        fetchUserAndActivate(verificationToken.get().getUser());
        log.info("Account activated for user: {}", verificationToken.get().getUser().getUsername());
    }

    private void fetchUserAndActivate(User user) {
        String userName = user.getUsername();
        User updatedUser = userRepository.findByUsername(userName)
                .orElseThrow(() -> new RedditAppException("User Not Found with name: " + userName));
        updatedUser.setActivated(true);
        userRepository.save(updatedUser);
        notificationProducer.notifyUser(updatedUser);
    }

    public JwtAuthenticationResponse login(LoginObject loginObject) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginObject.getUsername(), loginObject.getPassword()));
        User user = userRepository.findByUsername(loginObject.getUsername())
                .orElseThrow(() -> new RedditAppException("User Not Found with name: " + loginObject.getUsername()));
        user.setLastLogin(Instant.now());
        userRepository.save(user);

        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, refreshToken);

        log.info("User logged in: {}", user.getUsername());

        return JwtAuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = RefreshToken.builder()
                .user(user)
                .refreshToken(jwtToken)
                .expired(false)
                .revoked(false)
                .build();
        refreshTokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = refreshTokenRepository.findAllValidRefreshTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(refreshToken -> {
            refreshToken.setExpired(true);
            refreshToken.setRevoked(true);
        });
        refreshTokenRepository.saveAll(validUserTokens);
    }

    private void expireToken(RefreshToken refreshToken) {
        if (refreshToken == null) {
            return;
        }
        refreshToken.setExpired(true);
        refreshTokenRepository.save(refreshToken);
    }

    public void refreshToken(String refreshToken, HttpServletResponse response) throws IOException {
        final String username;
        username = jwtService.extractUsername(refreshToken, "refresh");
        if (username != null) {
            var user = this.userRepository.findByUsername(username).orElseThrow(() -> new RedditAppException("User Not Found with name: " + username));
            RefreshToken storedToken = refreshTokenRepository.findByRefreshToken(refreshToken).orElseThrow(() -> new RedditAppException("Refresh token not found"));

            if (storedToken.isRevoked()) {
                throw new RedditAppException("Refresh token has been revoked – please log in again");
            }

            if (jwtService.isTokenValid(refreshToken, user, "refresh")) {
                var accessToken = jwtService.generateToken(user);
                var authResponse = JwtAuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
                log.info("Refresh token successful for user: {}", username);
            } else if (jwtService.isTokenExpired(refreshToken, "refresh")) {
                expireToken(storedToken);
                throw new RedditAppException("Refresh token expired – please log in again");
            } else {
                throw new RedditAppException("Invalid refresh token");
            }
        }
    }

    public void logout(String rawRefreshToken) {
        refreshTokenRepository.findByRefreshToken(rawRefreshToken)
                .ifPresent(token -> {
                    token.setExpired(true);
                    token.setRevoked(true);
                    refreshTokenRepository.save(token);
                });
        log.info("User logged out (token revoked)");
    }

}
