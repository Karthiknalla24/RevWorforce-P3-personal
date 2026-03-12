package com.rev.leave_service.util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class LeaveUtil {

    public static long calculateLeaveDuration(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) return 0;
        return ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }

    public static boolean isValidLeaveRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) return false;
        return !startDate.isAfter(endDate);
    }
    
    public static boolean isFutureDate(LocalDate date) {
        return date != null && date.isAfter(LocalDate.now());
    }
}
