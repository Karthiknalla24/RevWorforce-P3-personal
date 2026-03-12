package com.rev.user_service.mapper;

import com.rev.user_service.dto.request.CreateUserRequest;
import com.rev.user_service.dto.request.UpdateUserRequest;
import com.rev.user_service.dto.response.EmployeeDirectoryResponse;
import com.rev.user_service.dto.response.UserResponse;
import com.rev.user_service.entity.User;

public class UserMapper {

    public static User toEntity(CreateUserRequest request) {

        return User.builder()
                .employeeId(request.getEmployeeId())
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(request.getPassword())
                .phone(request.getPhone())
                .department(request.getDepartment())
                .designation(request.getDesignation())
                .departmentId(request.getDepartmentId())
                .designationId(request.getDesignationId())
                .managerId(request.getManagerId())
                .role(request.getRole())
                .emergencyContact(request.getEmergencyContact())
                .joiningDate(request.getJoiningDate())
                .address(request.getAddress())
                .salary(request.getSalary())
                .active(true)
                .build();
    }

    public static UserResponse toUserResponse(User user) {
        return toUserResponseEnriched(user, null);
    }

    public static UserResponse toUserResponseEnriched(User user, User manager) {
        String fullName = (user.getFirstName() != null ? user.getFirstName() : "") + 
                         (user.getLastName() != null ? " " + user.getLastName() : "");
        
        UserResponse response = UserResponse.builder()
                .id(user.getId())
                .name(fullName.trim())
                .employeeId(user.getEmployeeId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .address(user.getAddress())
                .city(user.getCity())
                .state(user.getState())
                .zipCode(user.getZipCode())
                .department(user.getDepartment())
                .departmentName(user.getDepartment()) // fallback for frontend
                .designation(user.getDesignation())
                .designationTitle(user.getDesignation()) // fallback for frontend
                .managerId(user.getManagerId())
                .role(user.getRole())
                .active(user.getActive())
                .emergencyContact(user.getEmergencyContact())
                .joiningDate(user.getJoiningDate())
                .salary(user.getSalary())
                .build();
                
        if (manager != null) {
            response.setManager(UserResponse.ManagerResponse.builder()
                    .id(manager.getId())
                    .firstName(manager.getFirstName())
                    .lastName(manager.getLastName())
                    .email(manager.getEmail())
                    .phone(manager.getPhone())
                    .build());
        }
        
        return response;
    }

    public static EmployeeDirectoryResponse toEmployeeDirectoryResponse(User user) {
        return toEmployeeDirectoryResponse(user, null);
    }

    public static EmployeeDirectoryResponse toEmployeeDirectoryResponse(User user, String managerName) {

        String fullName = (user.getFirstName() != null ? user.getFirstName() : "") + 
                         (user.getLastName() != null ? " " + user.getLastName() : "");
        
        return EmployeeDirectoryResponse.builder()
                .id(user.getId())
                .employeeId(user.getEmployeeId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .name(fullName.trim())
                .email(user.getEmail())
                .phone(user.getPhone())
                .department(user.getDepartment() != null ? user.getDepartment() : "N/A")
                .departmentName(user.getDepartment() != null ? user.getDepartment() : "N/A")
                .designation(user.getDesignation() != null ? user.getDesignation() : "N/A")
                .designationTitle(user.getDesignation() != null ? user.getDesignation() : "N/A")
                .departmentId(user.getDepartmentId())
                .designationId(user.getDesignationId())
                .managerId(user.getManagerId())
                .managerName(managerName != null ? managerName : "None")
                .active(user.getActive() != null ? user.getActive() : true)
                .role(user.getRole() != null ? user.getRole().name() : "EMPLOYEE")
                .joiningDate(user.getJoiningDate())
                .salary(user.getSalary())
                .build();
    }

    public static void updateUserFromRequest(UpdateUserRequest request, User user) {

        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }

        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }

        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }

        if (request.getAddress() != null) {
            user.setAddress(request.getAddress());
        }

        if (request.getCity() != null) {
            user.setCity(request.getCity());
        }

        if (request.getState() != null) {
            user.setState(request.getState());
        }

        if (request.getZipCode() != null) {
            user.setZipCode(request.getZipCode());
        }

        if (request.getDepartment() != null) {
            user.setDepartment(request.getDepartment());
        }

        if (request.getDesignation() != null) {
            user.setDesignation(request.getDesignation());
        }

        if (request.getDepartmentId() != null) {
            user.setDepartmentId(request.getDepartmentId());
        }

        if (request.getDesignationId() != null) {
            user.setDesignationId(request.getDesignationId());
        }

        if (request.getManagerId() != null) {
            user.setManagerId(request.getManagerId());
        }
        
        if (request.getEmergencyContact() != null) {
            user.setEmergencyContact(request.getEmergencyContact());
        }

        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }

        if (request.getSalary() != null) {
            user.setSalary(request.getSalary());
        }

        if (request.getRole() != null) {
            user.setRole(request.getRole());
        }

        if (request.getJoiningDate() != null) {
            user.setJoiningDate(request.getJoiningDate());
        }
    }
}