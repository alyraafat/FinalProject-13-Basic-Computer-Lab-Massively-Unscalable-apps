package com.redditclone.user_service.services;

import com.redditclone.user_service.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.expirationms}")
    private Long EXPIRATION_MS;

    @Value("${jwt.refresh-token.secret}")
    private String REFRESH_TOKEN_SECRET_KEY;

    @Value("${jwt.refresh-token.expirationms}")
    private Long REFRESH_TOKEN_EXPIRATION_MS;

    private Key getSigningKey(String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(User user) {
        return prepareClaims(user, EXPIRATION_MS, SECRET_KEY);
    }

    private String prepareClaims(User user, Long expirationMs, String secretKey) {
        Map<String, Object> claims = new HashMap<>();
        String userId = user.getId().toString();
        claims.put("username", user.getUsername());
        claims.put("email", user.getEmail());
        return createToken(claims, userId, expirationMs, secretKey);
    }

    private String generateToken(Map<String, Object> claims, String userId) {
        return createToken(claims, userId, EXPIRATION_MS, SECRET_KEY);
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
        return prepareClaims(user, REFRESH_TOKEN_EXPIRATION_MS, REFRESH_TOKEN_SECRET_KEY);
    }

    public String generateRefreshToken(Map<String, Object> claims, String userId) {
        return createToken(claims, userId, REFRESH_TOKEN_EXPIRATION_MS, REFRESH_TOKEN_SECRET_KEY);
    }

    public String extractUsername(String token, String tokenType) {
        String secretKey = tokenType.equals("refresh") ? REFRESH_TOKEN_SECRET_KEY : SECRET_KEY;
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
        String secretKey = tokenType.equals("refresh") ? REFRESH_TOKEN_SECRET_KEY : SECRET_KEY;
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
