package com.rev.user_service.service.impl;

import com.rev.user_service.dto.request.CreateUserRequest;
import com.rev.user_service.dto.request.SearchUserRequest;
import com.rev.user_service.dto.request.UpdateUserRequest;
import com.rev.user_service.dto.response.EmployeeDirectoryResponse;
import com.rev.user_service.dto.response.UserResponse;
import com.rev.user_service.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * SRP & ISP Refactor: UserManagementServiceImpl is now a Facade.
 * It coordinates between specialized services while maintaining backward compatibility
 * for the UserManagementController and external clients.
 */
@Service
@Primary
@RequiredArgsConstructor
public class UserManagementServiceImpl implements UserManagementService {

    private final UserAccountService accountService;
    private final UserRelationshipService relationshipService;
    private final UserDirectoryService directoryService;

    @Override
    public UserResponse createUser(CreateUserRequest request) {
        return accountService.createUser(request);
    }

    @Override
    public UserResponse updateUser(Long userId, UpdateUserRequest request) {
        return accountService.updateUser(userId, request);
    }

    @Override
    public UserResponse getUserById(Long userId) {
        return accountService.getUserById(userId);
    }

    @Override
    public List<EmployeeDirectoryResponse> searchUsers(SearchUserRequest request) {
        return directoryService.searchUsers(request);
    }

    @Override
    public void deactivateUser(Long userId) {
        accountService.deactivateUser(userId);
    }

    @Override
    public void reactivateUser(Long userId) {
        accountService.reactivateUser(userId);
    }

    @Override
    public UserResponse getManager(Long userId) {
        return relationshipService.getManager(userId);
    }

    @Override
    public List<EmployeeDirectoryResponse> getTeamMembers(Long managerId) {
        return relationshipService.getTeamMembers(managerId);
    }

    @Override
    public List<EmployeeDirectoryResponse> getEmployeeDirectory() {
        return directoryService.getEmployeeDirectory();
    }

    @Override
    public long countByDepartmentId(Long departmentId) {
        return directoryService.countByDepartmentId(departmentId);
    }

    @Override
    public long countByDesignationId(Long designationId) {
        return directoryService.countByDesignationId(designationId);
    }

    @Override
    public UserResponse getMyProfile(Long userId) {
        return accountService.getMyProfile(userId);
    }

    @Override
    public UserResponse updateMyProfile(Long userId, UpdateUserRequest request) {
        return accountService.updateMyProfile(userId, request);
    }

    @Override
    public void changePassword(Long userId, String currentPassword, String newPassword) {
        accountService.changePassword(userId, currentPassword, newPassword);
    }

    @Override
    public UserResponse assignManager(Long userId, Long managerId) {
        return relationshipService.assignManager(userId, managerId);
    }

    @Override
    public List<EmployeeDirectoryResponse> getUsersByDepartment(Long departmentId) {
        return directoryService.getUsersByDepartment(departmentId);
    }

    @Override
    public List<EmployeeDirectoryResponse> getUsersByManager(Long managerId) {
        return relationshipService.getUsersByManager(managerId);
    }

    @Override
    public List<EmployeeDirectoryResponse> filterUsers(Long departmentId, Long designationId, Boolean active, String role) {
        return directoryService.filterUsers(departmentId, designationId, active, role);
    }
}