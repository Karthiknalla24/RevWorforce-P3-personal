package com.rev.employee_management_service.service;

import com.rev.employee_management_service.dto.request.CreateAnnouncementRequest;
import com.rev.employee_management_service.dto.request.UpdateAnnouncementRequest;
import com.rev.employee_management_service.dto.response.AnnouncementResponse;

import java.util.List;

public interface AnnouncementService {
    AnnouncementResponse createAnnouncement(CreateAnnouncementRequest request);
    AnnouncementResponse updateAnnouncement(Long id, UpdateAnnouncementRequest request);
    AnnouncementResponse getAnnouncementById(Long id);
    List<AnnouncementResponse> getAllAnnouncements();
    void deleteAnnouncement(Long id);
}
