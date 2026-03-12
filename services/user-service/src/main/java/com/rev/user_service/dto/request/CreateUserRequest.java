package com.rev.user_service.dto.request;

import com.rev.user_service.entity.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserRequest {

    private String employeeId;

    private String email;

    private String firstName;

    private String lastName;

    private String password;

    private String phone;

    private String department;

    private String designation;

    private Long departmentId;

    private Long designationId;

    private Long managerId;

    private Role role;
    
    private String emergencyContact;
    
    private java.time.LocalDate joiningDate;

    private String address;

    private Double salary;

}