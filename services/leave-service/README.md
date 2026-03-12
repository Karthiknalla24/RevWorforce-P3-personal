# Leave Service

## Overview
The Leave Service automates the leave application, tracking, and approval process for all employees.

## Core Features & Method Implementation

### 1. Leave Application & Lifecycle
*   `applyLeave(userId, leaveTypeId, startDate, endDate, reason)`: Validates leave balance, checks for holiday conflicts, and submits request in `PENDING` status.
*   `approveLeave(leaveId, managerId, comment)`: Transition to `APPROVED`, decrements the user's `LeaveBalance`, and notifies the employee.
*   `rejectLeave(leaveId, managerId, comment)`: Denies request and notifies employee.
*   `cancelLeave(leaveId, userId)`: Allows employees to rescind pending requests.

### 2. Balance Management
*   `assignLeaveBalance(userId, leaveTypeId, totalDays)`: Initializes or resets quotas for specific leave types.
*   `getMyBalance(userId)`: Calculates remaining days available for an employee.

### 3. Reporting & Visualization
*   `getDepartmentWiseReport(departmentId)`: Aggregates leave statistics for managers/HR.
*   `getEmployeeWiseReport(employeeId)`: Historical data for individual performance reviews.

### 4. Cross-Service Integration
*   **Notification Service**: Triggers alerts for managers when a new leave is applied and for employees when a status changes.
*   **User Service**: Fetches manager information and team list.

## Tech Stack
*   **Transactional Management**: Ensures balance updates and leave status changes occur atomically.
