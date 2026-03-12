package com.rev.employee_management_service.service.impl;

import com.rev.employee_management_service.dto.request.CreateAnnouncementRequest;
import com.rev.employee_management_service.dto.request.UpdateAnnouncementRequest;
import com.rev.employee_management_service.dto.response.AnnouncementResponse;
import com.rev.employee_management_service.entity.Announcement;
import com.rev.employee_management_service.mapper.AnnouncementMapper;
import com.rev.employee_management_service.repository.AnnouncementRepository;
import com.rev.employee_management_service.service.AnnouncementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnnouncementServiceImpl implements AnnouncementService {

    private final AnnouncementRepository repository;
    private final AnnouncementMapper mapper;

    @Override
    public AnnouncementResponse createAnnouncement(CreateAnnouncementRequest request) {
        Announcement announcement = mapper.toEntity(request);
        return mapper.toResponse(repository.save(announcement));
    }

    @Override
    public AnnouncementResponse updateAnnouncement(Long id, UpdateAnnouncementRequest request) {
        Announcement announcement = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Announcement not found"));
        mapper.updateEntityFromRequest(request, announcement);
        return mapper.toResponse(repository.save(announcement));
    }

    @Override
    public AnnouncementResponse getAnnouncementById(Long id) {
        return mapper.toResponse(repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Announcement not found")));
    }

    @Override
    public List<AnnouncementResponse> getAllAnnouncements() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    public void deleteAnnouncement(Long id) {
        Announcement announcement = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Announcement not found"));
        repository.delete(announcement);
    }
}
