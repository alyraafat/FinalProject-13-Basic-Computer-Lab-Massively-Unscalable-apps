package com.redditclone.user_service.services;

import com.redditclone.user_service.models.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.expirationms}")
    private Long EXPIRATION_MS;


    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        String userId = user.getId().toString();
        claims.put("username", user.getUsername());
        claims.put("email", user.getEmail());
        return generateToken(claims, userId);
    }

    private String generateToken(Map<String, Object> claims, String userId) {
        return Jwts.builder()
                .claims(claims)
                .subject(userId)
                .issuer("user-service")
                .header().empty().add("typ", "JWT")
                .and()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() * EXPIRATION_MS)) // 5 minutes expiration time
                .signWith(getSigningKey())
                .compact();
    }
}
