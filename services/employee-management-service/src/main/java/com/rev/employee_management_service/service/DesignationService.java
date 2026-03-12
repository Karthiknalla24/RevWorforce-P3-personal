package com.rev.employee_management_service.service;

import com.rev.employee_management_service.dto.request.CreateDesignationRequest;
import com.rev.employee_management_service.dto.request.UpdateDesignationRequest;
import com.rev.employee_management_service.dto.response.DesignationResponse;

import java.util.List;

public interface DesignationService {
    DesignationResponse createDesignation(CreateDesignationRequest request);
    DesignationResponse updateDesignation(Long id, UpdateDesignationRequest request);
    DesignationResponse getDesignationById(Long id);
    List<DesignationResponse> getAllDesignations();
    void deleteDesignation(Long id);
}
