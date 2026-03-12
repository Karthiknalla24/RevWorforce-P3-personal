package com.rev.employee_management_service.service;

import com.rev.employee_management_service.dto.request.CreateEmployeeRequest;
import com.rev.employee_management_service.dto.request.UpdateEmployeeRequest;
import com.rev.employee_management_service.dto.response.EmployeeResponse;

import java.util.List;

public interface EmployeeService {
    EmployeeResponse createEmployee(CreateEmployeeRequest request);
    EmployeeResponse updateEmployee(Long id, UpdateEmployeeRequest request);
    EmployeeResponse getEmployeeById(Long id);
    List<EmployeeResponse> getAllEmployees();
    void deleteEmployee(Long id);
    List<EmployeeResponse> getEmployeesByDepartment(Long departmentId);
}
