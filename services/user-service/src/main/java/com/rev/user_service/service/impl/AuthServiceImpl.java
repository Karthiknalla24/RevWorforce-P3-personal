package com.rev.user_service.service.impl;

import com.rev.user_service.dto.request.LoginRequest;
import com.rev.user_service.dto.response.LoginResponse;
import com.rev.user_service.entity.User;
import com.rev.user_service.exception.BadRequestException;
import com.rev.user_service.repository.UserRepository;
import com.rev.user_service.security.JwtUtil;
import com.rev.user_service.security.PasswordEncoderUtil;
import com.rev.user_service.service.AuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoderUtil passwordEncoder;

    private final com.rev.user_service.client.ReportingServiceClient reportingServiceClient;

    public AuthServiceImpl(UserRepository userRepository,
                           JwtUtil jwtUtil,
                           PasswordEncoderUtil passwordEncoder,
                           com.rev.user_service.client.ReportingServiceClient reportingServiceClient) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.reportingServiceClient = reportingServiceClient;
    }

    @Override
    public LoginResponse login(LoginRequest request) {

        User user = userRepository
                .findByEmployeeIdOrEmail(request.getIdentifier(), request.getIdentifier())
                .orElseThrow(() -> new BadRequestException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadRequestException("Invalid credentials");
        }

        if (!user.getActive()) {
            throw new BadRequestException("User account is deactivated");
        }

        String token = jwtUtil.generateToken(user);

        // Log Login Activity
        try {
            java.util.Map<String, Object> log = new java.util.HashMap<>();
            log.put("userId", user.getId());
            log.put("action", "LOGIN");
            log.put("details", "User logged in with " + (request.getIdentifier().contains("@") ? "email" : "employee ID"));
            reportingServiceClient.logActivity(log);
        } catch (Exception e) {
            // Non-blocking log failure
            System.err.println("Failed to log activity: " + e.getMessage());
        }

        return LoginResponse.builder()
                .token(token)
                .userId(user.getId())
                .employeeId(user.getEmployeeId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .name((user.getFirstName() != null ? user.getFirstName() : "") + " " + (user.getLastName() != null ? user.getLastName() : ""))
                .role(user.getRole().name())
                .designation(user.getDesignation())
                .department(user.getDepartment())
                .build();
    }
}