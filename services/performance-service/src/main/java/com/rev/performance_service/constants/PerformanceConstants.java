package com.rev.performance_service.constants;

public class PerformanceConstants {

    // Review Status Messages
    public static final String REVIEW_NOT_FOUND = "Performance review not found";
    public static final String GOAL_NOT_FOUND = "Goal not found";
    public static final String REVIEW_ALREADY_SUBMITTED = "Review has already been submitted";
    public static final String REVIEW_ALREADY_REVIEWED = "Review has already been reviewed by manager";

    // Validation Messages
    public static final String USER_ID_REQUIRED = "User ID is required";
    public static final String YEAR_REQUIRED = "Year is required";
    public static final String INVALID_RATING = "Rating must be between 1 and 5";
    public static final String TITLE_REQUIRED = "Goal title is required";
    public static final String PROGRESS_RANGE = "Progress must be between 0 and 100";

    // Notification Types
    public static final String NOTIFICATION_REVIEW_SUBMITTED = "REVIEW_SUBMITTED";
    public static final String NOTIFICATION_REVIEW_FEEDBACK = "REVIEW_FEEDBACK";
    public static final String NOTIFICATION_GOAL_CREATED = "GOAL_CREATED";
    public static final String NOTIFICATION_GOAL_COMMENT = "GOAL_COMMENT";
    public static final String NOTIFICATION_GOAL_UPDATED = "GOAL_UPDATED";

    // Activity Log Actions
    public static final String ACTION_REVIEW_CREATED = "REVIEW_CREATED";
    public static final String ACTION_REVIEW_SUBMITTED = "REVIEW_SUBMITTED";
    public static final String ACTION_REVIEW_FEEDBACK = "REVIEW_FEEDBACK_GIVEN";
    public static final String ACTION_GOAL_CREATED = "GOAL_CREATED";
    public static final String ACTION_GOAL_UPDATED = "GOAL_UPDATED";
    public static final String ACTION_GOAL_COMMENT = "GOAL_COMMENT_ADDED";
    public static final String ACTION_GOAL_DELETED = "GOAL_DELETED";

    // Rating Labels
    public static final int RATING_MIN = 1;
    public static final int RATING_MAX = 5;
    public static final int PROGRESS_MIN = 0;
    public static final int PROGRESS_MAX = 100;
}
