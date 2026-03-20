package com.rev.notification_service.service.impl;

import com.rev.notification_service.dto.response.NotificationResponse;
import com.rev.notification_service.entity.Notification;
import com.rev.notification_service.exception.NotificationNotFoundException;
import com.rev.notification_service.mapper.NotificationMapper;
import com.rev.notification_service.repository.NotificationRepository;
import com.rev.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository repository;
    private final NotificationMapper mapper;

    @Override
    public List<NotificationResponse> getAllNotifications() {
        return mapper.toResponseList(repository.findAll());
    }

    @Override
    public List<NotificationResponse> getNotificationsByUserId(Long userId) {
        return mapper.toResponseList(repository.findByUserIdOrderByCreatedAtDesc(userId));
    }

    @Override
    public void markAsRead(Long id, Long userId) {
        Notification notification = repository.findById(id)
                .orElseThrow(() -> new NotificationNotFoundException("Notification not found"));
        notification.setReadStatus(true);
        repository.save(notification);
    }

    @Override
    public void markAllAsRead(Long userId) {
        List<Notification> notifications = repository.findByUserIdAndReadStatusFalseOrderByCreatedAtDesc(userId);
        notifications.forEach(n -> n.setReadStatus(true));
        repository.saveAll(notifications);
    }

    @Override
    @Async("notificationExecutor")
    public void createNotification(Long userId, String message, String type) {
        System.out.println("[NotificationService] Creating notification for user " + userId + " type: " + type);
        try {
            Notification notification = new Notification();
            notification.setUserId(userId);
            notification.setMessage(message);
            notification.setType(type);
            notification.setReadStatus(false);
            repository.save(notification);
            System.out.println("[NotificationService] Notification saved successfully");
        } catch (Exception e) {
            System.err.println("[NotificationService] Error saving notification: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    @Async("notificationExecutor")
    public void createNotificationForAll(String message, String type) {
        // This will be called by other services via Feign with list of userIds
        // For now, keeping it simple
    }

    @Override
    public long getUnreadCount(Long userId) {
        return repository.countByUserIdAndReadStatusFalse(userId);
    }

    @Override
    public void clearNotifications(Long userId) {
        List<Notification> notifications = repository.findByUserIdOrderByCreatedAtDesc(userId);
        repository.deleteAll(notifications);
    }
}
