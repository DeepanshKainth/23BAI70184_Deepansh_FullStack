package com.classroom.security.config;

import com.classroom.security.filter.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * ============================================================
 *  CHAPTER 3.1 — Security Filter Chain Configuration
 *  CHAPTER 3.2 — CORS Configuration
 * ============================================================
 *
 *  Key concepts demonstrated here:
 *
 *  1. SecurityFilterChain  — controls which URLs are open/protected
 *  2. STATELESS sessions   — no HttpSession; JWT carries all state
 *  3. JwtAuthFilter        — registered BEFORE Spring's default filter
 *  4. @EnableMethodSecurity — enables @PreAuthorize on controllers
 *  5. CORS                 — allows React (port 3000) → Spring (8080)
 *  6. In-memory users      — no DB needed for the demo
 * ============================================================
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity           // ← enables @PreAuthorize / @PostAuthorize
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final GoogleOAuth2AuthorizationRequestRepository authorizationRequestRepository;
    private final GoogleOAuth2SuccessHandler googleOAuth2SuccessHandler;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter,
                          GoogleOAuth2AuthorizationRequestRepository authorizationRequestRepository,
                          GoogleOAuth2SuccessHandler googleOAuth2SuccessHandler) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authorizationRequestRepository = authorizationRequestRepository;
        this.googleOAuth2SuccessHandler = googleOAuth2SuccessHandler;
    }

    // ── 1. Security Filter Chain ─────────────────────────────────────────────
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // CSRF disabled — we're stateless (JWT), not cookie-based
            .csrf(AbstractHttpConfigurer::disable)

            // H2 console support for classroom demos
            .headers(headers -> headers.frameOptions(frame -> frame.disable()))

            // Chapter 3.2: CORS — allow React dev server
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))

            // Session management: STATELESS — no server-side session
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // Google OAuth2 login for the full-stack experiment
            .oauth2Login(oauth2 -> oauth2
                .authorizationEndpoint(endpoint -> endpoint.authorizationRequestRepository(authorizationRequestRepository))
                .successHandler(googleOAuth2SuccessHandler))

            // URL authorization rules
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll()        // login & refresh — open
                .requestMatchers("/oauth2/**", "/login/oauth2/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/api/polls/**").permitAll()
                .anyRequest().authenticated()                   // everything else — needs JWT
            )

            // Register our JWT filter BEFORE Spring's default auth filter
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // ── 2. CORS Configuration (Chapter 3.2) ──────────────────────────────────
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // Allow requests from React dev server
        config.setAllowedOrigins(List.of(
            "http://localhost:3000",    // React (npm start)
            "http://localhost:5173",    // Vite dev server
            "http://127.0.0.1:5500",   // VS Code Live Server (for our demo HTML)
            "null"                     // file:// origin — lets us open index.html directly
        ));

        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);    // cache preflight for 1 hour

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    // ── 3. In-Memory Users (no DB needed for classroom demo) ─────────────────
    @Bean
    public UserDetailsService userDetailsService() {
        // Three users used by the LivePoll experiment
        var alice = User.builder()
            .username("alice")
            .password(passwordEncoder().encode("password123"))
            .roles("ADMIN")             // ROLE_ADMIN
            .build();

        var bob = User.builder()
            .username("bob")
            .password(passwordEncoder().encode("password123"))
            .roles("USER")              // ROLE_USER
            .build();

        var carol = User.builder()
            .username("carol")
            .password(passwordEncoder().encode("password123"))
            .roles("USER")              // ROLE_USER
            .build();

        return new InMemoryUserDetailsManager(alice, bob, carol);
    }

    // ── 4. Password Encoder (BCrypt) ─────────────────────────────────────────
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ── 5. AuthenticationManager (needed by AuthController) ──────────────────
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
