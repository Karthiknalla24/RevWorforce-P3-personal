package com.rev.employee_management_service.service.impl;

import com.rev.employee_management_service.client.UserServiceClient;
import com.rev.employee_management_service.dto.request.CreateDepartmentRequest;
import com.rev.employee_management_service.dto.request.UpdateDepartmentRequest;
import com.rev.employee_management_service.dto.response.DepartmentResponse;
import com.rev.employee_management_service.entity.Department;
import com.rev.employee_management_service.exception.DepartmentNotFoundException;
import com.rev.employee_management_service.mapper.DepartmentMapper;
import com.rev.employee_management_service.repository.DepartmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceImplTest {

    @Mock
    private DepartmentRepository repository;

    @Mock
    private DepartmentMapper mapper;

    @Mock
    private UserServiceClient userServiceClient;

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    private Department department;
    private DepartmentResponse response;

    @BeforeEach
    void setUp() {
        department = new Department();
        department.setId(1L);
        department.setName("Engineering");

        response = new DepartmentResponse();
        response.setId(1L);
        response.setName("Engineering");
    }

    @Test
    void createDepartment_Success() {
        CreateDepartmentRequest request = new CreateDepartmentRequest();
        request.setName("Engineering");

        when(repository.existsByName(request.getName())).thenReturn(false);
        when(mapper.toEntity(request)).thenReturn(department);
        when(repository.save(department)).thenReturn(department);
        when(mapper.toResponse(department)).thenReturn(response);

        DepartmentResponse result = departmentService.createDepartment(request);

        assertNotNull(result);
        assertEquals("Engineering", result.getName());
        verify(repository).save(any());
    }

    @Test
    void createDepartment_AlreadyExists() {
        CreateDepartmentRequest request = new CreateDepartmentRequest();
        request.setName("Engineering");

        when(repository.existsByName(request.getName())).thenReturn(true);

        assertThrows(RuntimeException.class, () -> departmentService.createDepartment(request));
        verify(repository, never()).save(any());
    }

    @Test
    void updateDepartment_Success() {
        UpdateDepartmentRequest request = new UpdateDepartmentRequest();
        request.setName("R&D");

        when(repository.findById(1L)).thenReturn(Optional.of(department));
        when(repository.existsByName("R&D")).thenReturn(false);
        when(repository.save(department)).thenReturn(department);
        when(mapper.toResponse(department)).thenReturn(response);

        DepartmentResponse result = departmentService.updateDepartment(1L, request);

        assertNotNull(result);
        verify(mapper).updateEntityFromRequest(request, department);
        verify(repository).save(department);
    }

    @Test
    void updateDepartment_NotFound() {
        UpdateDepartmentRequest request = new UpdateDepartmentRequest();
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(DepartmentNotFoundException.class, () -> departmentService.updateDepartment(1L, request));
    }

    @Test
    void getDepartmentById_Success() {
        when(repository.findById(1L)).thenReturn(Optional.of(department));
        when(mapper.toResponse(department)).thenReturn(response);

        DepartmentResponse result = departmentService.getDepartmentById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getAllDepartments_Success() {
        when(repository.findAll()).thenReturn(Arrays.asList(department));
        when(mapper.toResponse(department)).thenReturn(response);

        List<DepartmentResponse> result = departmentService.getAllDepartments();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void deleteDepartment_Success() {
        when(repository.findById(1L)).thenReturn(Optional.of(department));
        when(userServiceClient.countUsersByDepartment(1L)).thenReturn(0L);

        assertDoesNotThrow(() -> departmentService.deleteDepartment(1L));
        verify(repository).delete(department);
    }

    @Test
    void deleteDepartment_HasUsers() {
        when(repository.findById(1L)).thenReturn(Optional.of(department));
        when(userServiceClient.countUsersByDepartment(1L)).thenReturn(5L);

        assertThrows(RuntimeException.class, () -> departmentService.deleteDepartment(1L));
        verify(repository, never()).delete(any());
    }
}
