package com.seonbistudy.seonbistudy.security.oauth2;

import java.io.IOException;

import com.seonbistudy.seonbistudy.model.enums.AccountStatus;
import com.seonbistudy.seonbistudy.model.enums.XpActivityType;
import com.seonbistudy.seonbistudy.service.IStreakService;
import com.seonbistudy.seonbistudy.service.IXpService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.seonbistudy.seonbistudy.security.jwt.JwtService;
import com.seonbistudy.seonbistudy.model.entity.Account;
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
    private final IStreakService streakService;
    private final IXpService xpService;
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

        // Tìm kiếm account theo email
        Account account = userService.findAccountByEmail(email);

        if (account == null) {
            // Nếu email chưa tồn tại, tạo account mới với role STUDENT
            account = Account.builder()
                    .email(email)
                    .username(email.split("@")[0] + "_" + System.currentTimeMillis())
                    .provider(AuthProvider.GOOGLE)
                    .providerId(providerId)
                    .role(Role.STUDENT)
                    .accountStatus(AccountStatus.ACTIVE)
                    .build();

            // Tạo user profile
            User user = User.builder()
                    .fullName(name)
                    .build();

            // Save account and user
            account = userService.createAccountWithUser(account, user);

            // Cộng XP & khởi tạo streak cho người mới
            xpService.grantXp(account.getId(), XpActivityType.ACCOUNT_REGISTER);
            streakService.initStreak(account);
        } else {
            if (!userService.isAccountEnabled(account)) {
                String targetUrl = UriComponentsBuilder.fromUriString(redirectUri)
                        .queryParam("error", "Account is disabled")
                        .build().toUriString();
                getRedirectStrategy().sendRedirect(request, response, targetUrl);
                return;
            }
            // Cập nhật streak & XP mỗi lần login
            streakService.updateStreak(account);
            xpService.grantXp(account.getId(), XpActivityType.DAILY_LOGIN);
        }

        // Tạo JWT token trực tiếp
        String targetUrl = UriComponentsBuilder.fromUriString(redirectUri)
                .queryParam("accessToken", jwtService.generateAccessToken(account))
                .queryParam("refreshToken", jwtService.generateRefreshToken(account))
                .build()
                .toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}