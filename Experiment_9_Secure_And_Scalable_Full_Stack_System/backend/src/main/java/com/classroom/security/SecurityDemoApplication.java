package com.classroom.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ============================================================
 *  Experiment 9: Secure and Scalable Full Stack System
 *  Spring Security + OAuth2 + RBAC + JPA Optimization
 * ============================================================
 *
 *  Run this class, then open frontend/index.html in a browser.
 *
 *  Default users (in-memory, no external identity store required):
 *    alice / password123  →  ROLE_ADMIN
 *    bob   / password123  →  ROLE_USER
 *    carol / password123  →  ROLE_USER
 * ============================================================
 */
@SpringBootApplication
public class SecurityDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(SecurityDemoApplication.class, args);
        System.out.println("""
                
                ╔══════════════════════════════════════════╗
                ║   Security Demo is running!              ║
                ║   API Base: http://localhost:8080        ║
                ║   Open frontend/index.html in browser    ║
                ╚══════════════════════════════════════════╝
                """);
    }
}
