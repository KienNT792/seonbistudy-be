package com.seonbistudy.seonbistudy.service.impl;

import com.seonbistudy.seonbistudy.model.entity.Streak;
import com.seonbistudy.seonbistudy.repository.StreakRepository;
import com.seonbistudy.seonbistudy.service.IStreakService;
import com.seonbistudy.seonbistudy.service.IXpService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
import com.seonbistudy.seonbistudy.service.IAuthService;
import com.seonbistudy.seonbistudy.service.IUserService;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {

    private final AccountRepository accountRepository;
    private final IUserService userService;
    private final IStreakService streakService;
    private final IXpService xpService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final StreakRepository streakRepository;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // Validate username
        if (accountRepository.existsByUsername(request.getUsername())) {
            throw new SeonbiException(ErrorCode.AUTH_USERNAME_EXISTS);
        }

        // Validate email
        if (accountRepository.existsByEmail(request.getEmail())) {
            throw new SeonbiException(ErrorCode.AUTH_EMAIL_EXISTS);
        }

        // Validate username length
        if (request.getUsername().length() < 3) {
            throw new SeonbiException(ErrorCode.AUTH_USERNAME_TOO_SHORT);
        }

        // Validate password strength
        if (request.getPassword().length() < 6) {
            throw new SeonbiException(ErrorCode.AUTH_PASSWORD_TOO_WEAK);
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

        var jwtToken = jwtService.generateToken(savedAccount);
        Streak newStreak = Streak.builder()
                .account(savedAccount)
                .currentStreak(0)
                .maxStreak(0)
                .lastActivityDate(LocalDateTime.now())
                .build();
        streakRepository.save(newStreak);

        return AuthResponse.builder()
                .token(jwtToken)
                .id(savedAccount.getId())
                .username(savedAccount.getUsername())
                .email(savedAccount.getEmail())
                .fullName(user.getFullName())
                .role(savedAccount.getRole())
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

        streakService.updateStreak(account.getId());
        var jwtToken = jwtService.generateToken(account);

        return AuthResponse.builder()
                .token(jwtToken)
                .id(account.getId())
                .username(account.getUsername())
                .email(account.getEmail())
                .fullName(account.getUser() != null ? account.getUser().getFullName() : null)
                .role(account.getRole())
                .build();
    }
}
