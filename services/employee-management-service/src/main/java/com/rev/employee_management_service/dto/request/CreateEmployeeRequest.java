package com.rev.employee_management_service.dto.request;

import lombok.Data;

@Data
public class CreateEmployeeRequest {
    private Long id; // User ID from User Service
    private String firstName;
    private String lastName;
    private String email;
    private Long departmentId;
    private Long designationId;
}
