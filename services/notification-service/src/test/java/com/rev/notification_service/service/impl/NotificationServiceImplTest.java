package com.rev.notification_service.service.impl;

import com.rev.notification_service.dto.response.NotificationResponse;
import com.rev.notification_service.entity.Notification;
import com.rev.notification_service.mapper.NotificationMapper;
import com.rev.notification_service.repository.NotificationRepository;
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
public class NotificationServiceImplTest {

    @Mock
    private NotificationRepository repository;

    @Mock
    private NotificationMapper mapper;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    private Notification notification;

    @BeforeEach
    void setUp() {
        notification = new Notification();
        notification.setId(1L);
        notification.setUserId(10L);
        notification.setMessage("Test Notification");
        notification.setReadStatus(false);
    }

    @Test
    void createNotification_Success() {
        notificationService.createNotification(10L, "Test Notification", "INFO");
        verify(repository).save(any(Notification.class));
    }

    @Test
    void markAsRead_Success() {
        when(repository.findById(1L)).thenReturn(Optional.of(notification));
        when(repository.save(any(Notification.class))).thenReturn(notification);

        notificationService.markAsRead(1L, 10L);

        assertTrue(notification.isReadStatus());
        verify(repository).save(notification);
    }

    @Test
    void markAllAsRead_Success() {
        when(repository.findByUserIdAndReadStatusFalse(10L)).thenReturn(Arrays.asList(notification));

        notificationService.markAllAsRead(10L);

        assertTrue(notification.isReadStatus());
        verify(repository).saveAll(any());
    }

    @Test
    void getUnreadCount_Success() {
        when(repository.countByUserIdAndReadStatusFalse(10L)).thenReturn(5L);

        long result = notificationService.getUnreadCount(10L);

        assertEquals(5L, result);
    }

    @Test
    void clearNotifications_Success() {
        when(repository.findByUserId(10L)).thenReturn(Arrays.asList(notification));
        notificationService.clearNotifications(10L);
        verify(repository).deleteAll(any());
    }
}
