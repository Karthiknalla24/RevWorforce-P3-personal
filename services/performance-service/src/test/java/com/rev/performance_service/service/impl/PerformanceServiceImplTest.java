package com.rev.performance_service.service.impl;

import com.rev.performance_service.entity.*;
import java.time.LocalDate;
import com.rev.performance_service.repository.GoalRepository;
import com.rev.performance_service.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PerformanceServiceImplTest {

    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private GoalRepository goalRepository;

    @InjectMocks
    private PerformanceServiceImpl performanceService;

    private PerformanceReview review;
    private Goal goal;

    @BeforeEach
    void setUp() {
        review = new PerformanceReview();
        review.setId(1L);
        review.setUserId(10L);
        review.setYear(2026);
        review.setStatus(ReviewStatus.DRAFT);

        goal = new Goal();
        goal.setId(1L);
        goal.setUserId(10L);
        goal.setTitle("Complete Unit Tests");
        goal.setStatus(GoalStatus.NOT_STARTED);
    }

    @Test
    void createReview_Success() {
        when(reviewRepository.save(any(PerformanceReview.class))).thenReturn(review);

        PerformanceReview result = performanceService.createReview(10L, 2026, "D", "A", "I", 5);

        assertNotNull(result);
        assertEquals(2026, result.getYear());
        verify(reviewRepository).save(any());
    }

    @Test
    void submitReview_Success() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
        when(reviewRepository.save(any(PerformanceReview.class))).thenReturn(review);

        PerformanceReview result = performanceService.submitReview(1L);

        assertEquals(ReviewStatus.SUBMITTED, result.getStatus());
    }

    @Test
    void provideFeedback_Success() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
        when(reviewRepository.save(any(PerformanceReview.class))).thenReturn(review);

        PerformanceReview result = performanceService.provideFeedback(1L, "Good job", 4);

        assertEquals(ReviewStatus.REVIEWED, result.getStatus());
        assertEquals("Good job", result.getManagerFeedback());
    }

    @Test
    void createGoal_Success() {
        when(goalRepository.save(any(Goal.class))).thenReturn(goal);

        Goal result = performanceService.createGoal(10L, "Complete Unit Tests", "Desc", LocalDate.now(), GoalPriority.HIGH);

        assertNotNull(result);
        assertEquals("Complete Unit Tests", result.getTitle());
    }

    @Test
    void updateGoalProgress_Success() {
        when(goalRepository.findById(1L)).thenReturn(Optional.of(goal));
        when(goalRepository.save(any(Goal.class))).thenReturn(goal);

        Goal result = performanceService.updateGoalProgress(1L, 80, GoalStatus.IN_PROGRESS);

        assertEquals(80, result.getProgress());
        assertEquals(GoalStatus.IN_PROGRESS, result.getStatus());
    }
}
