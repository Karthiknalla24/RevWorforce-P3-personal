package com.rev.notification_service.repository;

import com.rev.notification_service.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserId(Long userId);
    List<Notification> findByUserIdAndReadStatusFalse(Long userId);
    long countByUserIdAndReadStatusFalse(Long userId);
}
