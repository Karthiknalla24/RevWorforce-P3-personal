package com.rev.leave_service.service;

import com.rev.leave_service.entity.LeaveType;
import java.util.List;

public interface LeaveTypeService {
    LeaveType createLeaveType(String name, int defaultQuota);
    List<LeaveType> getAllLeaveTypes();
    void deleteLeaveType(Long id);
}
