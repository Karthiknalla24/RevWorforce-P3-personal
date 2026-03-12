package com.rev.employee_management_service.dto.response;

import lombok.Data;

@Data
public class EmployeeResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Long departmentId;
    private String departmentName;
    private Long designationId;
    private String designationName;
}
