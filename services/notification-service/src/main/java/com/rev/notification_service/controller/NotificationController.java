package com.rev.notification_service.controller;

import com.rev.notification_service.dto.request.CreateNotificationRequest;
import com.rev.notification_service.dto.response.NotificationResponse;
import com.rev.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService service;

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getAllNotifications() {
        return ResponseEntity.ok(service.getAllNotifications());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationResponse>> getNotificationsByUserId(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(service.getNotificationsByUserId(userId));
    }

    @GetMapping("/user/{userId}/unread-count")
    public ResponseEntity<Long> getUnreadCount(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(service.getUnreadCount(userId));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<Map<String, String>> markAsRead(@PathVariable("id") Long id, @RequestParam("userId") Long userId) {
        service.markAsRead(id, userId);
        return ResponseEntity.ok(Map.of("message", "Success"));
    }

    @PutMapping("/user/{userId}/read-all")
    public ResponseEntity<Map<String, String>> markAllAsRead(@PathVariable("userId") Long userId) {
        service.markAllAsRead(userId);
        return ResponseEntity.ok(Map.of("message", "Success"));
    }

    @DeleteMapping("/user/{userId}/clear")
    public ResponseEntity<Map<String, String>> clearNotifications(@PathVariable("userId") Long userId) {
        service.clearNotifications(userId);
        return ResponseEntity.ok(Map.of("message", "Success"));
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> createNotification(@RequestBody CreateNotificationRequest request) {
        service.createNotification(request.getUserId(), request.getMessage(), request.getType());
        return ResponseEntity.ok(Map.of("message", "Success"));
    }
}
