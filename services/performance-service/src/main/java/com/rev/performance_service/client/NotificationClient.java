package com.rev.performance_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "notification-service", contextId = "performanceNotificationClient")
public interface NotificationClient {
    
    @PostMapping("/api/notifications")
    void createNotification(@RequestBody Map<String, Object> request);
}
