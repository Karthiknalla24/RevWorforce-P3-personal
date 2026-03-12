# Notification Service

## Overview
The Notification Service provides a centralized hub for system-generated alerts and messages, ensuring employees stay informed about important events.

## Core Features & Method Implementation

### 1. Alert Generation
*   `createNotification(userId, message, type)`: Generates a persistence record for a specific user. Supports types like `LEAVE_APPLIED`, `PERFORMANCE_SUBMITTED`, etc.
*   `createNotificationForAll(message, type)`: System-wide broadcasts for maintenance or global announcements.

### 2. User Engagement
*   `getNotificationsByUserId(userId)`: Fetches unread and read alerts for the user's bell icon/inbox.
*   `markAsRead(id, userId)`: Updates individual notification status.
*   `markAllAsRead(userId)`: Bulk update for user inbox.

### 3. Counts & Housekeeping
*   `getUnreadCount(userId)`: Returns the current count of unread messages for real-time badge updates.
*   `clearNotifications(userId)`: Purges history for a clean inbox.

## Tech Stack
*   **AOP Logging**: `LoggingAspect` logs every notification dispatch and read-confirmation event.
