package com.rev.reporting_service.controller;

import com.rev.reporting_service.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboard() {
        return ResponseEntity.ok(reportService.getDashboard());
    }

    @GetMapping("/leave/{userId}")
    public ResponseEntity<Map<String, Object>> getLeaveReport(@PathVariable Long userId) {
        return ResponseEntity.ok(reportService.getLeaveReport(userId));
    }

    @GetMapping("/performance/{userId}")
    public ResponseEntity<Map<String, Object>> getPerformanceReport(@PathVariable Long userId) {
        return ResponseEntity.ok(reportService.getPerformanceReport(userId));
    }

    @GetMapping("/employee/{userId}")
    public ResponseEntity<Map<String, Object>> getEmployeeReport(@PathVariable Long userId) {
        return ResponseEntity.ok(reportService.getEmployeeReport(userId));
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<Map<String, Object>> getDepartmentReport(@PathVariable Long departmentId) {
        return ResponseEntity.ok(reportService.getDepartmentReport(departmentId));
    }
}
