package com.classroom.security;

import com.classroom.security.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Basic sanity tests — run with Ctrl+Shift+F10 in IntelliJ
 * or right-click → Run 'SecurityDemoTests'
 */
@SpringBootTest
class SecurityDemoTests {

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    void contextLoads() {
        // If this passes, Spring context started correctly
    }

    @Test
    void jwtGenerateAndValidate() {
        String token = jwtUtil.generateAccessToken("alice", List.of("ROLE_ADMIN"));

        assertTrue(jwtUtil.validateToken(token));
        assertEquals("alice", jwtUtil.extractUsername(token));
        assertTrue(jwtUtil.extractRoles(token).contains("ROLE_ADMIN"));
    }

    @Test
    void tamperedTokenIsRejected() {
        String token = jwtUtil.generateAccessToken("alice", List.of("ROLE_ADMIN"));
        String tampered = token.substring(0, token.length() - 5) + "XXXXX";
        assertFalse(jwtUtil.validateToken(tampered));
    }

    @Test
    void refreshTokenHasCorrectType() {
        String refresh = jwtUtil.generateRefreshToken("bob");
        assertTrue(jwtUtil.validateToken(refresh));
        assertEquals("refresh", jwtUtil.extractType(refresh));
    }
}
