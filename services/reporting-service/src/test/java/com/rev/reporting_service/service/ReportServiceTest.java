package com.rev.reporting_service.service;

import com.rev.reporting_service.client.EmployeeClient;
import com.rev.reporting_service.client.LeaveClient;
import com.rev.reporting_service.client.PerformanceClient;
import com.rev.reporting_service.client.UserClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReportServiceTest {

    @Mock
    private UserClient userClient;
    @Mock
    private LeaveClient leaveClient;
    @Mock
    private PerformanceClient performanceClient;
    @Mock
    private EmployeeClient employeeClient;

    @InjectMocks
    private ReportService reportService;

    @Test
    void getDashboard_Success() {
        when(userClient.getAllUsers()).thenReturn("UserCount");
        when(employeeClient.getAllDepartments()).thenReturn("DeptList");
        when(leaveClient.getAllLeaveTypes()).thenReturn("TypeList");

        Map<String, Object> result = reportService.getDashboard();

        assertNotNull(result);
        assertEquals("UserCount", result.get("totalUsers"));
        assertEquals("DeptList", result.get("totalDepartments"));
    }

    @Test
    void getLeaveReport_Success() {
        when(userClient.getUserById(1L)).thenReturn("UserObj");
        when(leaveClient.getUserLeaves(1L)).thenReturn("LeavesList");

        Map<String, Object> result = reportService.getLeaveReport(1L);

        assertNotNull(result);
        assertEquals("UserObj", result.get("user"));
    }
}
