package com.rev.performance_service.service;

import com.rev.performance_service.entity.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Interface defining the contract for performance review and goal management.
 * Follows the Dependency Inversion Principle — controllers depend on this abstraction,
 * not the concrete implementation.
 */
public interface PerformanceService {

    // --- Performance Reviews ---
    PerformanceReview createReview(Long userId, int year, String deliverables, String accomplishments, String improvements, int selfRating);

    PerformanceReview submitReview(Long reviewId);

    PerformanceReview provideFeedback(Long reviewId, String feedback, int managerRating);

    List<PerformanceReview> getMyReviews(Long userId);

    List<PerformanceReview> getTeamReviews(Long managerId, String role);

    // --- Goals ---
    Goal createGoal(Long userId, String title, String description, LocalDate deadline, GoalPriority priority);

    Goal updateGoalProgress(Long goalId, int progress, GoalStatus status);

    Goal addGoalComment(Long goalId, String comment);

    List<Goal> getMyGoals(Long userId);

    List<Goal> getTeamGoals(Long managerId, String role);

    void deleteGoal(Long goalId);
}
