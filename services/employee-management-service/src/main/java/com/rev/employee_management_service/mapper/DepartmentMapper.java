package com.rev.employee_management_service.mapper;

import com.rev.employee_management_service.dto.request.CreateDepartmentRequest;
import com.rev.employee_management_service.dto.request.UpdateDepartmentRequest;
import com.rev.employee_management_service.dto.response.DepartmentResponse;
import com.rev.employee_management_service.entity.Department;
import org.springframework.stereotype.Component;

@Component
public class DepartmentMapper {

    public Department toEntity(CreateDepartmentRequest request) {
        Department department = new Department();
        department.setName(request.getName());
        department.setDescription(request.getDescription());
        return department;
    }

    public DepartmentResponse toResponse(Department department) {
        DepartmentResponse response = new DepartmentResponse();
        response.setId(department.getId());
        response.setName(department.getName());
        response.setDescription(department.getDescription());
        return response;
    }

    public void updateEntityFromRequest(UpdateDepartmentRequest request, Department department) {
        department.setName(request.getName());
        department.setDescription(request.getDescription());
    }
}
