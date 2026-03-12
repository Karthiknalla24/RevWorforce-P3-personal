package com.rev.leave_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "notification-service", contextId = "notificationServiceClient")
public interface NotificationServiceClient {
    
    @PostMapping("/api/notifications")
    void createNotification(@RequestBody Map<String, Object> request);
}
