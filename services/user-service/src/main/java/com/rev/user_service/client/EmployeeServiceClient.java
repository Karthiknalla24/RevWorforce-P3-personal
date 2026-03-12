package com.rev.user_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Map;

@FeignClient(name = "employee-management-service")
public interface EmployeeServiceClient {

    @GetMapping("/api/departments/{id}")
    Map<String, Object> getDepartmentById(@PathVariable("id") Long id);

    @GetMapping("/api/designations/{id}")
    Map<String, Object> getDesignationById(@PathVariable("id") Long id);

    @GetMapping("/api/departments")
    java.util.List<Map<String, Object>> getAllDepartments();

    @GetMapping("/api/designations")
    java.util.List<Map<String, Object>> getAllDesignations();
}
