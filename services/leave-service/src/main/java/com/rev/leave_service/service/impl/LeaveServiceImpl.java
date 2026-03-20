package com.rev.leave_service.service.impl;

import com.rev.leave_service.client.NotificationServiceClient;
import com.rev.leave_service.client.UserServiceClient;
import com.rev.leave_service.entity.*;
import com.rev.leave_service.repository.*;
import com.rev.leave_service.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Main implementation of LeaveService.
 * Follows SRP by delegating specific responsibilities to specialized services:
 * - Holiday management -> HolidayService
 * - Leave Type management -> LeaveTypeService
 * - Reporting -> LeaveReportService
 */
@Service
@RequiredArgsConstructor
@Transactional
public class LeaveServiceImpl implements LeaveService {

    private final LeaveRepository leaveRepository;
    private final LeaveBalanceRepository leaveBalanceRepository;
    private final UserServiceClient userServiceClient;
    private final NotificationServiceClient notificationServiceClient;
    private final com.rev.leave_service.client.ReportingServiceClient reportingServiceClient;

    // Delegated Services
    private final HolidayService holidayService;
    private final LeaveTypeService leaveTypeService;
    private final LeaveReportService leaveReportService;

    private void logActivity(Long userId, String action, String details) {
        try {
            Map<String, Object> log = new HashMap<>();
            log.put("userId", userId);
            log.put("action", action);
            log.put("details", details);
            reportingServiceClient.logActivity(log);
        } catch (Exception e) {
            System.err.println("Failed to log activity for user " + userId + ": " + e.getMessage());
        }
    }

    @Override
    public Leave applyLeave(Long userId, Long leaveTypeId, LocalDate startDate, LocalDate endDate, String reason) {
        if (startDate.isBefore(LocalDate.now())) {
            throw new RuntimeException("Leave start date cannot be in the past.");
        }
        if (endDate.isBefore(startDate)) {
            throw new RuntimeException("End date cannot be before start date.");
        }

        List<Leave> overlapping = leaveRepository.findOverlappingLeaves(userId, startDate, endDate);
        if (!overlapping.isEmpty()) {
            throw new RuntimeException("Overlapping leave application exists.");
        }

        LeaveBalance balance = leaveBalanceRepository.findByUserIdAndLeaveTypeId(userId, leaveTypeId)
                .orElseThrow(() -> new RuntimeException("Leave balance not found"));
        
        long days = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        if (balance.getRemainingDays() < days) {
            throw new RuntimeException("Insufficient leave balance.");
        }

        Leave leave = new Leave();
        leave.setUserId(userId);
        leave.setLeaveTypeId(leaveTypeId);
        leave.setStartDate(startDate);
        leave.setEndDate(endDate);
        leave.setReason(reason);
        leave.setStatus(LeaveStatus.PENDING);
        
        Leave savedLeave = leaveRepository.save(leave);

        // Notifications
        notifyStakeholders(userId, startDate, endDate);
        
        logActivity(userId, "LEAVE_APPLIED", "Applied for leave type ID " + leaveTypeId + " from " + startDate + " to " + endDate);
        return savedLeave;
    }
    @Async
    public void notifyStakeholders(Long userId, LocalDate start, LocalDate end) {
        System.out.println("[LeaveService] Notifying stakeholders for user " + userId);
        try {
            // Fetch employee name
            Map<String, Object> employee = userServiceClient.getUserById(userId);
            String employeeName = (employee != null && employee.get("name") != null) ? 
                    employee.get("name").toString() : "Employee ID: " + userId;

            Map<String, Object> manager = userServiceClient.getManager(userId);
            if (manager != null && manager.containsKey("id")) {
                System.out.println("[LeaveService] Notifying Manager " + manager.get("id"));
                Map<String, Object> notification = new HashMap<>();
                notification.put("userId", Long.valueOf(manager.get("id").toString()));
                notification.put("message", employeeName + " has applied for leave from " + start + " to " + end);
                notification.put("type", "LEAVE_APPLIED");
                notificationServiceClient.createNotification(notification);
            }

            // ADMIN Oversight: Notify all Admins if a Manager is applying for leave
            String userRole = (employee != null && employee.get("role") != null) ? employee.get("role").toString() : "";
            System.out.println("[LeaveService] User role: " + userRole);
            if ("MANAGER".equalsIgnoreCase(userRole)) {
                System.out.println("[LeaveService] User is a Manager, notifying Admins...");
                List<Map<String, Object>> admins = userServiceClient.filterUsers(null, null, true, "ADMIN");
                System.out.println("[LeaveService] Found " + (admins != null ? admins.size() : 0) + " admins");
                if (admins != null) {
                    for (Map<String, Object> admin : admins) {
                        System.out.println("[LeaveService] Notifying Admin " + admin.get("id"));
                        Map<String, Object> adminNotification = new HashMap<>();
                        adminNotification.put("userId", Long.valueOf(admin.get("id").toString()));
                        adminNotification.put("message", "[Manager Leave Request] " + employeeName + " (Manager) has applied for leave from " + start + " to " + end);
                        adminNotification.put("type", "MANAGER_LEAVE_REQUEST");
                        notificationServiceClient.createNotification(adminNotification);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("[LeaveService] Failed to notify stakeholders: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void notifyEmployeeStatusUpdate(Long userId, String status, String comment) {
        try {
            Map<String, Object> notification = new HashMap<>();
            notification.put("userId", userId);
            notification.put("message", "Your leave request has been " + status + ". Manager comment: " + (comment != null ? comment : "No comments"));
            notification.put("type", "LEAVE_STATUS_UPDATE");
            notificationServiceClient.createNotification(notification);
        } catch (Exception e) {
            System.err.println("Failed to notify employee for leave status update: " + e.getMessage());
        }
    }

    @Override
    public Leave approveLeave(Long leaveId, Long managerId, String comment) {
        Leave leave = leaveRepository.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Leave not found"));
        
        leave.setStatus(LeaveStatus.APPROVED);
        leave.setManagerId(managerId);
        leave.setManagerComment(comment);
        
        LeaveBalance balance = leaveBalanceRepository.findByUserIdAndLeaveTypeId(leave.getUserId(), leave.getLeaveTypeId())
                .orElseThrow(() -> new RuntimeException("Leave balance not found"));
        
        long days = ChronoUnit.DAYS.between(leave.getStartDate(), leave.getEndDate()) + 1;
        balance.setUsedDays(balance.getUsedDays() + (int) days);
        balance.setRemainingDays(balance.getTotalDays() - balance.getUsedDays());
        leaveBalanceRepository.save(balance);

        notifyEmployeeStatusUpdate(leave.getUserId(), "APPROVED", comment);
        
        logActivity(leave.getUserId(), "LEAVE_APPROVED", "Leave request approved");
        return leaveRepository.save(leave);
    }

    @Override
    public Leave rejectLeave(Long leaveId, Long managerId, String comment) {
        Leave leave = leaveRepository.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Leave not found"));
        leave.setStatus(LeaveStatus.REJECTED);
        leave.setManagerId(managerId);
        leave.setManagerComment(comment);

        notifyEmployeeStatusUpdate(leave.getUserId(), "REJECTED", comment);

        logActivity(leave.getUserId(), "LEAVE_REJECTED", "Leave request rejected");
        return leaveRepository.save(leave);
    }

    @Override
    public void cancelLeave(Long leaveId, Long userId) {
        Leave leave = leaveRepository.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Leave not found"));
        if (!leave.getUserId().equals(userId)) throw new RuntimeException("Unauthorized");
        if (leave.getStatus() != LeaveStatus.PENDING) throw new RuntimeException("Can only cancel pending leaves");
        leave.setStatus(LeaveStatus.CANCELLED);
        leaveRepository.save(leave);
        logActivity(userId, "LEAVE_CANCELLED", "Cancelled leave request");
    }

    @Override
    public List<Leave> getMyLeaves(Long userId) {
        return leaveRepository.findByUserId(userId);
    }

    @Override
    public List<Leave> getTeamLeaves(Long managerId, String role) {
        List<Long> memberIds;
        if ("ADMIN".equalsIgnoreCase(role)) {
            // Admin sees all manager leaves
            List<Map<String, Object>> managers = userServiceClient.getAllManagers();
            memberIds = managers.stream()
                    .map(m -> Long.valueOf(m.get("id").toString()))
                    .collect(Collectors.toList());
        } else {
            List<Map<String, Object>> teamMembers = userServiceClient.getTeamMembers(managerId);
            memberIds = teamMembers.stream()
                    .map(m -> Long.valueOf(m.get("id").toString()))
                    .collect(Collectors.toList());
        }
        
        return leaveRepository.findAll().stream()
                .filter(l -> memberIds.contains(l.getUserId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<LeaveBalance> getMyBalance(Long userId) {
        return leaveBalanceRepository.findByUserId(userId);
    }

    @Override
    public LeaveBalance assignLeaveBalance(Long userId, Long leaveTypeId, int totalDays) {
        LeaveBalance balance = leaveBalanceRepository.findByUserIdAndLeaveTypeId(userId, leaveTypeId)
                .orElse(new LeaveBalance());
        balance.setUserId(userId);
        balance.setLeaveTypeId(leaveTypeId);
        balance.setTotalDays(totalDays);
        balance.setRemainingDays(totalDays - balance.getUsedDays());
        return leaveBalanceRepository.save(balance);
    }

    @Override
    public LeaveBalance adjustLeaveBalance(Long userId, Long leaveTypeId, int adjustment, String reason) {
        LeaveBalance balance = leaveBalanceRepository.findByUserIdAndLeaveTypeId(userId, leaveTypeId)
                .orElseThrow(() -> new RuntimeException("Leave balance not found"));
        balance.setTotalDays(balance.getTotalDays() + adjustment);
        balance.setRemainingDays(balance.getRemainingDays() + adjustment);
        return leaveBalanceRepository.save(balance);
    }

    // --- Delegated Methods ---

    @Override
    public LeaveType createLeaveType(String name, int defaultQuota) {
        return leaveTypeService.createLeaveType(name, defaultQuota);
    }

    @Override
    public List<LeaveType> getAllLeaveTypes() {
        return leaveTypeService.getAllLeaveTypes();
    }

    @Override
    public void deleteLeaveType(Long id) {
        leaveTypeService.deleteLeaveType(id);
    }

    @Override
    public Holiday createHoliday(LocalDate date, String name, String description) {
        return holidayService.createHoliday(date, name, description);
    }

    @Override
    public List<Holiday> getAllHolidays() {
        return holidayService.getAllHolidays();
    }

    @Override
    public void deleteHoliday(Long id) {
        holidayService.deleteHoliday(id);
    }

    @Override
    public List<Leave> getAllLeaves() {
        return leaveRepository.findAll();
    }

    @Override
    public List<LeaveBalance> getAllLeaveBalances() {
        return leaveBalanceRepository.findAll();
    }

    @Override
    public List<Map<String, Object>> getDepartmentWiseReport(Long departmentId) {
        return leaveReportService.getDepartmentWiseReport(departmentId);
    }

    @Override
    public List<Map<String, Object>> getEmployeeWiseReport(Long employeeId) {
        return leaveReportService.getEmployeeWiseReport(employeeId);
    }
}
