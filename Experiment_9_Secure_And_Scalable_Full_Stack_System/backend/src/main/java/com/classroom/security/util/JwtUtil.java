package com.classroom.security.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Key;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * ============================================================
 *  JWT Utility
 * ============================================================
 *  Handles: Token generation, signing (HS256), and validation.
 *
 *  JWT structure:
 *    HEADER.PAYLOAD.SIGNATURE
 *    - Header:    algorithm + token type
 *    - Payload:   subject (username), roles, issued-at, expiry
 *    - Signature: HMAC-SHA256(base64(header)+"."+base64(payload), secret)
 * ============================================================
 */
@Component
public class JwtUtil {

    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-ms}")
    private long jwtExpirationMs;

    @Value("${app.jwt.refresh-expiration-ms}")
    private long refreshExpirationMs;

    // ── Key derived from the configured secret ──────────────────────────────
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    // ── Generate ACCESS token (short-lived) ─────────────────────────────────
    public String generateAccessToken(String username, List<String> roles) {
        log.debug("Generating access token for user: {}", username);
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)          // custom claim
                .claim("type", "access")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // ── Generate REFRESH token (long-lived, no roles) ───────────────────────
    public String generateRefreshToken(String username) {
        return generateRefreshToken(username, List.of());
    }

    public String generateRefreshToken(String username, List<String> roles) {
        log.debug("Generating refresh token for user: {}", username);
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles == null ? List.of() : roles)
                .claim("type", "refresh")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // ── Validate token (checks signature + expiry) ───────────────────────────
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("JWT expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.warn("JWT unsupported: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.warn("JWT malformed: {}", e.getMessage());
        } catch (Exception e) {
            log.warn("JWT invalid: {}", e.getMessage());
        }
        return false;
    }

    // ── Extract claims from token ────────────────────────────────────────────
    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {
        List<String> roles = getClaims(token).get("roles", List.class);
        return roles == null ? new ArrayList<>() : roles;
    }

    public String extractType(String token) {
        return getClaims(token).get("type", String.class);
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
