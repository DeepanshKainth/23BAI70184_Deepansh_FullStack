package com.classroom.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ============================================================
 *  CLASSROOM DEMO — Chapter 3.1 & 3.2
 *  Application Security + Full Stack Integration
 * ============================================================
 *
 *  Run this class, then open frontend/index.html in a browser.
 *
 *  Demo users (in-memory, no database needed):
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
