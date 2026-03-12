package com.rev.employee_management_service.service.impl;

import com.rev.employee_management_service.client.UserServiceClient;
import com.rev.employee_management_service.dto.request.CreateDepartmentRequest;
import com.rev.employee_management_service.dto.request.UpdateDepartmentRequest;
import com.rev.employee_management_service.dto.response.DepartmentResponse;
import com.rev.employee_management_service.entity.Department;
import com.rev.employee_management_service.exception.DepartmentNotFoundException;
import com.rev.employee_management_service.mapper.DepartmentMapper;
import com.rev.employee_management_service.repository.DepartmentRepository;
import com.rev.employee_management_service.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository repository;
    private final DepartmentMapper mapper;
    private final UserServiceClient userServiceClient;

    @Override
    public DepartmentResponse createDepartment(CreateDepartmentRequest request) {
        if (repository.existsByName(request.getName())) {
            throw new RuntimeException("Department with this name already exists");
        }
        Department department = mapper.toEntity(request);
        return mapper.toResponse(repository.save(department));
    }

    @Override
    public DepartmentResponse updateDepartment(Long id, UpdateDepartmentRequest request) {
        Department department = repository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found"));
        if (!department.getName().equals(request.getName()) && repository.existsByName(request.getName())) {
            throw new RuntimeException("Department with this name already exists");
        }
        mapper.updateEntityFromRequest(request, department);
        return mapper.toResponse(repository.save(department));
    }

    @Override
    public DepartmentResponse getDepartmentById(Long id) {
        return mapper.toResponse(repository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found")));
    }

    @Override
    public List<DepartmentResponse> getAllDepartments() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    public void deleteDepartment(Long id) {
        Department department = repository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found"));
        Long userCount = userServiceClient.countUsersByDepartment(id);
        if (userCount > 0) {
            throw new RuntimeException("Cannot delete department with assigned users");
        }
        repository.delete(department);
    }
}
