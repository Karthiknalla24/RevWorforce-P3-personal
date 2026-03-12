package com.rev.reporting_service.config;

import com.rev.reporting_service.entity.ActivityLog;
import com.rev.reporting_service.repository.ActivityLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final ActivityLogRepository activityLogRepository;

    @Override
    public void run(String... args) throws Exception {
        if (activityLogRepository.count() == 0) {
            // Karthik (1)
            createLog(1L, "LOGIN", "Admin Karthik logged in to system");
            createLog(1L, "USER_CREATED", "Created user Vivek Murugan(EMP003)");
            
            // Chaithanya (2)
            createLog(2L, "LOGIN", "Manager Chaithanya logged in");
            createLog(2L, "LEAVE_APPROVED", "Approved leave application for Vivek Murugan");
            
            // Vivek Murugan(3)
            createLog(3L, "LOGIN", "Employee Vivek Murugan logged in");
            createLog(3L, "LEAVE_APPLIED", "Applied for Casual Leave: Family function");
            createLog(3L, "PROFILE_UPDATED", "Updated emergency contact and phone");
            createLog(3L, "GOAL_UPDATED", "Updated progress for 'Optimize Service Discovery' to 50%");
        }
    }

    private void createLog(Long userId, String action, String details) {
        ActivityLog log = new ActivityLog();
        log.setUserId(userId);
        log.setAction(action);
        log.setDetails(details);
        log.setCreatedAt(LocalDateTime.now().minusMinutes((long)(Math.random() * 1000)));
        activityLogRepository.save(log);
    }
}
