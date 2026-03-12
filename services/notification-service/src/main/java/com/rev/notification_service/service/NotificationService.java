package com.rev.notification_service.service;

import com.rev.notification_service.dto.response.NotificationResponse;

import java.util.List;

public interface NotificationService {
    List<NotificationResponse> getAllNotifications();
    List<NotificationResponse> getNotificationsByUserId(Long userId);
    void markAsRead(Long id, Long userId);
    void markAllAsRead(Long userId);
    void createNotification(Long userId, String message, String type);
    void createNotificationForAll(String message, String type);
    long getUnreadCount(Long userId);
    void clearNotifications(Long userId);
}
