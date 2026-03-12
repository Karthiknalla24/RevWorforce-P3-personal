package com.rev.reporting_service.service.impl;

import com.rev.reporting_service.dto.ActivityLogResponse;
import com.rev.reporting_service.entity.ActivityLog;
import com.rev.reporting_service.repository.ActivityLogRepository;
import com.rev.reporting_service.client.UserClient;
import com.rev.reporting_service.service.ActivityLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityLogServiceImpl implements ActivityLogService {

    private final ActivityLogRepository repository;
    private final UserClient userClient;

    @Override
    public Page<ActivityLogResponse> getPaginatedActivities(Pageable pageable) {
        Page<ActivityLog> logs = repository.findAllByOrderByCreatedAtDesc(pageable);
        Map<Long, Map<String, Object>> users = fetchUsers();

        return logs.map(log -> toResponse(log, users.get(log.getUserId())));
    }

    @Override
    public List<ActivityLogResponse> getAllActivities() {
        List<ActivityLog> logs = repository.findAll();
        Map<Long, Map<String, Object>> users = fetchUsers();

        return logs.stream()
                .map(log -> toResponse(log, users.get(log.getUserId())))
                .collect(Collectors.toList());
    }

    private Map<Long, Map<String, Object>> fetchUsers() {
        try {
            return userClient.getEmployeeDirectory().stream()
                .collect(Collectors.toMap(
                    u -> Long.valueOf(u.get("id").toString()),
                    u -> u,
                    (existing, replacement) -> existing
                ));
        } catch (Exception e) {
            return java.util.Collections.emptyMap();
        }
    }

    @Override
    public Page<ActivityLogResponse> getActivitiesByUser(Long userId, Pageable pageable) {
        return repository.findByUserIdOrderByCreatedAtDesc(userId, pageable)
                .map(this::toResponse);
    }

    @Override
    @Async("reportingAsyncExecutor")
    public void logActivity(Long userId, String action, String details) {
        ActivityLog log = new ActivityLog();
        log.setUserId(userId);
        log.setAction(action);
        log.setDetails(details);
        repository.save(log);
    }

    @Override
    @Transactional
    public void deleteLogsByDuration(String duration) {
        LocalDateTime threshold = LocalDateTime.now();
        switch (duration.toLowerCase()) {
            case "24h":
                threshold = threshold.minusHours(24);
                break;
            case "week":
                threshold = threshold.minusWeeks(1);
                break;
            case "month":
                threshold = threshold.minusMonths(1);
                break;
            default:
                return;
        }
        repository.deleteByCreatedAtBefore(threshold);
    }

    /**
     * Automatic log cleanup scheduled for every Sunday at 12:00 AM.
     * Removes logs older than 7 days to keep database size optimal.
     */
    @Scheduled(cron = "0 0 0 * * SUN")
    @Transactional
    public void scheduledLogCleanup() {
        LocalDateTime threshold = LocalDateTime.now().minusWeeks(1);
        repository.deleteByCreatedAtBefore(threshold);
        System.out.println("Scheduled Activity log cleanup completed at: " + LocalDateTime.now());
    }

    private ActivityLogResponse toResponse(ActivityLog log, Map<String, Object> user) {
        ActivityLogResponse response = new ActivityLogResponse();
        response.setId(log.getId());
        response.setUserId(log.getUserId());
        response.setAction(log.getAction());
        response.setDetails(log.getDetails());
        response.setCreatedAt(log.getCreatedAt());
        
        if (user != null) {
            response.setUserName(user.get("name") != null ? user.get("name").toString() : "Unknown");
            response.setUserRole(user.get("role") != null ? user.get("role").toString() : "Unknown");
        } else {
            response.setUserName("Unknown User (ID: " + log.getUserId() + ")");
            response.setUserRole("Unknown");
        }
        
        return response;
    }

    private ActivityLogResponse toResponse(ActivityLog log) {
        return toResponse(log, null);
    }
}
