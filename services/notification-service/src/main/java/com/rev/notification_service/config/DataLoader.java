package com.rev.notification_service.config;

import com.rev.notification_service.entity.Notification;
import com.rev.notification_service.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final NotificationRepository notificationRepository;

    @Override
    public void run(String... args) throws Exception {
        if (notificationRepository.count() == 0) {
            // Karthik (1)
            createNotif(1L, "Welcome to RevWorkforce, Karthik! System admin access granted.", "SYSTEM", true);
            
            // Chaithanya (2)
            createNotif(2L, "You have a new leave application from Vivek Muruganawaiting approval.", "LEAVE_PENDING", false);
            
            // Vivek Murugan(3)
            createNotif(3L, "Your leave application for 'Family function' was approved.", "LEAVE_APPROVED", true);
            createNotif(3L, "Your performance review for 2026 is now available for self-assessment.", "REVIEW_DUE", false);
            createNotif(3L, "New goal assigned: 'Optimize Service Discovery'.", "GOAL_UPDATE", false);
        }
    }

    private void createNotif(Long userId, String msg, String type, boolean read) {
        Notification n = new Notification();
        n.setUserId(userId);
        n.setMessage(msg);
        n.setType(type);
        n.setReadStatus(read);
        notificationRepository.save(n);
    }
}
