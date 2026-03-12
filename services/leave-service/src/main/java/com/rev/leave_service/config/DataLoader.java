package com.rev.leave_service.config;

import com.rev.leave_service.entity.*;
import com.rev.leave_service.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final LeaveTypeRepository leaveTypeRepository;
    private final LeaveBalanceRepository leaveBalanceRepository;
    private final LeaveRepository leaveRepository;
    private final HolidayRepository holidayRepository;

    @Override
    public void run(String... args) throws Exception {
        
        // Leave Types
        if (leaveTypeRepository.count() == 0) {
            createLeaveType("Casual Leave", 10);
            createLeaveType("Sick Leave", 15);
            createLeaveType("Paid Leave", 12);
        }

        // Leave Balances for Karthik (1), Chaithanya (2), Vivek (3)
        if (leaveBalanceRepository.count() == 0) {
            List<Long> userIds = List.of(1L, 2L, 3L);
            List<LeaveType> types = leaveTypeRepository.findAll();
            
            for (Long userId : userIds) {
                for (LeaveType type : types) {
                    LeaveBalance balance = new LeaveBalance();
                    balance.setUserId(userId);
                    balance.setLeaveTypeId(type.getId());
                    balance.setTotalDays(type.getDefaultQuota());
                    balance.setUsedDays(0);
                    balance.setRemainingDays(type.getDefaultQuota());
                    leaveBalanceRepository.save(balance);
                }
            }
        }

        // Holidays
        if (holidayRepository.count() == 0) {
            createHoliday("New Year", LocalDate.of(2026, 1, 1));
            createHoliday("Republic Day", LocalDate.of(2026, 1, 26));
            createHoliday("Independence Day", LocalDate.of(2026, 8, 15));
            createHoliday("Gandhi Jayanti", LocalDate.of(2026, 10, 2));
            createHoliday("Christmas", LocalDate.of(2026, 12, 25));
        }

        // Leave Applications
        if (leaveRepository.count() == 0) {
            // Vivek Murugan(3) leaves
            createLeave(3L, 1L, LocalDate.now().minusDays(10), LocalDate.now().minusDays(8), "Family function", LeaveStatus.APPROVED, 2L);
            createLeave(3L, 2L, LocalDate.now().plusDays(5), LocalDate.now().plusDays(6), "Medical checkup", LeaveStatus.PENDING, 2L);

            // Chaithanya (2) leaves
            createLeave(2L, 3L, LocalDate.now().minusDays(5), LocalDate.now().minusDays(3), "Vacation", LeaveStatus.APPROVED, null);
            createLeave(2L, 1L, LocalDate.now().plusDays(10), LocalDate.now().plusDays(11), "Personal work", LeaveStatus.PENDING, null);

            // Karthik (1) leaves
            createLeave(1L, 2L, LocalDate.now().minusDays(20), LocalDate.now().minusDays(19), "Fever", LeaveStatus.APPROVED, null);
            
            Leave rejected = new Leave();
            rejected.setUserId(1L);
            rejected.setLeaveTypeId(1L);
            rejected.setStartDate(LocalDate.now().plusDays(3));
            rejected.setEndDate(LocalDate.now().plusDays(4));
            rejected.setReason("Conference");
            rejected.setStatus(LeaveStatus.REJECTED);
            rejected.setManagerComment("Not approved due to critical meeting");
            leaveRepository.save(rejected);
        }
    }

    private void createLeaveType(String name, int quota) {
        LeaveType lt = new LeaveType();
        lt.setName(name);
        lt.setDefaultQuota(quota);
        leaveTypeRepository.save(lt);
    }

    private void createHoliday(String name, LocalDate date) {
        Holiday h = new Holiday();
        h.setName(name);
        h.setHolidayDate(date);
        holidayRepository.save(h);
    }

    private void createLeave(Long userId, Long typeId, LocalDate start, LocalDate end, String reason, LeaveStatus status, Long managerId) {
        Leave l = new Leave();
        l.setUserId(userId);
        l.setLeaveTypeId(typeId);
        l.setStartDate(start);
        l.setEndDate(end);
        l.setReason(reason);
        l.setStatus(status);
        l.setManagerId(managerId);
        leaveRepository.save(l);
    }
}
