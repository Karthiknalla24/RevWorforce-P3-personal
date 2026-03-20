package com.rev.reporting_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "performance-service")
public interface PerformanceClient {
    
    @GetMapping("/api/performance/reviews/user/{userId}")
    Object getUserReviews(@PathVariable("userId") Long userId);
    
    @GetMapping("/api/performance/goals/user/{userId}")
    Object getUserGoals(@PathVariable("userId") Long userId);
}
