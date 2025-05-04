package com.redditclone.user_service.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redditclone.user_service.dtos.*;
import com.redditclone.user_service.exceptions.RedditAppException;
import com.redditclone.user_service.models.RefreshToken;
import com.redditclone.user_service.models.User;
import com.redditclone.user_service.models.VerificationToken;
import com.redditclone.user_service.repositories.RefreshTokenRepository;
import com.redditclone.user_service.repositories.UserRepository;
import com.redditclone.user_service.repositories.VerificationTokenRepository;
import com.redditclone.user_service.utils.RegistrationMessage;
import com.redditclone.user_service.validation.UserValidation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
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

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final UserValidation userValidation;
    private final RefreshTokenRepository refreshTokenRepository;


    @Transactional
    public void signup(RegisterObject registerObject) {
        userValidation.validateUserDTO(registerObject);
        boolean userExists = userValidation.validateUserNonExistent(registerObject.getUsername(), registerObject.getEmail());
        String encodedPassword = passwordEncoder.encode(registerObject.getPassword());
        User user = new User(registerObject.getUsername(), encodedPassword, registerObject.getEmail(), Instant.now(), false);
        if (!userExists) {
            userRepository.save(user);
        }
        String token = generateVerificationToken(user);
        mailService.sendMail(new NotificationEmail(
                "Reddit App Account Activation",
                user.getEmail(),
                new RegistrationMessage(serviceUrl, token).toString()
        ));
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken(token, user);
        verificationTokenRepository.save(verificationToken);
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
    }

    //    Fetch user by username and activate the user
    //    and save the user to the database
    private void fetchUserAndActivate(User user) {
        String userName = user.getUsername();
        User updatedUser = userRepository.findByUsername(userName).orElseThrow(() -> new RedditAppException("User Not Found with name: " + userName));
        updatedUser.setActivated(true);
        userRepository.save(updatedUser);
    }

    //    Authenticate user using authentication manager and DAOProvider
    //    and generate JWT token for the user
    public JwtAuthenticationResponse login(LoginObject loginObject) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginObject.getUsername(), loginObject.getPassword()));
        User user = userRepository.findByUsername(loginObject.getUsername()).orElseThrow(() -> new RedditAppException("User Not Found with name: " + loginObject.getUsername()));
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, refreshToken);
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
        if(refreshToken == null) {
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
    }
}
