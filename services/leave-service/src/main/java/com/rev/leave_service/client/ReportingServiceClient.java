package com.rev.leave_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Map;

@FeignClient(name = "reporting-service", contextId = "reportingServiceClient")
public interface ReportingServiceClient {

    @PostMapping("/api/activity")
    void logActivity(@RequestBody Map<String, Object> request);
}
