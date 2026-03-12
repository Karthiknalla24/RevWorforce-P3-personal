package com.rev.user_service.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserRequest {

    private String firstName;

    private String lastName;

    private String phone;

    private String address;

    private String city;

    private String state;

    private String zipCode;

    private String department;

    private String designation;

    private Long departmentId;

    private Long designationId;

    private Long managerId;

    private String emergencyContact;

    private String email;

    private Double salary;

    private com.rev.user_service.entity.Role role;

    private java.time.LocalDate joiningDate;

}