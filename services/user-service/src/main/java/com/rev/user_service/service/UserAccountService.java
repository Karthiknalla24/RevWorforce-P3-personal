package com.rev.user_service.service;

import com.rev.user_service.dto.request.CreateUserRequest;
import com.rev.user_service.dto.request.UpdateUserRequest;
import com.rev.user_service.dto.response.UserResponse;

/**
 * Interface Segregation Principle: Handles core user identity and account lifecycle.
 */
public interface UserAccountService {
    UserResponse createUser(CreateUserRequest request);
    UserResponse updateUser(Long userId, UpdateUserRequest request);
    UserResponse getUserById(Long userId);
    void deactivateUser(Long userId);
    void reactivateUser(Long userId);
    void changePassword(Long userId, String currentPassword, String newPassword);
    UserResponse getMyProfile(Long userId);
    UserResponse updateMyProfile(Long userId, UpdateUserRequest request);
}
