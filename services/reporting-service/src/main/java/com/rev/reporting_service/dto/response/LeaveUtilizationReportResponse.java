package com.rev.reporting_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaveUtilizationReportResponse {
    private Long userId;
    private String employeeName;
    private Object leaveBalance;
    private Map<String, Long> leavesByType;       // e.g., {"CASUAL": 3, "SICK": 1, "PAID": 2}
    private long totalLeavesUsed;
    private long totalLeavesAvailable;
    private double utilizationPercentage;
    private long generatedAt;
}
