package com.rev.user_service.service;

import com.rev.user_service.dto.request.SearchUserRequest;
import com.rev.user_service.dto.response.EmployeeDirectoryResponse;
import java.util.List;

/**
 * Interface Segregation Principle: Handles browsing and searching the user directory.
 */
public interface UserDirectoryService {
    List<EmployeeDirectoryResponse> searchUsers(SearchUserRequest request);
    List<EmployeeDirectoryResponse> getEmployeeDirectory();
    long countByDepartmentId(Long departmentId);
    long countByDesignationId(Long designationId);
    List<EmployeeDirectoryResponse> getUsersByDepartment(Long departmentId);
    List<EmployeeDirectoryResponse> filterUsers(Long departmentId, Long designationId, Boolean active, String role);
}
