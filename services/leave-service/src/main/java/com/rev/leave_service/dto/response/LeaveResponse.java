package com.rev.leave_service.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class LeaveResponse {
    private Long id;
    private Long userId;
    private String employeeName;        // Added for frontend display
    private Long leaveTypeId;
    private String leaveType;           // The leave type name for frontend display
    private LocalDate startDate;
    private LocalDate endDate;
    private Long days;                  // Old version
    private Long numberOfDays;          // For frontend compatibility
    private String reason;
    private String status;
    private String managerComment;
    private Long managerId;
    private String createdAt;
    private String appliedDate;         // For frontend compatibility
}
