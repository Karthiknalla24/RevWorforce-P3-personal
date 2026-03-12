package com.rev.employee_management_service.controller;

import com.rev.employee_management_service.dto.request.CreateAnnouncementRequest;
import com.rev.employee_management_service.dto.request.UpdateAnnouncementRequest;
import com.rev.employee_management_service.dto.response.AnnouncementResponse;
import com.rev.employee_management_service.service.AnnouncementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/announcements")
@RequiredArgsConstructor
public class AnnouncementController {

    private final AnnouncementService service;

    @PostMapping
    public ResponseEntity<AnnouncementResponse> create(@RequestBody CreateAnnouncementRequest request) {
        return ResponseEntity.ok(service.createAnnouncement(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnnouncementResponse> update(@PathVariable("id") Long id, @RequestBody UpdateAnnouncementRequest request) {
        return ResponseEntity.ok(service.updateAnnouncement(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnnouncementResponse> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.getAnnouncementById(id));
    }

    @GetMapping
    public ResponseEntity<List<AnnouncementResponse>> getAll() {
        return ResponseEntity.ok(service.getAllAnnouncements());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        service.deleteAnnouncement(id);
        return ResponseEntity.ok().build();
    }
}
