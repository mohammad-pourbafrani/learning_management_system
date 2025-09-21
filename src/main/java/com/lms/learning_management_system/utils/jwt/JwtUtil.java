package com.lms.learning_management_system.utils.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    private JwtProperties jwtProperties;

    @Autowired
    public JwtUtil(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }


    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Generate JWT with phone or email and role
     */
    public String generateToken(String phone, String email, String role, TokenType tokenType) {
        Map<String, Object> claims = new HashMap<>();

        if (phone != null) {
            claims.put("phone", phone);
        }
        if (email != null) {
            claims.put("email", email);
        }
        claims.put("role", role);

        Long expiration = null;
        switch (tokenType) {
            case ACCESS -> {
                expiration = jwtProperties.getExpiration();
            }
            case REFRESH -> {
                expiration = jwtProperties.getRefreshExpiration();
            }
        }

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(role) // subject can be role or identifier
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    public String extractPhone(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("phone", String.class);
    }

    public String extractEmail(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("email", String.class);
    }

    public String extractRole(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("role", String.class);
    }

    public boolean validateToken(String token) {
        try {
            return !isTokenExpired(token);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // Helpers
    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }


}
