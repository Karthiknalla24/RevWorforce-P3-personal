package com.rev.reporting_service.service;

import java.util.Map;

public interface ReportService {
    Map<String, Object> getDashboard();
    Map<String, Object> getLeaveReport(Long userId);
    Map<String, Object> getPerformanceReport(Long userId);
    Map<String, Object> getEmployeeReport(Long userId);
    Map<String, Object> getDepartmentReport(Long departmentId);
}
