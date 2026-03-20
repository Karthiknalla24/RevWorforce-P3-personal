package com.rev.reporting_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "leave-service")
public interface LeaveClient {
    
    @GetMapping("/api/leaves/user/{userId}")
    Object getUserLeaves(@PathVariable("userId") Long userId);
    
    @GetMapping("/api/leaves/balance/{userId}")
    Object getUserBalance(@PathVariable("userId") Long userId);
    
    @GetMapping("/api/leaves/types")
    Object getAllLeaveTypes();
}
