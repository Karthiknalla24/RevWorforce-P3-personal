package com.rev.leave_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaveReportResponse {
    private String name;
    private String department;
    private int totalLeaves;
    private int approvedLeaves;
    private int pendingLeaves;
    private int rejectedLeaves;
    private Map<String, Integer> leaveTypeStats;
}
