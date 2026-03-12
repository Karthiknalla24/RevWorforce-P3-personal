package com.rev.performance_service.dto.request;

import lombok.Data;

@Data
public class ManagerFeedbackRequest {
    private String feedback;
    private Integer managerRating;
    private Integer rating;
}
