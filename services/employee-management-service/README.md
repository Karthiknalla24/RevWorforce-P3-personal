# Employee Management Service

## Overview
This service manages the structural data of the organization, including departments, job titles (designations), and internal company announcements.

## Core Features & Method Implementation

### 1. Department Management
Organizes the workforce into functional units.
*   `createDepartment(CreateDepartmentRequest)`: Stores new departments with name validation.
*   `deleteDepartment(Long id)`: includes a **business rule** check; prevents deletion if any employees are currently assigned to the department (via `user-service` check).

### 2. Designation Management
Manages job titles and roles.
*   `createDesignation(CreateDesignationRequest)`: Adds new job titles to the system.
*   `deleteDesignation(Long id)`: Restricted deletion based on active occupancy.

### 3. Announcement System
Broadcasts company-wide information.
*   `createAnnouncement(CreateAnnouncementRequest)`: Publishes new news items.
*   `getAllAnnouncements()`: Retrieves historical and current broadcasts for the employee dashboard.

### 4. Logging & Monitoring
*   **AOP Logging**: `LoggingAspect` monitors all department and designation modifications for audit trails.

## Tech Stack
*   **MapStruct**: Efficient mapping between Entities and DTOs.
*   **OpenFeign**: Cross-service communication for employee count verification.
