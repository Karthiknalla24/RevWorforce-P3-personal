package com.rev.employee_management_service.service.impl;

import com.rev.employee_management_service.dto.request.CreateAnnouncementRequest;
import com.rev.employee_management_service.dto.request.UpdateAnnouncementRequest;
import com.rev.employee_management_service.dto.response.AnnouncementResponse;
import com.rev.employee_management_service.entity.Announcement;
import com.rev.employee_management_service.mapper.AnnouncementMapper;
import com.rev.employee_management_service.repository.AnnouncementRepository;
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
public class AnnouncementServiceImplTest {

    @Mock
    private AnnouncementRepository repository;

    @Mock
    private AnnouncementMapper mapper;

    @InjectMocks
    private AnnouncementServiceImpl announcementService;

    private Announcement announcement;
    private AnnouncementResponse response;

    @BeforeEach
    void setUp() {
        announcement = new Announcement();
        announcement.setId(1L);
        announcement.setTitle("Office Party");

        response = new AnnouncementResponse();
        response.setId(1L);
        response.setTitle("Office Party");
    }

    @Test
    void createAnnouncement_Success() {
        CreateAnnouncementRequest request = new CreateAnnouncementRequest();
        when(mapper.toEntity(request)).thenReturn(announcement);
        when(repository.save(announcement)).thenReturn(announcement);
        when(mapper.toResponse(announcement)).thenReturn(response);

        AnnouncementResponse result = announcementService.createAnnouncement(request);

        assertNotNull(result);
        assertEquals("Office Party", result.getTitle());
    }

    @Test
    void updateAnnouncement_Success() {
        UpdateAnnouncementRequest request = new UpdateAnnouncementRequest();
        when(repository.findById(1L)).thenReturn(Optional.of(announcement));
        when(repository.save(announcement)).thenReturn(announcement);
        when(mapper.toResponse(announcement)).thenReturn(response);

        AnnouncementResponse result = announcementService.updateAnnouncement(1L, request);

        assertNotNull(result);
        verify(mapper).updateEntityFromRequest(request, announcement);
    }

    @Test
    void deleteAnnouncement_Success() {
        when(repository.findById(1L)).thenReturn(Optional.of(announcement));

        announcementService.deleteAnnouncement(1L);

        verify(repository).delete(announcement);
    }
}
