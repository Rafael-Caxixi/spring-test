package com.rafaelcaxixi.spring_junit.security.services.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Service
public class TokenServiceImpl implements com.rafaelcaxixi.spring_junit.security.services.TokenService {

    private final String secret;
    private final long prazoExpiracao;

    public TokenServiceImpl(@Value("${jwt.secret}") String secret,
                            @Value("${jwt.prazoExpiracao}") long prazoExpiracao) {
        this.secret = secret;
        this.prazoExpiracao = prazoExpiracao;
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String generateToken(String login) {
        try {
            return Jwts.builder()
                    .setIssuer("spring-junit")
                    .setSubject(login)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + prazoExpiracao))
                    .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                    .compact();

        } catch (Exception exception) {
            throw new RuntimeException("JWT generation failed", exception);
        }
    }

    @Override
    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    @Override
    public String extractUsername(String token) {
        Claims claims = extractClaims(token);
        if (claims == null) throw new JwtException("Invalid or Expired Token");
        return claims.getSubject();
    }

    @Override
    public boolean isTokenExpired(String token) {
        Claims claims = extractClaims(token);
        if (claims == null || claims.getExpiration() == null) {
            throw new JwtException("Invalid or Expired Token");
        }
        return claims.getExpiration().before(new Date());
    }

    @Override
    public boolean validateToken(String token, String username) {
        return (username.equals(extractUsername(token)) && !isTokenExpired(token));
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }
}