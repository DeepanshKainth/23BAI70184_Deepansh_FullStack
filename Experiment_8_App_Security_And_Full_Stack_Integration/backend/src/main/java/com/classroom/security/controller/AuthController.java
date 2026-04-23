package com.classroom.security.controller;

import com.classroom.security.model.AuthResponse;
import com.classroom.security.model.LoginRequest;
import com.classroom.security.model.RefreshRequest;
import com.classroom.security.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * ============================================================
 *  CHAPTER 3.1 — Authentication Controller
 * ============================================================
 *  POST /auth/login    — authenticates user, returns JWT pair
 *  POST /auth/refresh  — issues new access token via refresh token
 *  POST /auth/logout   — client-side only (stateless)
 *  GET  /auth/me       — returns current user info
 *
 *  These endpoints are permitAll() in SecurityConfig
 *  (except /auth/me which needs a valid token to identify the user)
 * ============================================================
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authManager, JwtUtil jwtUtil) {
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
    }

    // ── LOGIN ────────────────────────────────────────────────────────────────
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        log.debug("Login attempt for user: {}", request.getUsername());
        try {
            // Spring Security verifies username + password
            Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getUsername(), request.getPassword()
                )
            );

            // Extract roles from the authenticated principal
            List<String> roles = auth.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            // Generate both tokens
            String accessToken  = jwtUtil.generateAccessToken(auth.getName(), roles);
            String refreshToken = jwtUtil.generateRefreshToken(auth.getName(), roles);

            log.info("✓ Login successful: {} → roles: {}", auth.getName(), roles);

            return ResponseEntity.ok(new AuthResponse(
                accessToken, refreshToken, auth.getName(), roles
            ));

        } catch (Exception e) {
            log.warn("✗ Login failed for {}: {}", request.getUsername(), e.getMessage());
            return ResponseEntity.status(401)
                    .body(Map.of("error", "Invalid username or password"));
        }
    }

    // ── REFRESH TOKEN ────────────────────────────────────────────────────────
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshRequest request) {
        String refreshToken = request.getRefreshToken();
        log.debug("Refresh token request received");

        if (!jwtUtil.validateToken(refreshToken)) {
            log.warn("Invalid refresh token");
            return ResponseEntity.status(401)
                    .body(Map.of("error", "Refresh token is invalid or expired. Please login again."));
        }

        // Make sure this is actually a refresh token (not an access token being misused)
        if (!"refresh".equals(jwtUtil.extractType(refreshToken))) {
            return ResponseEntity.status(401)
                    .body(Map.of("error", "Invalid token type"));
        }

        String username = jwtUtil.extractUsername(refreshToken);
        List<String> roles = jwtUtil.extractRoles(refreshToken);
        if (roles.isEmpty()) {
            roles = List.of("ROLE_USER");
        }

        String newAccessToken = jwtUtil.generateAccessToken(username, roles);
        log.info("✓ Access token refreshed for: {}", username);

        return ResponseEntity.ok(Map.of(
            "accessToken", newAccessToken,
            "tokenType", "Bearer"
        ));
    }

    // ── LOGOUT (stateless — just confirmation) ───────────────────────────────
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // With stateless JWT, logout is client-side (delete token from storage)
        // In production: maintain a token blocklist in Redis
        return ResponseEntity.ok(Map.of(
            "message", "Logged out. Please delete your token on the client side."
        ));
    }

    @GetMapping(value = "/oauth2/bridge", produces = MediaType.TEXT_HTML_VALUE)
    public String oauthBridge(@RequestParam String accessToken,
                              @RequestParam String refreshToken,
                              @RequestParam String username,
                              @RequestParam String roles) {
        String safeUsername = UriUtils.encode(username, StandardCharsets.UTF_8);
        String safeRoles = UriUtils.encode(roles, StandardCharsets.UTF_8);

        return """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8" />
                    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
                    <title>LivePoll OAuth Bridge</title>
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            background: #0f1117;
                            color: #e2e8f0;
                            display: grid;
                            place-items: center;
                            min-height: 100vh;
                            margin: 0;
                        }
                        .card {
                            background: #1a1d27;
                            border: 1px solid #2e3347;
                            border-radius: 14px;
                            padding: 24px;
                            width: min(520px, 92vw);
                        }
                        h1 { margin: 0 0 8px; font-size: 20px; }
                        p { color: #94a3b8; line-height: 1.5; }
                    </style>
                </head>
                <body>
                    <div class="card">
                        <h1>Google login completed</h1>
                        <p>You can close this window. The token has been sent back to the LivePoll frontend.</p>
                    </div>
                    <script>
                        (function () {
                            const payload = {
                                accessToken: new URLSearchParams(window.location.search).get('accessToken'),
                                refreshToken: new URLSearchParams(window.location.search).get('refreshToken'),
                                username: decodeURIComponent('%s'),
                                roles: decodeURIComponent('%s').split(',').filter(Boolean)
                            };
                            if (window.opener) {
                                window.opener.postMessage({ type: 'LIVEPOLL_OAUTH_SUCCESS', payload }, '*');
                            }
                            setTimeout(() => window.close(), 150);
                        })();
                    </script>
                </body>
                </html>
                """.formatted(safeUsername, safeRoles);
    }
}
