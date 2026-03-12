package com.rev.performance_service.dto.request;

import lombok.Data;

@Data
public class UpdateGoalStatusRequest {
    private Integer progress;
    private String status;    // NOT_STARTED, IN_PROGRESS, COMPLETED
    private String comment;   // Optional manager comment
}
