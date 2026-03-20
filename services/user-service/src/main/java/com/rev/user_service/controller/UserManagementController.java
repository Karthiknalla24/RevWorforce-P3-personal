package com.rev.user_service.controller;

import com.rev.user_service.dto.request.CreateUserRequest;
import com.rev.user_service.dto.request.SearchUserRequest;
import com.rev.user_service.dto.request.UpdateUserRequest;
import com.rev.user_service.dto.response.EmployeeDirectoryResponse;
import com.rev.user_service.dto.response.UserResponse;
import com.rev.user_service.service.UserManagementService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")

@Tag(name = "User Management API", description = "Operations related to user management")
public class UserManagementController {

    private final UserManagementService userService;

    public UserManagementController(UserManagementService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Get manager of a user")
    @ApiResponse(responseCode = "200", description = "Manager fetched successfully")
    @GetMapping("/{id}/manager")
    public ResponseEntity<UserResponse> getManager(@PathVariable("id") Long id) {

        return ResponseEntity.ok(userService.getManager(id));
    }

    @Operation(summary = "Get employee directory")
    @ApiResponse(responseCode = "200", description = "Employee directory retrieved")
    @GetMapping("/directory")
    public ResponseEntity<List<EmployeeDirectoryResponse>> getEmployeeDirectory() {

        return ResponseEntity.ok(userService.getEmployeeDirectory());
    }

    @Operation(summary = "Get team members under a manager")
    @GetMapping("/manager/{managerId}/team")
    public ResponseEntity<List<EmployeeDirectoryResponse>> getTeamMembers(
            @PathVariable("managerId") Long managerId) {

        return ResponseEntity.ok(userService.getTeamMembers(managerId));
    }

    @Operation(summary = "Create new user")
    @ApiResponse(responseCode = "200", description = "User created successfully")
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody CreateUserRequest request) {
        return ResponseEntity.ok(userService.createUser(request));
    }

    @Operation(summary = "Update existing user")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable("id") Long id,
            @RequestBody UpdateUserRequest request) {

        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    @Operation(summary = "Get user by ID")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable("id") Long id) {

        return ResponseEntity.ok(userService.getUserById(id));
    }

    @Operation(summary = "Search users")
    @PostMapping("/search")
    public ResponseEntity<List<EmployeeDirectoryResponse>> searchUsers(
            @RequestBody SearchUserRequest request) {

        return ResponseEntity.ok(userService.searchUsers(request));
    }

    @Operation(summary = "Deactivate user")
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateUser(@PathVariable("id") Long id) {

        userService.deactivateUser(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Reactivate user")
    @PutMapping("/{id}/reactivate")
    public ResponseEntity<Void> reactivateUser(@PathVariable("id") Long id) {

        userService.reactivateUser(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Count users by department")
    @GetMapping("/department/{departmentId}/count")
    public ResponseEntity<Long> countUsersByDepartment(@PathVariable("departmentId") Long departmentId) {
        return ResponseEntity.ok(userService.countByDepartmentId(departmentId));
    }

    @Operation(summary = "Count users by designation")
    @GetMapping("/designation/{designationId}/count")
    public ResponseEntity<Long> countUsersByDesignation(@PathVariable("designationId") Long designationId) {
        return ResponseEntity.ok(userService.countByDesignationId(designationId));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMyProfile(@RequestHeader("X-User-Id") Long userId) {
        return ResponseEntity.ok(userService.getMyProfile(userId));
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateMyProfile(@RequestHeader("X-User-Id") Long userId, @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(userService.updateMyProfile(userId, request));
    }

    @PutMapping("/me/change-password")
    public ResponseEntity<Void> changePassword(@RequestHeader("X-User-Id") Long userId, @RequestBody java.util.Map<String, String> request) {
        userService.changePassword(userId, request.get("currentPassword"), request.get("newPassword"));
        return ResponseEntity.ok().build();
    }

    @PutMapping("/assign-manager")
    public ResponseEntity<UserResponse> assignManager(@RequestBody java.util.Map<String, Long> request) {
        return ResponseEntity.ok(userService.assignManager(request.get("userId"), request.get("managerId")));
    }

    @GetMapping("/department/{departmentId}/users")
    public ResponseEntity<List<EmployeeDirectoryResponse>> getUsersByDepartment(@PathVariable("departmentId") Long departmentId) {
        return ResponseEntity.ok(userService.getUsersByDepartment(departmentId));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<EmployeeDirectoryResponse>> filterUsers(
            @RequestParam(name = "departmentId", required = false) Long departmentId,
            @RequestParam(name = "designationId", required = false) Long designationId,
            @RequestParam(name = "active", required = false) Boolean active,
            @RequestParam(name = "role", required = false) String role) {
        return ResponseEntity.ok(userService.filterUsers(departmentId, designationId, active, role));
    }

    @GetMapping("/managers")
    public ResponseEntity<List<EmployeeDirectoryResponse>> getAllManagers() {
        return ResponseEntity.ok(userService.filterUsers(null, null, true, "MANAGER"));
    }
}