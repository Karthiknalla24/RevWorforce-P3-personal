package com.rev.employee_management_service.controller;

import com.rev.employee_management_service.dto.request.CreateDesignationRequest;
import com.rev.employee_management_service.dto.request.UpdateDesignationRequest;
import com.rev.employee_management_service.dto.response.DesignationResponse;
import com.rev.employee_management_service.service.DesignationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/designations")
@RequiredArgsConstructor
public class DesignationController {

    private final DesignationService service;

    @PostMapping
    public ResponseEntity<DesignationResponse> create(@RequestBody CreateDesignationRequest request) {
        return ResponseEntity.ok(service.createDesignation(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DesignationResponse> update(@PathVariable("id") Long id, @RequestBody UpdateDesignationRequest request) {
        return ResponseEntity.ok(service.updateDesignation(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DesignationResponse> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.getDesignationById(id));
    }

    @GetMapping
    public ResponseEntity<List<DesignationResponse>> getAll() {
        return ResponseEntity.ok(service.getAllDesignations());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        service.deleteDesignation(id);
        return ResponseEntity.ok().build();
    }
}
