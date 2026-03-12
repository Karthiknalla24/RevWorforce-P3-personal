package com.rev.user_service.dto.response;

import com.rev.user_service.entity.Role;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private Long id;
    
    private String name;

    private String employeeId;

    private String email;

    private String firstName;

    private String lastName;

    private String phone;

    private String address;

    private String city;

    private String state;

    private String zipCode;

    private String department;
    
    private String departmentName; // for frontend

    private String designation;
    
    private String designationTitle; // for frontend

    private Long managerId;
    
    private ManagerResponse manager; // for frontend nested manager profile

    private Role role;

    private Boolean active;
    
    private String emergencyContact;
    
    private java.time.LocalDate joiningDate;

    private Double salary;

    // Helper for nested manager info
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ManagerResponse {
        private Long id;
        private String firstName;
        private String lastName;
        private String email;
        private String phone;
    }
}