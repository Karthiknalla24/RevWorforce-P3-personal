package com.rev.user_service.service.impl;

import com.rev.user_service.dto.response.EmployeeDirectoryResponse;
import com.rev.user_service.dto.response.UserResponse;
import com.rev.user_service.entity.User;
import com.rev.user_service.exception.ResourceNotFoundException;
import com.rev.user_service.mapper.UserMapper;
import com.rev.user_service.repository.UserRepository;
import com.rev.user_service.service.UserRelationshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserRelationshipServiceImpl implements UserRelationshipService {

    private final UserRepository userRepository;

    @Override
    public UserResponse getManager(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (user.getManagerId() == null) throw new ResourceNotFoundException("Manager not assigned");
        User manager = userRepository.findById(user.getManagerId()).orElseThrow(() -> new ResourceNotFoundException("Manager not found"));
        return UserMapper.toUserResponse(manager);
    }

    @Override
    public List<EmployeeDirectoryResponse> getTeamMembers(Long managerId) {
        List<User> users = userRepository.findByManagerId(managerId);
        Map<Long, String> userNames = fetchUserNames();
        return users.stream()
                .map(u -> UserMapper.toEmployeeDirectoryResponse(u, u.getManagerId() != null ? userNames.get(u.getManagerId()) : "None"))
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse assignManager(Long userId, Long managerId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (managerId != null) userRepository.findById(managerId).orElseThrow(() -> new ResourceNotFoundException("Manager not found"));
        user.setManagerId(managerId);
        User savedUser = userRepository.save(user);
        User manager = managerId != null ? userRepository.findById(managerId).orElse(null) : null;
        return UserMapper.toUserResponseEnriched(savedUser, manager);
    }

    @Override
    public List<EmployeeDirectoryResponse> getUsersByManager(Long managerId) { return getTeamMembers(managerId); }

    private Map<Long, String> fetchUserNames() {
        return userRepository.findAll().stream()
                .collect(Collectors.toMap(User::getId, u -> (u.getFirstName() != null ? u.getFirstName() : "") + " " + (u.getLastName() != null ? u.getLastName() : ""), (a, b) -> a));
    }
}
