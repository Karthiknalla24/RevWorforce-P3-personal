package com.rev.performance_service.dto.request;

import lombok.Data;

@Data
public class CreateGoalRequest {
    private Long employeeId;
    private Long userId;
    private String title;
    private String description;
    private String deadline;
    private String priority;
}
