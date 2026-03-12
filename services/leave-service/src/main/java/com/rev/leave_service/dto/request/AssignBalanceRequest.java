package com.rev.leave_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignBalanceRequest {
    private Long userId;
    private Long leaveTypeId;
    private Integer totalDays;      // for creation
    private Integer totalQuota;     // alias for creation
    private Integer adjustment;     // for adjustment
    private String reason;          // for adjustment
}
