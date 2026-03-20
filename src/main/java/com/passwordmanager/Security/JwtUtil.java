package com.passwordmanager.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class JwtUtil {

    // 🔥 Strong secret key (minimum 32 characters for HS256)
    private static final String SECRET =
            "mySuperSecretKeyForJwtGeneration123456";

    private static final Key key =
            Keys.hmacShaKeyFor(SECRET.getBytes());

    // Token validity: 1 hour
    private static final long EXPIRATION_TIME = 1000 * 60 * 60;

    // ✅ Generate JWT with username + role
    public static String generateToken(String username, String role) {

        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // ✅ Extract username
    public static String extractUsername(String token) {

        return extractAllClaims(token).getSubject();
    }

    // ✅ Extract role
    public static String extractRole(String token) {

        return extractAllClaims(token).get("role", String.class);
    }

    // ✅ Validate token
    public static boolean validateToken(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // 🔥 Centralized claim extraction (cleaner)
    private static Claims extractAllClaims(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}