package com.rev.performance_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PerformanceResponse {
    private Long id;
    private Long userId;
    private String employeeName;
    private int year;
    private String deliverables;
    private String accomplishments;
    private String areasOfAccomplishment;
    private String improvements;
    private String areasOfImprovement;
    private int selfRating;
    private int managerRating;
    private String managerFeedback;
    private String status;      // DRAFT, SUBMITTED, REVIEWED
    private String ratingLabel; // Excellent, Good, Average, Needs Improvement
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
