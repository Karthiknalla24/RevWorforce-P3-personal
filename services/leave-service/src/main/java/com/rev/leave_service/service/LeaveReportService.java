package com.rev.leave_service.service;

import java.util.List;
import java.util.Map;

public interface LeaveReportService {
    List<Map<String, Object>> getDepartmentWiseReport(Long departmentId);
    List<Map<String, Object>> getEmployeeWiseReport(Long employeeId);
}
