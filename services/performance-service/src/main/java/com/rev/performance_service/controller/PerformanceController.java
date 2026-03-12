package com.rev.performance_service.controller;

import com.rev.performance_service.entity.*;
import com.rev.performance_service.service.PerformanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/performance")
@RequiredArgsConstructor
public class PerformanceController {

    private final PerformanceService performanceService;

    private Long parseLong(Object obj) {
        if (obj == null || obj.toString().isEmpty()) return null;
        try {
            String str = obj.toString();
            if (str.contains(".")) {
                return Double.valueOf(str).longValue();
            }
            return Long.valueOf(str);
        } catch (Exception e) {
            return null;
        }
    }

    private Integer parseInt(Object obj) {
        if (obj == null || obj.toString().isEmpty()) return null;
        try {
            String str = obj.toString();
            if (str.contains(".")) {
                return Double.valueOf(str).intValue();
            }
            return Integer.valueOf(str);
        } catch (Exception e) {
            return null;
        }
    }

    @PostMapping("/reviews")
    public ResponseEntity<PerformanceReview> createReview(@RequestBody Map<String, Object> request) {
        Long userId = parseLong(request.getOrDefault("userId", request.get("employeeId")));
        if (userId == null) {
            throw new RuntimeException("User ID (userId or employeeId) is required");
        }
        
        Integer year = parseInt(request.get("year"));
        if (year == null) {
            year = java.time.LocalDate.now().getYear();
        }
        
        String deliverables = request.getOrDefault("deliverables", "").toString();
        String accomplishments = request.getOrDefault("areasOfAccomplishment", 
                                 request.getOrDefault("areasOfAccomplishments", 
                                 request.getOrDefault("accomplishments", ""))).toString();
        String improvements = request.getOrDefault("improvementAreas",
                              request.getOrDefault("areasOfImprovement", 
                              request.getOrDefault("areasOfImprovements", 
                              request.getOrDefault("improvements", "")))).toString();
        
        Integer selfRating = parseInt(request.getOrDefault("selfRating", request.get("rating")));
        if (selfRating == null) selfRating = 0;
        
        return ResponseEntity.ok(performanceService.createReview(userId, year, deliverables, accomplishments, improvements, selfRating));
    }

    @PutMapping("/reviews/{id}/submit")
    public ResponseEntity<PerformanceReview> submitReview(@PathVariable("id") Long id) {
        return ResponseEntity.ok(performanceService.submitReview(id));
    }

    @PutMapping("/reviews/{id}/feedback")
    public ResponseEntity<PerformanceReview> provideFeedback(@PathVariable("id") Long id, @RequestBody Map<String, Object> request) {
        String feedback = request.getOrDefault("feedback", request.getOrDefault("managerFeedback", "")).toString();
        
        Integer managerRating = parseInt(request.getOrDefault("managerRating", request.get("rating")));
        if (managerRating == null) managerRating = 0;
        
        return ResponseEntity.ok(performanceService.provideFeedback(id, feedback, managerRating));
    }

    @GetMapping("/reviews/user/{userId}")
    public ResponseEntity<List<PerformanceReview>> getMyReviews(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(performanceService.getMyReviews(userId));
    }

    @GetMapping("/reviews/manager/{managerId}/team")
    public ResponseEntity<List<PerformanceReview>> getTeamReviews(@PathVariable("managerId") Long managerId) {
        return ResponseEntity.ok(performanceService.getTeamReviews(managerId));
    }

    @PostMapping("/goals")
    public ResponseEntity<Goal> createGoal(@RequestBody Map<String, Object> request) {
        Long userId = parseLong(request.getOrDefault("userId", request.get("employeeId")));
        if (userId == null) {
            throw new RuntimeException("User ID (userId or employeeId) is required");
        }
        
        String title = request.getOrDefault("title", "New Goal").toString();
        String description = request.getOrDefault("description", "").toString();
        
        java.time.LocalDate deadline = null;
        if (request.containsKey("deadline") && request.get("deadline") != null) {
            try {
                deadline = java.time.LocalDate.parse(request.get("deadline").toString());
            } catch (Exception e) {}
        }
        
        GoalPriority priority;
        try {
            priority = GoalPriority.valueOf(request.getOrDefault("priority", "MEDIUM").toString());
        } catch (Exception e) {
            priority = GoalPriority.MEDIUM;
        }
        
        return ResponseEntity.ok(performanceService.createGoal(userId, title, description, deadline, priority));
    }

    @PutMapping("/goals/{id}/progress")
    public ResponseEntity<Goal> updateGoalProgress(@PathVariable("id") Long id, @RequestBody Map<String, Object> request) {
        Integer progress = parseInt(request.get("progress"));
        if (progress == null) progress = 0;
        
        GoalStatus status;
        if (request.containsKey("status") && request.get("status") != null) {
            try {
                status = GoalStatus.valueOf(request.get("status").toString());
            } catch (Exception e) {
                status = GoalStatus.IN_PROGRESS;
            }
        } else {
            if (progress >= 100) {
                status = GoalStatus.COMPLETED;
            } else if (progress > 0) {
                status = GoalStatus.IN_PROGRESS;
            } else {
                status = GoalStatus.NOT_STARTED;
            }
        }
        
        return ResponseEntity.ok(performanceService.updateGoalProgress(id, progress, status));
    }

    @PutMapping("/goals/{id}/comment")
    public ResponseEntity<Goal> addGoalComment(@PathVariable("id") Long id, @RequestBody Map<String, Object> request) {
        String comment = request.getOrDefault("comment", "").toString();
        
        return ResponseEntity.ok(performanceService.addGoalComment(id, comment));
    }

    @GetMapping("/goals/user/{userId}")
    public ResponseEntity<List<Goal>> getMyGoals(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(performanceService.getMyGoals(userId));
    }

    @GetMapping("/goals")
    public ResponseEntity<List<Goal>> getTeamGoals(@RequestHeader("X-User-Id") Long managerId) {
        return ResponseEntity.ok(performanceService.getTeamGoals(managerId));
    }

    @DeleteMapping("/goals/{id}")
    public ResponseEntity<Void> deleteGoal(@PathVariable("id") Long id) {
        performanceService.deleteGoal(id);
        return ResponseEntity.ok().build();
    }
}
