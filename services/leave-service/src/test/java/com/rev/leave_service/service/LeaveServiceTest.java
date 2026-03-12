package com.rev.leave_service.service;

import com.rev.leave_service.client.NotificationServiceClient;
import com.rev.leave_service.client.ReportingServiceClient;
import com.rev.leave_service.client.UserServiceClient;
import com.rev.leave_service.entity.*;
import com.rev.leave_service.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LeaveServiceTest {

    @Mock
    private LeaveRepository leaveRepository;
    @Mock
    private LeaveBalanceRepository leaveBalanceRepository;
    @Mock
    private LeaveTypeRepository leaveTypeRepository;
    @Mock
    private HolidayRepository holidayRepository;
    @Mock
    private UserServiceClient userServiceClient;
    @Mock
    private NotificationServiceClient notificationServiceClient;
    @Mock
    private ReportingServiceClient reportingServiceClient;

    @InjectMocks
    private LeaveService leaveService;

    private Leave leave;
    private LeaveBalance balance;

    @BeforeEach
    void setUp() {
        leave = new Leave();
        leave.setId(1L);
        leave.setUserId(10L);
        leave.setLeaveTypeId(1L);
        leave.setStartDate(LocalDate.now());
        leave.setEndDate(LocalDate.now());
        leave.setStatus(LeaveStatus.PENDING);

        balance = new LeaveBalance();
        balance.setUserId(10L);
        balance.setLeaveTypeId(1L);
        balance.setTotalDays(10);
        balance.setUsedDays(0);
        balance.setRemainingDays(10);
    }

    @Test
    void applyLeave_Success() {
        when(leaveBalanceRepository.findByUserIdAndLeaveTypeId(10L, 1L)).thenReturn(Optional.of(balance));
        when(leaveRepository.save(any(Leave.class))).thenReturn(leave);

        Leave result = leaveService.applyLeave(10L, 1L, LocalDate.now(), LocalDate.now(), "Vacation");

        assertNotNull(result);
        verify(leaveRepository).save(any());
        verify(notificationServiceClient, atLeastOnce()).createNotification(any());
    }

    @Test
    void applyLeave_InsufficientBalance() {
        balance.setRemainingDays(0);
        when(leaveBalanceRepository.findByUserIdAndLeaveTypeId(10L, 1L)).thenReturn(Optional.of(balance));

        assertThrows(RuntimeException.class, () -> 
            leaveService.applyLeave(10L, 1L, LocalDate.now(), LocalDate.now(), "Vacation"));
    }

    @Test
    void approveLeave_Success() {
        when(leaveRepository.findById(1L)).thenReturn(Optional.of(leave));
        when(leaveBalanceRepository.findByUserIdAndLeaveTypeId(10L, 1L)).thenReturn(Optional.of(balance));
        when(leaveRepository.save(any(Leave.class))).thenReturn(leave);

        Leave result = leaveService.approveLeave(1L, 100L, "Approved");

        assertEquals(LeaveStatus.APPROVED, result.getStatus());
        verify(leaveBalanceRepository).save(balance);
    }

    @Test
    void rejectLeave_Success() {
        when(leaveRepository.findById(1L)).thenReturn(Optional.of(leave));
        when(leaveRepository.save(any(Leave.class))).thenReturn(leave);

        Leave result = leaveService.rejectLeave(1L, 100L, "Rejected");

        assertEquals(LeaveStatus.REJECTED, result.getStatus());
    }

    @Test
    void cancelLeave_Success() {
        when(leaveRepository.findById(1L)).thenReturn(Optional.of(leave));

        assertDoesNotThrow(() -> leaveService.cancelLeave(1L, 10L));
        assertEquals(LeaveStatus.CANCELLED, leave.getStatus());
    }

    @Test
    void getTeamLeaves_Success() {
        Map<String, Object> member = new HashMap<>();
        member.put("id", 10L);
        when(userServiceClient.getTeamMembers(100L)).thenReturn(List.of(member));
        when(leaveRepository.findAll()).thenReturn(List.of(leave));

        List<Leave> result = leaveService.getTeamLeaves(100L);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void createLeaveType_Success() {
        LeaveType type = new LeaveType();
        type.setName("Annual");
        when(leaveTypeRepository.existsByName("Annual")).thenReturn(false);
        when(leaveTypeRepository.save(any())).thenReturn(type);

        LeaveType result = leaveService.createLeaveType("Annual", 15);

        assertNotNull(result);
    }
}
