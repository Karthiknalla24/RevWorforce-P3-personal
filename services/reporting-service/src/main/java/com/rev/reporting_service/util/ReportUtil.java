package com.rev.reporting_service.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReportUtil {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    public static String generateReportFileName(String reportType) {
        return reportType + "_" + LocalDateTime.now().format(FORMATTER) + ".json";
    }
}
