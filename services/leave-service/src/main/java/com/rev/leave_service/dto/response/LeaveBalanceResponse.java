package com.rev.leave_service.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LeaveBalanceResponse {
    private Long id;
    private Long userId;
    private String employeeName;
    private String userName;            // Alias for frontend
    private Long leaveTypeId;
    private String leaveTypeName;       // Enriched name for frontend
    private int totalQuota;             // alias totalDays
    private int used;                   // alias usedDays
    private int remaining;              // alias remainingDays
}
