package com.rev.user_service.service;

import com.rev.user_service.dto.request.CreateUserRequest;
import com.rev.user_service.dto.request.SearchUserRequest;
import com.rev.user_service.dto.request.UpdateUserRequest;
import com.rev.user_service.dto.response.EmployeeDirectoryResponse;
import com.rev.user_service.dto.response.UserResponse;

import java.util.List;

public interface UserManagementService {

    UserResponse createUser(CreateUserRequest request);

    UserResponse updateUser(Long userId, UpdateUserRequest request);

    UserResponse getUserById(Long userId);

    List<EmployeeDirectoryResponse> searchUsers(SearchUserRequest request);

    void deactivateUser(Long userId);

    void reactivateUser(Long userId);

    UserResponse getManager(Long userId);

    List<EmployeeDirectoryResponse> getTeamMembers(Long managerId);

    List<EmployeeDirectoryResponse> getEmployeeDirectory();

    long countByDepartmentId(Long departmentId);

    long countByDesignationId(Long designationId);

    // Missing methods
    UserResponse getMyProfile(Long userId);

    UserResponse updateMyProfile(Long userId, UpdateUserRequest request);

    void changePassword(Long userId, String currentPassword, String newPassword);

    UserResponse assignManager(Long userId, Long managerId);

    List<EmployeeDirectoryResponse> getUsersByDepartment(Long departmentId);

    List<EmployeeDirectoryResponse> getUsersByManager(Long managerId);

    List<EmployeeDirectoryResponse> filterUsers(Long departmentId, Long designationId, Boolean active, String role);

}