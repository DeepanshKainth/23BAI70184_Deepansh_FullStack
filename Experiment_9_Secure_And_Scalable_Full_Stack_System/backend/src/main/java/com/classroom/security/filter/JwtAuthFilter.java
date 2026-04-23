package com.classroom.security.filter;

import com.classroom.security.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * ============================================================
 *  CHAPTER 3.1 — JWT Authentication Filter
 * ============================================================
 *  Runs ONCE per request (OncePerRequestFilter).
 *  Sits in the Security Filter Chain BEFORE the default
 *  UsernamePasswordAuthenticationFilter.
 *
 *  Steps:
 *   1. Read "Authorization: Bearer <token>" header
 *   2. Validate the JWT
 *   3. Extract username + roles
 *   4. Store authentication in SecurityContextHolder
 *      → Spring Security then enforces @PreAuthorize rules
 * ============================================================
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthFilter.class);

    private final JwtUtil jwtUtil;

    public JwtAuthFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        log.debug("→ JwtAuthFilter processing: {} {}", request.getMethod(), path);

        // Step 1: Extract the Authorization header
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.debug("  No Bearer token found — continuing as anonymous");
            filterChain.doFilter(request, response);
            return;
        }

        // Step 2: Strip "Bearer " prefix to get the raw token
        String token = authHeader.substring(7);

        // Step 3: Validate
        if (!jwtUtil.validateToken(token)) {
            log.warn("  Invalid or expired JWT — rejecting");
            filterChain.doFilter(request, response);
            return;
        }

        // Step 4: Extract claims and build Spring Security Authentication
        String username = jwtUtil.extractUsername(token);
        List<String> roles = jwtUtil.extractRoles(token);

        List<SimpleGrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(username, null, authorities);

        // Step 5: Put authentication into the SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.debug("  ✓ Authenticated: {} with roles {}", username, roles);

        filterChain.doFilter(request, response);
    }
}
