package com.rev.leave_service.service;

import com.rev.leave_service.entity.Holiday;
import com.rev.leave_service.entity.Leave;
import com.rev.leave_service.entity.LeaveBalance;
import com.rev.leave_service.entity.LeaveType;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface LeaveService {
    Leave applyLeave(Long userId, Long leaveTypeId, LocalDate startDate, LocalDate endDate, String reason);
    Leave approveLeave(Long leaveId, Long managerId, String comment);
    Leave rejectLeave(Long leaveId, Long managerId, String comment);
    void cancelLeave(Long leaveId, Long userId);
    List<Leave> getMyLeaves(Long userId);
    List<Leave> getTeamLeaves(Long managerId, String role);
    List<LeaveBalance> getMyBalance(Long userId);
    LeaveBalance assignLeaveBalance(Long userId, Long leaveTypeId, int totalDays);
    LeaveBalance adjustLeaveBalance(Long userId, Long leaveTypeId, int adjustment, String reason);
    LeaveType createLeaveType(String name, int defaultQuota);
    List<LeaveType> getAllLeaveTypes();
    void deleteLeaveType(Long id);
    Holiday createHoliday(LocalDate date, String name, String description);
    List<Holiday> getAllHolidays();
    void deleteHoliday(Long id);
    List<Leave> getAllLeaves();
    List<LeaveBalance> getAllLeaveBalances();
    List<Map<String, Object>> getDepartmentWiseReport(Long departmentId);
    List<Map<String, Object>> getEmployeeWiseReport(Long employeeId);
}
