package com.rev.user_service.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeDirectoryResponse {

    private Long id;

    private String employeeId;

    private String firstName;

    private String lastName;

    private String name;

    private String email;

    private String phone;

    private String department;
    
    private String departmentName; // Frontend expects this

    private String designation;
    
    private String designationTitle; // Frontend expects this
    
    private Long departmentId;
    
    private Long designationId;
    
    private Long managerId; // Needed for manager assignment view
    
    private String managerName; // Added for frontend display
    
    private boolean active;
    
    private String role; // Needed for filters/roles
    
    private Double salary;
    
    private java.time.LocalDate joiningDate;

}