# Reporting Service

## Overview
The Reporting Service acts as the **Data Aggregator** for the entire platform. it collects activities and generates high-level insights into the workforce.

## Core Features & Method Implementation

### 1. Activity Logging (ActivityLogService)
Centralized audit trail for all significant system events across all microservices.
*   `logActivity(userId, action, details)`: Stores an event record.
*   `getAllActivities()`: Retrieves a global activity feed with full name enrichment via `user-service`.
*   `getActivitiesByUser(userId)`: Tailored feed for security audits on specific users.

### 2. Dashboard Aggregation (ReportService)
Aggregates real-time statistics by communicating with other services.
*   `getDashboard()`: Retrieves "At a Glance" stats including total users (User Service), departments (Employee Service), and active leave types (Leave Service).

### 3. Comprehensive Reports
*   `getLeaveReport(userId)`: Combines profile data with leave history and current balances.
*   `getPerformanceReport(userId)`: Consolidates user profile with review scores and goal progress.
*   `getEmployeeReport(userId)`: A 360-degree view of an employee's organizational status.

## Tech Stack
*   **OpenFeign**: Intensive use of Feign clients (`UserClient`, `LeaveClient`, `PerformanceClient`, `EmployeeClient`) to fetch data from business services.
