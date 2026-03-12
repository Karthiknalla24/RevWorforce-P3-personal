package com.rev.reporting_service.service;

import com.rev.reporting_service.client.UserClient;
import com.rev.reporting_service.dto.ActivityLogResponse;
import com.rev.reporting_service.entity.ActivityLog;
import com.rev.reporting_service.repository.ActivityLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ActivityLogServiceTest {

    @Mock
    private ActivityLogRepository repository;

    @Mock
    private UserClient userClient;

    @InjectMocks
    private ActivityLogService activityLogService;

    private ActivityLog log;

    @BeforeEach
    void setUp() {
        log = new ActivityLog();
        log.setId(1L);
        log.setUserId(10L);
        log.setAction("LOGIN");
        log.setDetails("User logged in");
    }

    @Test
    void logActivity_Success() {
        activityLogService.logActivity(10L, "LOGIN", "User logged in");
        verify(repository).save(any(ActivityLog.class));
    }

    @Test
    void getActivitiesByUser_Success() {
        Pageable pageable = PageRequest.of(0, 20);
        Page<ActivityLog> logPage = new PageImpl<>(Arrays.asList(log));
        when(repository.findByUserIdOrderByCreatedAtDesc(eq(10L), any(Pageable.class))).thenReturn(logPage);

        Page<ActivityLogResponse> result = activityLogService.getActivitiesByUser(10L, pageable);

        assertFalse(result.getContent().isEmpty());
        assertEquals("LOGIN", result.getContent().get(0).getAction());
    }

    @Test
    void getAllActivities_Success() {
        when(repository.findAll()).thenReturn(Arrays.asList(log));
        when(userClient.getEmployeeDirectory()).thenReturn(Arrays.asList());

        List<ActivityLogResponse> result = activityLogService.getAllActivities();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }
}
