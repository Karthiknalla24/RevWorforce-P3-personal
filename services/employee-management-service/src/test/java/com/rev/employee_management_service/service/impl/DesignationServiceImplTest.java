package com.rev.employee_management_service.service.impl;

import com.rev.employee_management_service.client.UserServiceClient;
import com.rev.employee_management_service.dto.request.CreateDesignationRequest;
import com.rev.employee_management_service.dto.request.UpdateDesignationRequest;
import com.rev.employee_management_service.dto.response.DesignationResponse;
import com.rev.employee_management_service.entity.Designation;
import com.rev.employee_management_service.mapper.DesignationMapper;
import com.rev.employee_management_service.repository.DesignationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DesignationServiceImplTest {

    @Mock
    private DesignationRepository repository;

    @Mock
    private DesignationMapper mapper;

    @Mock
    private UserServiceClient userServiceClient;

    @InjectMocks
    private DesignationServiceImpl designationService;

    private Designation designation;
    private DesignationResponse response;

    @BeforeEach
    void setUp() {
        designation = new Designation();
        designation.setId(1L);
        designation.setTitle("Senior Developer");

        response = new DesignationResponse();
        response.setId(1L);
        response.setTitle("Senior Developer");
    }

    @Test
    void createDesignation_Success() {
        CreateDesignationRequest request = new CreateDesignationRequest();
        request.setTitle("Senior Developer");

        when(repository.existsByTitle(request.getTitle())).thenReturn(false);
        when(mapper.toEntity(request)).thenReturn(designation);
        when(repository.save(designation)).thenReturn(designation);
        when(mapper.toResponse(designation)).thenReturn(response);

        DesignationResponse result = designationService.createDesignation(request);

        assertNotNull(result);
        assertEquals("Senior Developer", result.getTitle());
    }

    @Test
    void updateDesignation_Success() {
        UpdateDesignationRequest request = new UpdateDesignationRequest();
        request.setTitle("Lead Developer");

        when(repository.findById(1L)).thenReturn(Optional.of(designation));
        when(repository.existsByTitle("Lead Developer")).thenReturn(false);
        when(repository.save(designation)).thenReturn(designation);
        when(mapper.toResponse(designation)).thenReturn(response);

        DesignationResponse result = designationService.updateDesignation(1L, request);

        assertNotNull(result);
        verify(mapper).updateEntityFromRequest(request, designation);
    }

    @Test
    void deleteDesignation_Success() {
        when(repository.findById(1L)).thenReturn(Optional.of(designation));
        when(userServiceClient.countUsersByDesignation(1L)).thenReturn(0L);

        designationService.deleteDesignation(1L);

        verify(repository).delete(designation);
    }

    @Test
    void deleteDesignation_HasUsers() {
        when(repository.findById(1L)).thenReturn(Optional.of(designation));
        when(userServiceClient.countUsersByDesignation(1L)).thenReturn(3L);

        assertThrows(RuntimeException.class, () -> designationService.deleteDesignation(1L));
        verify(repository, never()).delete(any());
    }
}
