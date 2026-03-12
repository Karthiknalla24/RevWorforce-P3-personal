package com.rev.employee_management_service.service;

import com.rev.employee_management_service.dto.request.CreateDepartmentRequest;
import com.rev.employee_management_service.dto.request.UpdateDepartmentRequest;
import com.rev.employee_management_service.dto.response.DepartmentResponse;

import java.util.List;

public interface DepartmentService {
    DepartmentResponse createDepartment(CreateDepartmentRequest request);
    DepartmentResponse updateDepartment(Long id, UpdateDepartmentRequest request);
    DepartmentResponse getDepartmentById(Long id);
    List<DepartmentResponse> getAllDepartments();
    void deleteDepartment(Long id);
}
