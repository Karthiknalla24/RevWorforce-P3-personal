package com.rev.api_gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {

    @GetMapping("/fallback/user")
    public String userServiceFallback() {
        return "User Service is temporarily unavailable. Please try again later.";
    }

    @GetMapping("/fallback/employee")
    public String employeeServiceFallback() {
        return "Employee Management Service is temporarily unavailable.";
    }

    @GetMapping("/fallback/leave")
    public String leaveServiceFallback() {
        return "Leave Service is temporarily unavailable.";
    }

    @GetMapping("/fallback/performance")
    public String performanceServiceFallback() {
        return "Performance Service is temporarily unavailable.";
    }

    @GetMapping("/fallback/notification")
    public String notificationServiceFallback() {
        return "Notification Service is temporarily unavailable.";
    }

    @GetMapping("/fallback/report")
    public String reportingServiceFallback() {
        return "Reporting Service is temporarily unavailable.";
    }
}