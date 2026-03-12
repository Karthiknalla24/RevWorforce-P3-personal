package com.rev.employee_management_service.service.impl;

import com.rev.employee_management_service.client.UserServiceClient;
import com.rev.employee_management_service.dto.request.CreateDesignationRequest;
import com.rev.employee_management_service.dto.request.UpdateDesignationRequest;
import com.rev.employee_management_service.dto.response.DesignationResponse;
import com.rev.employee_management_service.entity.Designation;
import com.rev.employee_management_service.mapper.DesignationMapper;
import com.rev.employee_management_service.repository.DesignationRepository;
import com.rev.employee_management_service.service.DesignationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DesignationServiceImpl implements DesignationService {

    private final DesignationRepository repository;
    private final DesignationMapper mapper;
    private final UserServiceClient userServiceClient;

    @Override
    public DesignationResponse createDesignation(CreateDesignationRequest request) {
        if (repository.existsByTitle(request.getTitle())) {
            throw new RuntimeException("Designation with this title already exists");
        }
        Designation designation = mapper.toEntity(request);
        return mapper.toResponse(repository.save(designation));
    }

    @Override
    public DesignationResponse updateDesignation(Long id, UpdateDesignationRequest request) {
        Designation designation = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Designation not found"));
        if (!designation.getTitle().equals(request.getTitle()) && repository.existsByTitle(request.getTitle())) {
            throw new RuntimeException("Designation with this title already exists");
        }
        mapper.updateEntityFromRequest(request, designation);
        return mapper.toResponse(repository.save(designation));
    }

    @Override
    public DesignationResponse getDesignationById(Long id) {
        return mapper.toResponse(repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Designation not found")));
    }

    @Override
    public List<DesignationResponse> getAllDesignations() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    public void deleteDesignation(Long id) {
        Designation designation = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Designation not found"));
        Long userCount = userServiceClient.countUsersByDesignation(id);
        if (userCount > 0) {
            throw new RuntimeException("Cannot delete designation assigned to users");
        }
        repository.delete(designation);
    }
}
