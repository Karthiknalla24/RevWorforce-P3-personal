package com.rev.reporting_service.service.impl;

import com.rev.reporting_service.client.*;
import com.rev.reporting_service.service.ReportService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import org.springframework.beans.factory.annotation.Qualifier;

@Service
public class ReportServiceImpl implements ReportService {

    private final UserClient userClient;
    private final LeaveClient leaveClient;
    private final PerformanceClient performanceClient;
    private final EmployeeClient employeeClient;
    private final Executor executor;

    public ReportServiceImpl(UserClient userClient, LeaveClient leaveClient, 
                             PerformanceClient performanceClient, EmployeeClient employeeClient,
                             @Qualifier("reportingAsyncExecutor") Executor executor) {
        this.userClient = userClient;
        this.leaveClient = leaveClient;
        this.performanceClient = performanceClient;
        this.employeeClient = employeeClient;
        this.executor = executor;
    }

    @Override
    public Map<String, Object> getDashboard() {
        CompletableFuture<Object> usersFuture = CompletableFuture.supplyAsync(userClient::getAllUsers, executor);
        CompletableFuture<Object> deptsFuture = CompletableFuture.supplyAsync(employeeClient::getAllDepartments, executor);
        CompletableFuture<Object> leaveTypesFuture = CompletableFuture.supplyAsync(leaveClient::getAllLeaveTypes, executor);

        return CompletableFuture.allOf(usersFuture, deptsFuture, leaveTypesFuture)
                .thenApply(v -> {
                    Map<String, Object> dashboard = new HashMap<>();
                    dashboard.put("totalUsers", usersFuture.join());
                    dashboard.put("totalDepartments", deptsFuture.join());
                    dashboard.put("leaveTypes", leaveTypesFuture.join());
                    dashboard.put("timestamp", System.currentTimeMillis());
                    return dashboard;
                }).join();
    }

    @Override
    public Map<String, Object> getLeaveReport(Long userId) {
        CompletableFuture<Object> userFuture = CompletableFuture.supplyAsync(() -> userClient.getUserById(userId), executor);
        CompletableFuture<Object> leavesFuture = CompletableFuture.supplyAsync(() -> leaveClient.getUserLeaves(userId), executor);
        CompletableFuture<Object> balanceFuture = CompletableFuture.supplyAsync(() -> leaveClient.getUserBalance(userId), executor);

        return CompletableFuture.allOf(userFuture, leavesFuture, balanceFuture)
                .thenApply(v -> {
                    Map<String, Object> report = new HashMap<>();
                    report.put("user", userFuture.join());
                    report.put("leaves", leavesFuture.join());
                    report.put("balance", balanceFuture.join());
                    return report;
                }).join();
    }

    @Override
    public Map<String, Object> getPerformanceReport(Long userId) {
        CompletableFuture<Object> userFuture = CompletableFuture.supplyAsync(() -> userClient.getUserById(userId), executor);
        CompletableFuture<Object> reviewsFuture = CompletableFuture.supplyAsync(() -> performanceClient.getUserReviews(userId), executor);
        CompletableFuture<Object> goalsFuture = CompletableFuture.supplyAsync(() -> performanceClient.getUserGoals(userId), executor);

        return CompletableFuture.allOf(userFuture, reviewsFuture, goalsFuture)
                .thenApply(v -> {
                    Map<String, Object> report = new HashMap<>();
                    report.put("user", userFuture.join());
                    report.put("reviews", reviewsFuture.join());
                    report.put("goals", goalsFuture.join());
                    return report;
                }).join();
    }

    @Override
    public Map<String, Object> getEmployeeReport(Long userId) {
        CompletableFuture<Object> userFuture = CompletableFuture.supplyAsync(() -> userClient.getUserById(userId), executor);
        CompletableFuture<Object> leavesFuture = CompletableFuture.supplyAsync(() -> leaveClient.getUserLeaves(userId), executor);
        CompletableFuture<Object> reviewsFuture = CompletableFuture.supplyAsync(() -> performanceClient.getUserReviews(userId), executor);

        return CompletableFuture.allOf(userFuture, leavesFuture, reviewsFuture)
                .thenApply(v -> {
                    Map<String, Object> report = new HashMap<>();
                    report.put("user", userFuture.join());
                    report.put("leaves", leavesFuture.join());
                    report.put("reviews", reviewsFuture.join());
                    return report;
                }).join();
    }

    @Override
    public Map<String, Object> getDepartmentReport(Long departmentId) {
        Map<String, Object> report = new HashMap<>();
        Object department = employeeClient.getDepartmentById(departmentId);
        
        report.put("department", department);
        report.put("timestamp", System.currentTimeMillis());
        
        return report;
    }
}
