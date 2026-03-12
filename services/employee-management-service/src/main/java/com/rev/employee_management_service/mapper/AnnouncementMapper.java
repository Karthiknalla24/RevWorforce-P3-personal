package com.rev.employee_management_service.mapper;

import com.rev.employee_management_service.dto.request.CreateAnnouncementRequest;
import com.rev.employee_management_service.dto.request.UpdateAnnouncementRequest;
import com.rev.employee_management_service.dto.response.AnnouncementResponse;
import com.rev.employee_management_service.entity.Announcement;
import org.springframework.stereotype.Component;

@Component
public class AnnouncementMapper {

    public Announcement toEntity(CreateAnnouncementRequest request) {
        Announcement announcement = new Announcement();
        announcement.setTitle(request.getTitle());
        announcement.setContent(request.getContent());
        return announcement;
    }

    public AnnouncementResponse toResponse(Announcement announcement) {
        AnnouncementResponse response = new AnnouncementResponse();
        response.setId(announcement.getId());
        response.setTitle(announcement.getTitle());
        response.setContent(announcement.getContent());
        response.setCreatedAt(announcement.getCreatedAt());
        return response;
    }

    public void updateEntityFromRequest(UpdateAnnouncementRequest request, Announcement announcement) {
        announcement.setTitle(request.getTitle());
        announcement.setContent(request.getContent());
    }
}
