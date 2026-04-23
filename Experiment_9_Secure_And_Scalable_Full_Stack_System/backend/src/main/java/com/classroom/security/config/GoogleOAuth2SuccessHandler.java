package com.classroom.security.config;

import com.classroom.security.util.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class GoogleOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;

    @Value("${app.oauth2.admin-emails:}")
    private String adminEmails;

    public GoogleOAuth2SuccessHandler(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = Objects.toString(oAuth2User.getAttribute("email"), authentication.getName());
        String username = Objects.toString(oAuth2User.getAttribute("name"), email);

        List<String> roles = new ArrayList<>();
        roles.add("ROLE_USER");
        if (isAdminEmail(email)) {
            roles.add("ROLE_ADMIN");
        }

        String accessToken = jwtUtil.generateAccessToken(email, roles);
        String refreshToken = jwtUtil.generateRefreshToken(email, roles);

        String redirectUrl = UriComponentsBuilder.fromPath("/auth/oauth2/bridge")
            .queryParam("accessToken", accessToken)
            .queryParam("refreshToken", refreshToken)
            .queryParam("username", username)
            .queryParam("roles", String.join(",", roles))
            .build()
            .encode()
            .toUriString();

        response.sendRedirect(redirectUrl);
    }

    private boolean isAdminEmail(String email) {
        if (adminEmails == null || adminEmails.isBlank()) {
            return false;
        }

        return List.of(adminEmails.split(",")).stream()
            .map(String::trim)
            .filter(value -> !value.isBlank())
            .anyMatch(value -> value.equalsIgnoreCase(email));
    }
}