package com.seonbistudy.seonbistudy.service.impl;

import com.seonbistudy.seonbistudy.model.enums.XpActivityType;
import com.seonbistudy.seonbistudy.service.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.seonbistudy.seonbistudy.security.jwt.JwtService;
import com.seonbistudy.seonbistudy.dto.auth.AuthResponse;
import com.seonbistudy.seonbistudy.dto.auth.LoginRequest;
import com.seonbistudy.seonbistudy.dto.auth.RegisterRequest;
import com.seonbistudy.seonbistudy.exception.ErrorCode;
import com.seonbistudy.seonbistudy.exception.SeonbiException;
import com.seonbistudy.seonbistudy.model.entity.Account;
import com.seonbistudy.seonbistudy.model.entity.User;
import com.seonbistudy.seonbistudy.model.enums.AuthProvider;
import com.seonbistudy.seonbistudy.repository.AccountRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {

    private final AccountRepository accountRepository;
    private final IUserService userService;
    private final IStreakService streakService;
    private final ILevelThresholdService levelThresholdService;
    private final IXpService xpService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // Validate request
        if (accountRepository.existsByUsername(request.getUsername())) {
            throw new SeonbiException(ErrorCode.AUTH_USERNAME_EXISTS);
        }

        if (accountRepository.existsByEmail(request.getEmail())) {
            throw new SeonbiException(ErrorCode.AUTH_EMAIL_EXISTS);
        }

        if (!request.getPassword().equals(request.getRePassword())) {
            throw new SeonbiException(ErrorCode.AUTH_PASSWORD_MISMATCH);
        }

        // Create account
        var account = Account.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .hashedPassword(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .provider(AuthProvider.LOCAL)
                .enabled(true)
                .build();

        // Create user profile
        var user = User.builder()
                .fullName(request.getFullName())
                .build();

        // Save account and user
        Account savedAccount = userService.createAccountWithUser(account, user);

        return AuthResponse.builder()
                .accessToken(jwtService.generateAccessToken(savedAccount))
                .refreshToken(jwtService.generateRefreshToken(savedAccount))
                .user(buildUser(savedAccount))
                .build();
    }

    @Override
    @Transactional
    public AuthResponse login(LoginRequest request) {
        var account = accountRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new SeonbiException(ErrorCode.AUTH_INVALID_CREDENTIALS));

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            account.getUsername(),
                            request.getPassword()));
        } catch (Exception e) {
            throw new SeonbiException(ErrorCode.AUTH_INVALID_CREDENTIALS);
        }

        if (!account.isEnabled()) {
            throw new SeonbiException(ErrorCode.AUTH_ACCOUNT_DISABLED);
        }

        var result = streakService.updateStreak(account);
        if (result.isFirstLoginToday()) {
            xpService.grantXp(account.getId(), XpActivityType.DAILY_LOGIN);
        }

        var user = buildUser(account);
        var currentLevel = levelThresholdService.getLevelByXp(user.getCurrentXp());
        user.setStreakMsg(result.getMessage());
        user.setCurrentLevel(currentLevel.getLevel());

        return AuthResponse.builder()
                .accessToken(jwtService.generateAccessToken(account))
                .refreshToken(jwtService.generateRefreshToken(account))
                .user(user)
                .build();
    }

    @Override
    public AuthResponse.UserResponse getMe(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            throw new SeonbiException(ErrorCode.AUTH_INVALID_CREDENTIALS);
        }
        String username = auth.getName();
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new SeonbiException(ErrorCode.AUTH_INVALID_CREDENTIALS));
        return buildUser(account);
    }

    private AuthResponse.UserResponse buildUser(Account account) {
        var progress = xpService.getProgress(account);
        var streak = streakService.getStreak(account);

        AuthResponse.UserResponse user = new AuthResponse.UserResponse();
        user.setId(account.getId());
        user.setUsername(account.getUsername());
        user.setFullName(account.getUser() != null ? account.getUser().getFullName() : null);
        user.setEmail(account.getEmail());
        user.setRole(account.getRole());
        user.setCurrentXp(progress.getTotalXp());
        user.setCurrentStreak(streak.getCurrentStreak());

        return user;
    }
}