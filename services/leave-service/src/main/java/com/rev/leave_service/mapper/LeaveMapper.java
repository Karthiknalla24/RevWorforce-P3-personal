package com.rev.leave_service.mapper;

import com.rev.leave_service.entity.Holiday;
import com.rev.leave_service.entity.Leave;
import com.rev.leave_service.entity.LeaveBalance;
import com.rev.leave_service.entity.LeaveType;
import com.rev.leave_service.dto.response.*;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class LeaveMapper {

    public LeaveResponse toLeaveResponse(Leave leave, Map<Long, String> leaveTypeNames, Map<Long, String> userNames) {
        LeaveResponse response = new LeaveResponse();
        response.setId(leave.getId());
        response.setUserId(leave.getUserId());
        response.setEmployeeName(userNames.getOrDefault(leave.getUserId(), "Unknown"));
        response.setLeaveTypeId(leave.getLeaveTypeId());
        response.setLeaveType(leaveTypeNames.getOrDefault(leave.getLeaveTypeId(), "Unknown"));
        response.setStartDate(leave.getStartDate());
        response.setEndDate(leave.getEndDate());
        if (leave.getStartDate() != null && leave.getEndDate() != null) {
            long d = java.time.temporal.ChronoUnit.DAYS.between(leave.getStartDate(), leave.getEndDate()) + 1;
            response.setDays(d);
            response.setNumberOfDays(d);
        }
        response.setReason(leave.getReason());
        response.setStatus(leave.getStatus() != null ? leave.getStatus().name() : "PENDING");
        response.setManagerComment(leave.getManagerComment());
        response.setManagerId(leave.getManagerId());
        if (leave.getCreatedAt() != null) {
            String formattedDate = leave.getCreatedAt().toString();
            response.setCreatedAt(formattedDate);
            response.setAppliedDate(formattedDate);
        }
        return response;
    }

    public LeaveBalanceResponse toBalanceResponse(LeaveBalance balance, Map<Long, LeaveType> leaveTypeMap, Map<Long, String> userNames) {
        LeaveBalanceResponse response = new LeaveBalanceResponse();
        response.setId(balance.getId());
        response.setUserId(balance.getUserId());
        String name = userNames.getOrDefault(balance.getUserId(), "Unknown");
        response.setEmployeeName(name);
        response.setUserName(name);
        response.setLeaveTypeId(balance.getLeaveTypeId());
        LeaveType type = leaveTypeMap.get(balance.getLeaveTypeId());
        response.setLeaveTypeName(type != null ? type.getName() : "Unknown");
        response.setTotalQuota(balance.getTotalDays());
        response.setUsed(balance.getUsedDays());
        response.setRemaining(balance.getRemainingDays());
        return response;
    }

    public HolidayResponse toHolidayResponse(Holiday holiday) {
        return HolidayResponse.builder()
                .id(holiday.getId())
                .holidayDate(holiday.getHolidayDate())
                .name(holiday.getName())
                .description(holiday.getDescription())
                .build();
    }

    public LeaveReportResponse toReportResponse(Map<String, Object> stats) {
        return LeaveReportResponse.builder()
                .name(stats.getOrDefault("name", "").toString())
                .department(stats.getOrDefault("department", "").toString())
                .totalLeaves((int) stats.getOrDefault("totalLeaves", 0))
                .approvedLeaves((int) stats.getOrDefault("approvedLeaves", 0))
                .pendingLeaves((int) stats.getOrDefault("pendingLeaves", 0))
                .rejectedLeaves((int) stats.getOrDefault("rejectedLeaves", 0))
                .build();
    }
}
