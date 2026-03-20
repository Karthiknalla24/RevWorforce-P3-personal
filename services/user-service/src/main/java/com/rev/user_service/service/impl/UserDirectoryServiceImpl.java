package com.rev.user_service.service.impl;

import com.rev.user_service.dto.request.SearchUserRequest;
import com.rev.user_service.dto.response.EmployeeDirectoryResponse;
import com.rev.user_service.entity.User;
import com.rev.user_service.mapper.UserMapper;
import com.rev.user_service.repository.UserRepository;
import com.rev.user_service.service.UserDirectoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserDirectoryServiceImpl implements UserDirectoryService {

    private final UserRepository userRepository;
    private final com.rev.user_service.client.EmployeeServiceClient employeeServiceClient;

    @Override
    public List<EmployeeDirectoryResponse> searchUsers(SearchUserRequest request) {
        List<User> allUsers = userRepository.findAll();
        Map<Long, String> departments = fetchDepartmentNames();
        Map<Long, String> designations = fetchDesignationNames();
        Map<Long, String> userNames = fetchUserNames(allUsers);

        return allUsers.stream()
                .filter(user -> matches(user, request, departments, designations))
                .map(u -> UserMapper.toEmployeeDirectoryResponse(u, u.getManagerId() != null ? userNames.get(u.getManagerId()) : "None"))
                .collect(Collectors.toList());
    }

    private boolean matches(User user, SearchUserRequest request, Map<Long, String> depts, Map<Long, String> desigs) {
        if (request.getName() != null && !request.getName().isBlank()) {
            String fullName = (user.getFirstName() + " " + user.getLastName()).toLowerCase();
            if (!fullName.contains(request.getName().toLowerCase())) return false;
        }
        if (request.getDepartment() != null && !request.getDepartment().isBlank()) {
            String d = user.getDepartment() != null ? user.getDepartment() : depts.get(user.getDepartmentId());
            if (d == null || !d.toLowerCase().contains(request.getDepartment().toLowerCase())) return false;
        }
        if (request.getDesignation() != null && !request.getDesignation().isBlank()) {
            String d = user.getDesignation() != null ? user.getDesignation() : desigs.get(user.getDesignationId());
            if (d == null || !d.toLowerCase().contains(request.getDesignation().toLowerCase())) return false;
        }
        return true;
    }

    @Override
    public List<EmployeeDirectoryResponse> getEmployeeDirectory() {
        List<User> users = userRepository.findAll();
        Map<Long, String> userNames = fetchUserNames(users);
        return users.stream()
                .map(u -> UserMapper.toEmployeeDirectoryResponse(u, u.getManagerId() != null ? userNames.get(u.getManagerId()) : "None"))
                .collect(Collectors.toList());
    }

    @Override
    public long countByDepartmentId(Long departmentId) { return userRepository.countByDepartmentId(departmentId); }

    @Override
    public long countByDesignationId(Long designationId) { return userRepository.countByDesignationId(designationId); }

    @Override
    public List<EmployeeDirectoryResponse> getUsersByDepartment(Long departmentId) {
        return getEmployeeDirectory().stream().filter(u -> u.getDepartmentId() != null && u.getDepartmentId().equals(departmentId)).toList();
    }

    @Override
    public List<EmployeeDirectoryResponse> filterUsers(Long departmentId, Long designationId, Boolean active, String role) {
        return getEmployeeDirectory().stream().filter(u -> 
            (departmentId == null || departmentId.equals(u.getDepartmentId())) &&
            (designationId == null || designationId.equals(u.getDesignationId())) &&
            (active == null || active.equals(u.isActive())) &&
            (role == null || role.equalsIgnoreCase(u.getRole() != null ? u.getRole().toString() : null))
        ).toList();
    }

    private Map<Long, String> fetchDepartmentNames() {
        try { return employeeServiceClient.getAllDepartments().stream().collect(Collectors.toMap(d -> Long.valueOf(d.get("id").toString()), d -> d.get("name").toString(), (a, b) -> a)); }
        catch (Exception e) { return Map.of(); }
    }

    private Map<Long, String> fetchDesignationNames() {
        try { return employeeServiceClient.getAllDesignations().stream().collect(Collectors.toMap(d -> Long.valueOf(d.get("id").toString()), d -> d.get("title").toString(), (a, b) -> a)); }
        catch (Exception e) { return Map.of(); }
    }

    private Map<Long, String> fetchUserNames(List<User> users) {
        return users.stream().collect(Collectors.toMap(User::getId, u -> (u.getFirstName() != null ? u.getFirstName() : "") + " " + (u.getLastName() != null ? u.getLastName() : ""), (a, b) -> a));
    }
}
