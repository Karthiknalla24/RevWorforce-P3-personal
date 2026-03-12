package com.rev.notification_service.util;

public class NotificationUtil {
    public static String formatNotification(String message) {
        if (message == null) return "";
        return message.trim();
    }
}
