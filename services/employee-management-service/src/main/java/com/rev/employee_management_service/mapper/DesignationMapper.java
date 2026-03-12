package com.rev.employee_management_service.mapper;

import com.rev.employee_management_service.dto.request.CreateDesignationRequest;
import com.rev.employee_management_service.dto.request.UpdateDesignationRequest;
import com.rev.employee_management_service.dto.response.DesignationResponse;
import com.rev.employee_management_service.entity.Designation;
import org.springframework.stereotype.Component;

@Component
public class DesignationMapper {

    public Designation toEntity(CreateDesignationRequest request) {
        Designation designation = new Designation();
        designation.setTitle(request.getTitle());
        designation.setDescription(request.getDescription());
        return designation;
    }

    public DesignationResponse toResponse(Designation designation) {
        DesignationResponse response = new DesignationResponse();
        response.setId(designation.getId());
        response.setTitle(designation.getTitle());
        response.setDescription(designation.getDescription());
        return response;
    }

    public void updateEntityFromRequest(UpdateDesignationRequest request, Designation designation) {
        designation.setTitle(request.getTitle());
        designation.setDescription(request.getDescription());
    }
}
