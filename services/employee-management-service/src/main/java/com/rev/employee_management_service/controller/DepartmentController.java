package com.rev.employee_management_service.controller;

import com.rev.employee_management_service.dto.request.CreateDepartmentRequest;
import com.rev.employee_management_service.dto.request.UpdateDepartmentRequest;
import com.rev.employee_management_service.dto.response.DepartmentResponse;
import com.rev.employee_management_service.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService service;

    @PostMapping
    public ResponseEntity<DepartmentResponse> create(@RequestBody CreateDepartmentRequest request) {
        return ResponseEntity.ok(service.createDepartment(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartmentResponse> update(@PathVariable("id") Long id, @RequestBody UpdateDepartmentRequest request) {
        return ResponseEntity.ok(service.updateDepartment(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentResponse> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.getDepartmentById(id));
    }

    @GetMapping
    public ResponseEntity<List<DepartmentResponse>> getAll() {
        return ResponseEntity.ok(service.getAllDepartments());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        service.deleteDepartment(id);
        return ResponseEntity.ok().build();
    }
}
