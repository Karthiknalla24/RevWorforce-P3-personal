package com.rev.performance_service.util;

public class PerformanceUtil {
    public static String calculateRatingLabel(int rating) {
        if (rating >= 5) return "Excellent";
        if (rating >= 4) return "Good";
        if (rating >= 3) return "Average";
        return "Needs Improvement";
    }
}
