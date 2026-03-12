package com.rev.user_service.service.impl;

import com.rev.user_service.client.ReportingServiceClient;
import com.rev.user_service.dto.request.LoginRequest;
import com.rev.user_service.dto.response.LoginResponse;
import com.rev.user_service.entity.Role;
import com.rev.user_service.entity.User;
import com.rev.user_service.exception.BadRequestException;
import com.rev.user_service.repository.UserRepository;
import com.rev.user_service.security.JwtUtil;
import com.rev.user_service.security.PasswordEncoderUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PasswordEncoderUtil passwordEncoder;

    @Mock
    private ReportingServiceClient reportingServiceClient;

    @InjectMocks
    private AuthServiceImpl authService;

    private User user;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .employeeId("EMP001")
                .email("test@rev.com")
                .password("encoded")
                .role(Role.EMPLOYEE)
                .active(true)
                .build();

        loginRequest = new LoginRequest();
        loginRequest.setIdentifier("EMP001");
        loginRequest.setPassword("password");
    }

    @Test
    void login_Success() {
        when(userRepository.findByEmployeeIdOrEmail(anyString(), anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "encoded")).thenReturn(true);
        when(jwtUtil.generateToken(user)).thenReturn("mock_token");

        LoginResponse response = authService.login(loginRequest);

        assertNotNull(response);
        assertEquals("mock_token", response.getToken());
        verify(reportingServiceClient).logActivity(any());
    }

    @Test
    void login_InvalidIdentifier() {
        when(userRepository.findByEmployeeIdOrEmail(anyString(), anyString())).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class, () -> authService.login(loginRequest));
    }

    @Test
    void login_InvalidPassword() {
        when(userRepository.findByEmployeeIdOrEmail(anyString(), anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "encoded")).thenReturn(false);
        loginRequest.setPassword("wrong");

        assertThrows(BadRequestException.class, () -> authService.login(loginRequest));
    }

    @Test
    void login_DeactivatedUser() {
        user.setActive(false);
        when(userRepository.findByEmployeeIdOrEmail(anyString(), anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "encoded")).thenReturn(true);

        assertThrows(BadRequestException.class, () -> authService.login(loginRequest));
    }
}
