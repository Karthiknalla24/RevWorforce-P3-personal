package com.rev.user_service.service;

import com.rev.user_service.dto.response.UserResponse;
import com.rev.user_service.dto.response.EmployeeDirectoryResponse;
import java.util.List;

/**
 * Interface Segregation Principle: Handles organizational hierarchy and relationships.
 */
public interface UserRelationshipService {
    UserResponse getManager(Long userId);
    List<EmployeeDirectoryResponse> getTeamMembers(Long managerId);
    UserResponse assignManager(Long userId, Long managerId);
    List<EmployeeDirectoryResponse> getUsersByManager(Long managerId);
}
