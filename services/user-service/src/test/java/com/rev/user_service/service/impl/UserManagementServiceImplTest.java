package com.rev.user_service.service.impl;

import com.rev.user_service.client.EmployeeServiceClient;
import com.rev.user_service.client.LeaveServiceClient;
import com.rev.user_service.client.ReportingServiceClient;
import com.rev.user_service.dto.request.CreateUserRequest;
import com.rev.user_service.dto.request.UpdateUserRequest;
import com.rev.user_service.dto.response.UserResponse;
import com.rev.user_service.entity.Role;
import com.rev.user_service.entity.User;
import com.rev.user_service.exception.BadRequestException;
import com.rev.user_service.exception.ResourceNotFoundException;
import com.rev.user_service.repository.UserRepository;
import com.rev.user_service.security.PasswordEncoderUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserManagementServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoderUtil passwordEncoder;

    @Mock
    private LeaveServiceClient leaveServiceClient;

    @Mock
    private EmployeeServiceClient employeeServiceClient;

    @Mock
    private ReportingServiceClient reportingServiceClient;

    @InjectMocks
    private UserManagementServiceImpl userManagementService;

    private User user;
    private CreateUserRequest createRequest;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .employeeId("EMP001")
                .email("test@rev.com")
                .firstName("Vivek")
                .lastName("Murugan")
                .role(Role.EMPLOYEE)
                .password("encoded")
                .active(true)
                .build();

        createRequest = new CreateUserRequest();
        createRequest.setEmail("test@rev.com");
        createRequest.setFirstName("Vivek");
        createRequest.setLastName("Murugan");
        createRequest.setPassword("password");
        createRequest.setRole(Role.EMPLOYEE);
    }

    @Test
    void createUser_Success() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.findAll()).thenReturn(new ArrayList<>());
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponse response = userManagementService.createUser(createRequest);

        assertNotNull(response);
        assertEquals("Vivek Murugan", response.getName());
        verify(userRepository).save(any(User.class));
        verify(leaveServiceClient, atLeastOnce()).assignBalance(any());
    }

    @Test
    void createUser_EmailExists() {
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(BadRequestException.class, () -> userManagementService.createUser(createRequest));
    }

    @Test
    void getUserById_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserResponse response = userManagementService.getUserById(1L);

        assertNotNull(response);
        assertEquals("Vivek Murugan", response.getName());
    }

    @Test
    void getUserById_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userManagementService.getUserById(1L));
    }

    @Test
    void updateUser_Success() {
        UpdateUserRequest updateRequest = new UpdateUserRequest();
        updateRequest.setFirstName("Vivekny");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponse response = userManagementService.updateUser(1L, updateRequest);

        assertNotNull(response);
        verify(userRepository).save(user);
    }

    @Test
    void deactivateUser_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userManagementService.deactivateUser(1L);

        assertFalse(user.getActive());
        verify(userRepository).save(user);
    }

    @Test
    void changePassword_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("old", "encoded")).thenReturn(true);
        when(passwordEncoder.encode("new")).thenReturn("new_encoded");

        userManagementService.changePassword(1L, "old", "new");

        verify(userRepository).save(user);
        assertEquals("new_encoded", user.getPassword());
    }

    @Test
    void changePassword_IncorrectCurrent() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "encoded")).thenReturn(false);

        assertThrows(BadRequestException.class, () -> userManagementService.changePassword(1L, "wrong", "new"));
    }

    @Test
    void getEmployeeDirectory_Success() {
        when(userRepository.findAll()).thenReturn(java.util.Collections.singletonList(user));
        
        List<com.rev.user_service.dto.response.EmployeeDirectoryResponse> directory = userManagementService.getEmployeeDirectory();
        
        assertFalse(directory.isEmpty());
        assertEquals("Vivek Murugan", directory.get(0).getName());
    }
}
