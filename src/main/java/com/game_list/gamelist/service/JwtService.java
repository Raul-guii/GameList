package com.game_list.gamelist.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.security.Key;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtService {

    @Value("${JWT_SECRET:default_secret_for_dev_change}")
    private String secret;

    @Value("${JWT_EXP_MS:36000000}")
    private long expirationMs;

    public String generateToken(String username) {
        Key key = Keys.hmacShaKeyFor(secret.getBytes());
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String validateToken(String token) {
        try {
            Key key = Keys.hmacShaKeyFor(secret.getBytes());
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
        } catch (JwtException ex) {
            return null;
        }
    }
}


