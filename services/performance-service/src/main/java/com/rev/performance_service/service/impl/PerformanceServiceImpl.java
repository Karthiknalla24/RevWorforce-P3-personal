package com.rev.performance_service.service.impl;

import com.rev.performance_service.client.NotificationClient;
import com.rev.performance_service.client.UserClient;
import com.rev.performance_service.client.ReportingServiceClient;
import com.rev.performance_service.entity.*;
import com.rev.performance_service.repository.GoalRepository;
import com.rev.performance_service.repository.ReviewRepository;
import com.rev.performance_service.service.PerformanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class PerformanceServiceImpl implements PerformanceService {

    private final ReviewRepository reviewRepository;
    private final GoalRepository goalRepository;
    private final UserClient userClient;
    private final NotificationClient notificationClient;
    private final ReportingServiceClient reportingServiceClient;

    private void logActivity(Long userId, String action, String details) {
        try {
            Map<String, Object> log = new HashMap<>();
            log.put("userId", userId);
            log.put("action", action);
            log.put("details", details);
            reportingServiceClient.logActivity(log);
        } catch (Exception e) {
            System.err.println("Failed to log activity for user " + userId + ": " + e.getMessage());
        }
    }

    // --- Performance Reviews ---

    @Override
    public PerformanceReview createReview(Long userId, int year, String deliverables, String accomplishments, String improvements, int selfRating) {
        // Check for existing review for the same year
        if (reviewRepository.existsByUserIdAndYear(userId, year)) {
            throw new RuntimeException("A performance review already exists for user ID " + userId + " for the year " + year);
        }

        PerformanceReview review = new PerformanceReview();
        review.setUserId(userId);
        review.setYear(year);
        review.setDeliverables(deliverables);
        review.setAccomplishments(accomplishments);
        review.setImprovements(improvements);
        review.setSelfRating(selfRating);
        review.setStatus(ReviewStatus.DRAFT);
        
        PerformanceReview saved = reviewRepository.save(review);
        logActivity(userId, "REVIEW_CREATED", "Created performance review for year " + year);
        return saved;
    }

    @Override
    public PerformanceReview submitReview(Long reviewId) {
        PerformanceReview review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));
        
        review.setStatus(ReviewStatus.SUBMITTED);
        
        // Notify Manager
        try {
            Map<String, Object> empInfo = userClient.getUserById(review.getUserId());
            String empName = (empInfo != null && empInfo.get("name") != null) ? 
                    empInfo.get("name").toString() : "Employee ID: " + review.getUserId();

            Map<String, Object> manager = userClient.getManager(review.getUserId());
            if (manager != null && manager.containsKey("id")) {
                Long managerId = Long.valueOf(manager.get("id").toString());
                Map<String, Object> notification = new HashMap<>();
                notification.put("userId", managerId);
                notification.put("message", empName + " has submitted their performance review");
                notification.put("type", "REVIEW_SUBMITTED_TO_MANAGER");
                notificationClient.createNotification(notification);
            }

            // ADMIN Oversight: Notify Admins if a Manager submitted a review
            String userRole = (empInfo != null && empInfo.get("role") != null) ? empInfo.get("role").toString() : "";
            System.out.println("[PerformanceService] User role for review: " + userRole);
            if ("MANAGER".equalsIgnoreCase(userRole)) {
                System.out.println("[PerformanceService] Notifying Admins for Manager review...");
                List<Map<String, Object>> admins = userClient.filterUsers(null, null, true, "ADMIN");
                System.out.println("[PerformanceService] Found " + (admins != null ? admins.size() : 0) + " admins");
                if (admins != null) {
                    for (Map<String, Object> admin : admins) {
                        System.out.println("[PerformanceService] Notifying Admin " + admin.get("id"));
                        Map<String, Object> adminNotification = new HashMap<>();
                        adminNotification.put("userId", Long.valueOf(admin.get("id").toString()));
                        adminNotification.put("message", "[Manager Performance Review] " + empName + " (Manager) has submitted their performance review");
                        adminNotification.put("type", "MANAGER_REVIEW_SUBMITTED");
                        notificationClient.createNotification(adminNotification);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("[PerformanceService] Failed to notify admins for review: " + e.getMessage());
        }
        
        // Notify Employee
        try {
            Map<String, Object> empNotification = new HashMap<>();
            empNotification.put("userId", review.getUserId());
            empNotification.put("message", "Performance review submitted successfully");
            empNotification.put("type", "REVIEW_SUBMITTED");
            notificationClient.createNotification(empNotification);
        } catch (Exception e) {}
        
        PerformanceReview saved = reviewRepository.save(review);
        logActivity(review.getUserId(), "REVIEW_SUBMITTED", "Submitted performance review for year " + review.getYear());
        return saved;
    }

    @Override
    public PerformanceReview provideFeedback(Long reviewId, String feedback, int managerRating) {
        PerformanceReview review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));
        
        review.setManagerFeedback(feedback);
        review.setManagerRating(managerRating);
        review.setStatus(ReviewStatus.REVIEWED);
        
        Map<String, Object> notification = new HashMap<>();
        notification.put("userId", review.getUserId());
        notification.put("message", "Manager has provided feedback on your performance review");
        notification.put("type", "REVIEW_FEEDBACK");
        notificationClient.createNotification(notification);
        
        PerformanceReview saved = reviewRepository.save(review);
        logActivity(review.getUserId(), "REVIEW_FEEDBACK_GIVEN", "Manager provided feedback on review #" + reviewId);
        return saved;
    }

    @Override
    public List<PerformanceReview> getMyReviews(Long userId) {
        return reviewRepository.findByUserId(userId);
    }

    @Override
    public List<PerformanceReview> getTeamReviews(Long managerId, String role) {
        List<Long> memberIds = new java.util.ArrayList<>();
        Map<Long, String> nameMap = new HashMap<>();

        if ("ADMIN".equalsIgnoreCase(role)) {
            List<Map<String, Object>> managers = userClient.getAllManagers();
            for (Map<String, Object> m : managers) {
                if (m.get("id") != null) {
                    Long id = Long.valueOf(m.get("id").toString());
                    memberIds.add(id);
                    nameMap.put(id, m.get("name") != null ? m.get("name").toString() : "Unknown");
                }
            }
        } else {
            List<Map<String, Object>> teamMembers = userClient.getTeamMembers(managerId);
            if (teamMembers == null || teamMembers.isEmpty()) {
                return java.util.Collections.emptyList();
            }
            for (Map<String, Object> m : teamMembers) {
                if (m.get("id") != null) {
                    Long id = Long.valueOf(m.get("id").toString());
                    memberIds.add(id);
                    nameMap.put(id, m.get("name") != null ? m.get("name").toString() : "Unknown");
                }
            }
        }
        
        return reviewRepository.findAll().stream()
                .filter(r -> r.getUserId() != null && memberIds.contains(r.getUserId()))
                .peek(r -> r.setEmployeeName(nameMap.get(r.getUserId())))
                .collect(java.util.stream.Collectors.toList());
    }

    // --- Goals ---

    @Override
    public Goal createGoal(Long userId, String title, String description, LocalDate deadline, GoalPriority priority) {
        Goal goal = new Goal();
        goal.setUserId(userId);
        goal.setTitle(title);
        goal.setDescription(description);
        goal.setDeadline(deadline);
        goal.setProgress(0);
        goal.setStatus(GoalStatus.NOT_STARTED);
        goal.setPriority(priority != null ? priority : GoalPriority.MEDIUM);
        
        Goal savedGoal = goalRepository.save(goal);

        // Notify Manager
        try {
             Map<String, Object> empInfo = userClient.getUserById(userId);
             String empName = (empInfo != null && empInfo.get("name") != null) ? 
                     empInfo.get("name").toString() : "Employee ID: " + userId;

            Map<String, Object> manager = userClient.getManager(userId);
            if (manager != null && manager.containsKey("id")) {
                Long managerId = Long.valueOf(manager.get("id").toString());
                Map<String, Object> notification = new HashMap<>();
                notification.put("userId", managerId);
                notification.put("message", empName + " has created a new performance goal: " + title);
                notification.put("type", "GOAL_CREATED");
                notificationClient.createNotification(notification);
            }

            // ADMIN Oversight: Notify Admins if a Manager created a goal
            String userRole = (empInfo != null && empInfo.get("role") != null) ? empInfo.get("role").toString() : "";
            System.out.println("[PerformanceService] User role for goal: " + userRole);
            if ("MANAGER".equalsIgnoreCase(userRole)) {
                System.out.println("[PerformanceService] Notifying Admins for Manager goal...");
                List<Map<String, Object>> admins = userClient.filterUsers(null, null, true, "ADMIN");
                System.out.println("[PerformanceService] Found " + (admins != null ? admins.size() : 0) + " admins");
                if (admins != null) {
                    for (Map<String, Object> admin : admins) {
                        System.out.println("[PerformanceService] Notifying Admin " + admin.get("id"));
                        Map<String, Object> adminNotification = new HashMap<>();
                        adminNotification.put("userId", Long.valueOf(admin.get("id").toString()));
                        adminNotification.put("message", "[Manager Goal] " + empName + " (Manager) has created a new performance goal: " + title);
                        adminNotification.put("type", "MANAGER_GOAL_CREATED");
                        notificationClient.createNotification(adminNotification);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("[PerformanceService] Failed to notify admins for goal: " + e.getMessage());
        }

        // Notify Employee
        try {
            Map<String, Object> empNotification = new HashMap<>();
            empNotification.put("userId", userId);
            empNotification.put("message", "Performance goal created successfully: " + title);
            empNotification.put("type", "GOAL_CREATED_SUCCESS");
            notificationClient.createNotification(empNotification);
        } catch (Exception e) {}

        logActivity(userId, "GOAL_CREATED", "Created a new goal: " + title);

        return savedGoal;
    }

    @Override
    public Goal updateGoalProgress(Long goalId, int progress, GoalStatus status) {
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new RuntimeException("Goal not found"));
        
        goal.setProgress(progress);
        goal.setStatus(status);
        
        Goal saved = goalRepository.save(goal);
        logActivity(goal.getUserId(), "GOAL_UPDATED", "Updated goal '" + goal.getTitle() + "' progress to " + progress + "%");
        return saved;
    }

    @Override
    public Goal addGoalComment(Long goalId, String comment) {
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new RuntimeException("Goal not found"));
        
        goal.setManagerComment(comment);
        
        Map<String, Object> notification = new HashMap<>();
        notification.put("userId", goal.getUserId());
        notification.put("message", "Manager has commented on your goal");
        notification.put("type", "GOAL_COMMENT");
        notificationClient.createNotification(notification);
        
        Goal saved = goalRepository.save(goal);
        logActivity(goal.getUserId(), "GOAL_COMMENT_ADDED", "Manager added a comment to goal: " + goal.getTitle());
        return saved;
    }

    @Override
    public List<Goal> getMyGoals(Long userId) {
        return goalRepository.findByUserId(userId);
    }

    @Override
    public List<Goal> getTeamGoals(Long managerId, String role) {
        List<Long> memberIds = new java.util.ArrayList<>();
        Map<Long, String> nameMap = new HashMap<>();

        if ("ADMIN".equalsIgnoreCase(role)) {
            List<Map<String, Object>> managers = userClient.getAllManagers();
            for (Map<String, Object> m : managers) {
                if (m.get("id") != null) {
                    Long id = Long.valueOf(m.get("id").toString());
                    memberIds.add(id);
                    nameMap.put(id, m.get("name") != null ? m.get("name").toString() : "Unknown");
                }
            }
        } else {
            List<Map<String, Object>> teamMembers = userClient.getTeamMembers(managerId);
            if (teamMembers == null || teamMembers.isEmpty()) {
                return java.util.Collections.emptyList();
            }
            for (Map<String, Object> m : teamMembers) {
                if (m.get("id") != null) {
                    Long id = Long.valueOf(m.get("id").toString());
                    memberIds.add(id);
                    nameMap.put(id, m.get("name") != null ? m.get("name").toString() : "Unknown");
                }
            }
        }
        
        return goalRepository.findAll().stream()
                .filter(g -> g.getUserId() != null && memberIds.contains(g.getUserId()))
                .peek(g -> g.setEmployeeName(nameMap.get(g.getUserId())))
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public void deleteGoal(Long goalId) {
        goalRepository.deleteById(goalId);
    }
}
