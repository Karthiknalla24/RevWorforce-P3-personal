package com.rev.employee_management_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", contextId = "employeeUserServiceClient")
public interface UserServiceClient {

    @GetMapping("/api/users/department/{departmentId}/count")
    Long countUsersByDepartment(@PathVariable Long departmentId);

    @GetMapping("/api/users/designation/{designationId}/count")
    Long countUsersByDesignation(@PathVariable Long designationId);
}
