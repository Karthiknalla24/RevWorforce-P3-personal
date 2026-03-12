package com.rev.performance_service.dto.request;

import lombok.Data;

@Data
public class SubmitReviewRequest {
    private Long employeeId;
    private Long userId;
    private Integer year;
    private String deliverables;
    private String accomplishments;
    private String areasOfAccomplishment;
    private String areasOfAccomplishments;
    private String areasOfImprovement;
    private String areasOfImprovements;
    private String improvementAreas;
    private String improvements;
    private Integer selfRating;
}
