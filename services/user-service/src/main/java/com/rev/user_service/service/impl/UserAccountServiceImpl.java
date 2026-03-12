package com.rev.user_service.service.impl;

import com.rev.user_service.dto.request.CreateUserRequest;
import com.rev.user_service.dto.request.UpdateUserRequest;
import com.rev.user_service.dto.response.UserResponse;
import com.rev.user_service.entity.User;
import com.rev.user_service.exception.BadRequestException;
import com.rev.user_service.exception.ResourceNotFoundException;
import com.rev.user_service.mapper.UserMapper;
import com.rev.user_service.repository.UserRepository;
import com.rev.user_service.security.PasswordEncoderUtil;
import com.rev.user_service.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class UserAccountServiceImpl implements UserAccountService {

    private final UserRepository userRepository;
    private final PasswordEncoderUtil passwordEncoder;
    private final com.rev.user_service.client.LeaveServiceClient leaveServiceClient;
    private final com.rev.user_service.client.EmployeeServiceClient employeeServiceClient;
    private final com.rev.user_service.client.ReportingServiceClient reportingServiceClient;

    private void logActivity(Long userId, String action, String details) {
        try {
            Map<String, Object> log = new HashMap<>();
            log.put("userId", userId);
            log.put("action", action);
            log.put("details", details);
            reportingServiceClient.logActivity(log);
        } catch (Exception e) {}
    }

    @Override
    public UserResponse createUser(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        generateEmployeeId(request);

        User user = UserMapper.toEntity(request);
        enrichUserWithExternalData(user);

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setActive(true);

        User savedUser = userRepository.save(user);
        logActivity(savedUser.getId(), "USER_CREATED", "User account created with role " + savedUser.getRole());
        
        assignDefaultLeaves(savedUser.getId());

        return UserMapper.toUserResponse(savedUser);
    }

    private void generateEmployeeId(CreateUserRequest request) {
        if (request.getEmployeeId() == null || request.getEmployeeId().isBlank()) {
            List<User> allUsers = userRepository.findAll();
            long maxNumeric = 0;
            for (User u : allUsers) {
                String eid = u.getEmployeeId();
                if (eid != null && eid.startsWith("EMP")) {
                    try {
                        long val = Long.parseLong(eid.substring(3));
                        if (val > maxNumeric) maxNumeric = val;
                    } catch (Exception e) {}
                }
            }
            request.setEmployeeId("EMP" + String.format("%03d", maxNumeric + 1));
        } else if (userRepository.existsByEmployeeId(request.getEmployeeId())) {
            throw new BadRequestException("Employee ID already exists");
        }
    }

    private void enrichUserWithExternalData(User user) {
        if (user.getDepartmentId() != null) {
            try {
                Map<String, Object> dept = employeeServiceClient.getDepartmentById(user.getDepartmentId());
                if (dept != null && dept.containsKey("name")) user.setDepartment(dept.get("name").toString());
            } catch (Exception e) {}
        }
        if (user.getDesignationId() != null) {
            try {
                Map<String, Object> desig = employeeServiceClient.getDesignationById(user.getDesignationId());
                if (desig != null && desig.containsKey("title")) user.setDesignation(desig.get("title").toString());
            } catch (Exception e) {}
        }
    }

    private void assignDefaultLeaves(Long userId) {
        // OCP: These should ideally come from a LeaveTypeService or Config
        // For now, we keep the logic but it's isolated in this specialized service
        long[] types = {1L, 2L, 3L};
        int[] quotas = {10, 15, 12};
        
        for (int i = 0; i < types.length; i++) {
            try {
                Map<String, Object> b = new HashMap<>();
                b.put("userId", userId);
                b.put("leaveTypeId", types[i]);
                b.put("totalQuota", quotas[i]);
                leaveServiceClient.assignBalance(b);
            } catch (Exception e) {}
        }
    }

    @Override
    public UserResponse updateUser(Long userId, UpdateUserRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        UserMapper.updateUserFromRequest(request, user);
        enrichUserWithExternalData(user);
        User updatedUser = userRepository.save(user);
        logActivity(updatedUser.getId(), "PROFILE_UPDATED", "User updated their profile details");
        return UserMapper.toUserResponse(updatedUser);
    }

    @Override
    public UserResponse getUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        enrichUserWithExternalData(user);
        User manager = user.getManagerId() != null ? userRepository.findById(user.getManagerId()).orElse(null) : null;
        return UserMapper.toUserResponseEnriched(user, manager);
    }

    @Override
    public void deactivateUser(Long userId) {
        userRepository.findById(userId).ifPresent(u -> {
            u.setActive(false);
            userRepository.save(u);
        });
    }

    @Override
    public void reactivateUser(Long userId) {
        userRepository.findById(userId).ifPresent(u -> {
            u.setActive(true);
            userRepository.save(u);
        });
    }

    @Override
    public void changePassword(Long userId, String currentPassword, String newPassword) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) throw new BadRequestException("Current password is incorrect");
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        logActivity(userId, "PASSWORD_CHANGED", "User changed their password");
    }

    @Override
    public UserResponse getMyProfile(Long userId) { return getUserById(userId); }

    @Override
    public UserResponse updateMyProfile(Long userId, UpdateUserRequest request) { return updateUser(userId, request); }
}
