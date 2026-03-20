package com.rev.employee_management_service.controller;

import com.rev.employee_management_service.dto.request.CreateEmployeeRequest;
import com.rev.employee_management_service.dto.request.UpdateEmployeeRequest;
import com.rev.employee_management_service.dto.response.EmployeeResponse;
import com.rev.employee_management_service.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
@Tag(name = "Employee API", description = "Handles operations related to employee profile management")
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    @Operation(summary = "Create a new employee profile")
    public ResponseEntity<EmployeeResponse> createEmployee(@RequestBody CreateEmployeeRequest request) {
        return ResponseEntity.ok(employeeService.createEmployee(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing employee profile")
    public ResponseEntity<EmployeeResponse> updateEmployee(@PathVariable("id") Long id, @RequestBody UpdateEmployeeRequest request) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get employee profile by ID")
    public ResponseEntity<EmployeeResponse> getEmployeeById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @GetMapping
    @Operation(summary = "Get all employee profiles")
    public ResponseEntity<List<EmployeeResponse>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an employee profile")
    public ResponseEntity<Void> deleteEmployee(@PathVariable("id") Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/department/{departmentId}")
    @Operation(summary = "Get employees by department")
    public ResponseEntity<List<EmployeeResponse>> getEmployeesByDepartment(@PathVariable("departmentId") Long departmentId) {
        return ResponseEntity.ok(employeeService.getEmployeesByDepartment(departmentId));
    }
}
