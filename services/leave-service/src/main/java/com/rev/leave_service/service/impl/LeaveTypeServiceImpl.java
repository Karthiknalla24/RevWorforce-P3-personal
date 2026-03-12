package com.rev.leave_service.service.impl;

import com.rev.leave_service.entity.LeaveType;
import com.rev.leave_service.repository.LeaveTypeRepository;
import com.rev.leave_service.service.LeaveTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LeaveTypeServiceImpl implements LeaveTypeService {

    private final LeaveTypeRepository leaveTypeRepository;

    @Override
    public LeaveType createLeaveType(String name, int defaultQuota) {
        if (leaveTypeRepository.existsByName(name)) {
            throw new RuntimeException("Leave type already exists");
        }
        
        LeaveType leaveType = new LeaveType();
        leaveType.setName(name);
        leaveType.setDefaultQuota(defaultQuota);
        return leaveTypeRepository.save(leaveType);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LeaveType> getAllLeaveTypes() {
        return leaveTypeRepository.findAll();
    }

    @Override
    public void deleteLeaveType(Long id) {
        leaveTypeRepository.deleteById(id);
    }
}
