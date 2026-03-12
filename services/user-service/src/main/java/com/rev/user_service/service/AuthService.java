package com.rev.user_service.service;

import com.rev.user_service.dto.request.LoginRequest;
import com.rev.user_service.dto.response.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);

}