package com.rev.reporting_service.controller;

import com.rev.reporting_service.dto.ActivityLogResponse;
import com.rev.reporting_service.service.ActivityLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/activity")
@RequiredArgsConstructor
public class ActivityLogController {

    private final ActivityLogService activityLogService;

    @GetMapping
    public ResponseEntity<Page<ActivityLogResponse>> getPaginatedActivities(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(activityLogService.getPaginatedActivities(pageable));
    }

    @GetMapping("/all")
    public ResponseEntity<List<ActivityLogResponse>> getAllActivities() {
        return ResponseEntity.ok(activityLogService.getAllActivities());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Page<ActivityLogResponse>> getActivitiesByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(activityLogService.getActivitiesByUser(userId, pageable));
    }

    @PostMapping
    public ResponseEntity<Void> logActivity(@RequestBody Map<String, Object> request) {
        Long userId = Long.valueOf(request.get("userId").toString());
        String action = request.get("action").toString();
        String details = request.getOrDefault("details", "").toString();
        activityLogService.logActivity(userId, action, details);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/cleanup")
    public ResponseEntity<Void> cleanupLogs(@RequestParam String duration) {
        activityLogService.deleteLogsByDuration(duration);
        return ResponseEntity.ok().build();
    }
}
