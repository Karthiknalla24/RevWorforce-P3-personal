# Performance Service

## Overview
The Performance Service manages employee appraisals, annual reviews, and goal tracking to facilitate professional development.

## Core Features & Method Implementation

### 1. Performance Reviews
*   `createReview(userId, year, ...)`: Initializes a review cycle with deliverables and self-rating.
*   `submitReview(reviewId)`: Locks self-assessment and alerts the manager.
*   `provideFeedback(reviewId, feedback, managerRating)`: Finalizes the appraisal process with manager input.

### 2. Goal Tracking
*   `createGoal(userId, title, description)`: Sets professional objectives.
*   `updateGoalProgress(goalId, progress, status)`: Tracks milestone completion (0-100%).
*   `addGoalComment(goalId, comment)`: Facilitates manager coaching on specific goals.

### 3. Management Dashboards
*   `getMyReviews(userId)`: Personal history of appraisals.
*   `getTeamReviews(managerId)`: Consolidated view for managers to evaluate their subordinates.

### 4. Automated Notifications
*   Integrates with the **Notification Service** to remind managers of pending reviews or milestones.

## Tech Stack
*   **AOP Logging**: Tracks every feedback update for compliance and audit requirements.
