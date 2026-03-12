package com.rev.reporting_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "employee-management-service")
public interface EmployeeClient {
    
    @GetMapping("/api/departments")
    Object getAllDepartments();
    
    @GetMapping("/api/departments/{id}")
    Object getDepartmentById(@PathVariable Long id);
    
    @GetMapping("/api/designations")
    Object getAllDesignations();
    
    @GetMapping("/api/announcements")
    Object getAllAnnouncements();
}
