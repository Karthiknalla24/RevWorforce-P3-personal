package com.rev.performance_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoalResponse {
    private Long id;
    private Long userId;
    private String employeeName;
    private String title;
    private String description;
    private LocalDate deadline;
    private Integer progress;
    private String status;      // NOT_STARTED, IN_PROGRESS, COMPLETED
    private String priority;    // LOW, MEDIUM, HIGH
    private String managerComment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
