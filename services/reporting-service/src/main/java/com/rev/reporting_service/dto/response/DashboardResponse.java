package com.rev.reporting_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {
    private Object totalUsers;
    private Object totalDepartments;
    private Object leaveTypes;
    private long totalActiveEmployees;
    private long totalPendingLeaves;
    private long totalReviewsSubmitted;
    private long timestamp;
}
