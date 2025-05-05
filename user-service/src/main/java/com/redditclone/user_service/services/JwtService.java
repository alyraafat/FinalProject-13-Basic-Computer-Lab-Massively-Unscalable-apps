package com.redditclone.user_service.services;

import com.redditclone.user_service.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class JwtService {
    private final String secretKey;

    private final Long expirationMs;

    private final String refreshTokenSecretKey;

    private final Long refreshTokenExpirationMs;

    private static JwtService jwtService;

    private JwtService(String secretKey, Long expirationMs, String refreshTokenSecretKey, Long refreshTokenExpirationMs) {
        this.secretKey = secretKey;
        this.expirationMs = expirationMs;
        this.refreshTokenSecretKey = refreshTokenSecretKey;
        this.refreshTokenExpirationMs = refreshTokenExpirationMs;
    }

    public static JwtService getInstance(String secretKey, Long expirationMs, String refreshTokenSecretKey, Long refreshTokenExpirationMs) {
        if (jwtService == null) {
            jwtService = new JwtService(secretKey, expirationMs, refreshTokenSecretKey, refreshTokenExpirationMs);
        }
        return jwtService;
    }

    private Key getSigningKey(String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(User user) {
        return prepareClaims(user, expirationMs, secretKey);
    }

    private String prepareClaims(User user, Long expirationMs, String secretKey) {
        Map<String, Object> claims = new HashMap<>();
        String userId = user.getId().toString();
        claims.put("username", user.getUsername());
        claims.put("email", user.getEmail());
        return createToken(claims, userId, expirationMs, secretKey);
    }

    private String createToken(Map<String, Object> claims, String userId, Long expirationMs, String secretKey) {
        return Jwts.builder()
                .claims(claims)
                .subject(userId)
                .issuer("user-service")
                .header().empty().add("typ", "JWT")
                .and()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(getSigningKey(secretKey))
                .compact();
    }

    public String generateRefreshToken(User user) {
        return prepareClaims(user, refreshTokenExpirationMs, refreshTokenSecretKey);
    }

    public String extractUsername(String token, String tokenType) {
        String secretKey = tokenType.equals("refresh") ? refreshTokenSecretKey : this.secretKey;
        return extractClaim(
                token,
                claims -> claims.get("username", String.class),
                secretKey
        );
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver, String secretKey) {
        final Claims claims = extractAllClaims(token, secretKey);
        return claimsResolver.apply(claims);
    }

    public boolean isTokenValid(String token, UserDetails userDetails, String tokenType) {
        final String username = extractUsername(token, tokenType);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token, tokenType);
    }

    public boolean isTokenExpired(String token, String tokenType) {
        String secretKey = tokenType.equals("refresh") ? refreshTokenSecretKey : this.secretKey;
        return extractExpiration(token, secretKey).before(new Date());
    }

    private Date extractExpiration(String token, String secretKey) {
        return extractClaim(token, Claims::getExpiration, secretKey);
    }

    private Claims extractAllClaims(String token, String secretKey) {
        return Jwts.parser()
                .verifyWith((SecretKey) getSigningKey(secretKey))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}
