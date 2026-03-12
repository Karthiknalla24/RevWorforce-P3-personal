package com.rev.reporting_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaveReportResponse {
    private Object user;
    private Object leaves;
    private Object balance;
    private long totalLeavesTaken;
    private long totalLeavesPending;
    private long totalLeavesApproved;
    private long totalLeavesRejected;
    private long generatedAt;
}
