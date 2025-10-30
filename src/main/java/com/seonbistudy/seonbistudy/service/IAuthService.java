package com.seonbistudy.seonbistudy.service;

import com.seonbistudy.seonbistudy.dto.auth.AuthResponse;
import com.seonbistudy.seonbistudy.dto.auth.LoginRequest;
import com.seonbistudy.seonbistudy.dto.auth.RegisterRequest;

public interface IAuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}
