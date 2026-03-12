package com.rev.reporting_service.service;

import com.rev.reporting_service.dto.ActivityLogResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Interface defining the contract for activity log management.
 * Follows the Dependency Inversion Principle — controllers depend on this abstraction.
 */
public interface ActivityLogService {

    Page<ActivityLogResponse> getPaginatedActivities(Pageable pageable);

    List<ActivityLogResponse> getAllActivities();

    Page<ActivityLogResponse> getActivitiesByUser(Long userId, Pageable pageable);

    void logActivity(Long userId, String action, String details);

    void deleteLogsByDuration(String duration);
}
