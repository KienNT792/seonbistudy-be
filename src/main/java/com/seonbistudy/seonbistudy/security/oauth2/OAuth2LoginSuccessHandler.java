package com.seonbistudy.seonbistudy.security.oauth2;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.seonbistudy.seonbistudy.config.JwtService;
import com.seonbistudy.seonbistudy.model.entity.User;
import com.seonbistudy.seonbistudy.model.enums.AuthProvider;
import com.seonbistudy.seonbistudy.model.enums.Role;
import com.seonbistudy.seonbistudy.service.IUserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final IUserService userService;
    private final JwtService jwtService;

    @Value("${app.oauth2.redirect-uri}")
    private String redirectUri;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String providerId = oAuth2User.getAttribute("sub");

        // Tìm kiếm user theo email
        User user = userService.findByEmail(email);

        if (user == null) {
            // Nếu email chưa tồn tại, tạo account mới với role STUDENT
            user = User.builder()
                    .email(email)
                    .username(email.split("@")[0] + "_" + System.currentTimeMillis())
                    .fullName(name)
                    .provider(AuthProvider.GOOGLE)
                    .providerId(providerId)
                    .role(Role.STUDENT)
                    .enabled(true)
                    .build();
            user = userService.createUser(user);
        } else {
            // Nếu email đã tồn tại, kiểm tra xem account có enabled không
            if (!userService.isAccountEnabled(user)) {
                // Nếu account bị disabled, chuyển hướng với lỗi
                String targetUrl = UriComponentsBuilder.fromUriString(redirectUri)
                        .queryParam("error", "Account is disabled")
                        .build().toUriString();
                getRedirectStrategy().sendRedirect(request, response, targetUrl);
                return;
            }
        }

        // Tạo JWT token trực tiếp
        String token = jwtService.generateToken(user);

        String targetUrl = UriComponentsBuilder.fromUriString(redirectUri)
                .queryParam("token", token)
                .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
