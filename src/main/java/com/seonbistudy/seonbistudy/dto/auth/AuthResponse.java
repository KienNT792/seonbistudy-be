package com.seonbistudy.seonbistudy.dto.auth;

import com.seonbistudy.seonbistudy.model.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private UserResponse user;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserResponse {
        private Long id;
        private String username;
        private String fullName;
        private String email;
        private Role role;
        private long currentXp;
        private int currentStreak;
        private String streakMsg;
        private int currentLevel;
    }
}