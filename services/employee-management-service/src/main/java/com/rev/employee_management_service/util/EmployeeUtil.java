package com.rev.employee_management_service.util;

import com.rev.employee_management_service.entity.Employee;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

/**
 * Utility class for common employee-related operations.
 */
public class EmployeeUtil {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    private EmployeeUtil() {
        // Private constructor to prevent instantiation
    }

    /**
     * Combines first and last name into a single formatted full name string.
     *
     * @param firstName
     * @param lastName
     * @return Formatted full name
     */
    public static String getFullName(String firstName, String lastName) {
        if (!StringUtils.hasText(firstName)) {
            return StringUtils.hasText(lastName) ? lastName.trim() : "";
        }
        if (!StringUtils.hasText(lastName)) {
            return firstName.trim();
        }
        return (firstName.trim() + " " + lastName.trim()).trim();
    }

    /**
     * Combines first and last name from an Employee entity into a single formatted full name.
     *
     * @param employee
     * @return Formatted full name
     */
    public static String getFullName(Employee employee) {
        if (employee == null) {
            return "";
        }
        return getFullName(employee.getFirstName(), employee.getLastName());
    }

    /**
     * Validates if an email address is in the correct format.
     *
     * @param email
     * @return true if valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        if (!StringUtils.hasText(email)) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Verifies if a name string is non-empty and has a reasonable length.
     *
     * @param name
     * @return true if valid, false otherwise
     */
    public static boolean isValidName(String name) {
        return StringUtils.hasText(name) && name.trim().length() >= 2;
    }

    /**
     * Sanitizes and capitalizes name strings for consistent data formatting.
     *
     * @param name
     * @return Formatted name
     */
    public static String formatName(String name) {
        if (!StringUtils.hasText(name)) {
            return name;
        }
        String trimmed = name.trim();
        if (trimmed.length() < 1) {
            return trimmed;
        }
        return trimmed.substring(0, 1).toUpperCase() + trimmed.substring(1).toLowerCase();
    }

    /**
     * Determines if a designation title implies a managerial or lead role.
     *
     * @param designationTitle
     * @return true if it is a managerial/lead role
     */
    public static boolean isManagerialRole(String designationTitle) {
        if (!StringUtils.hasText(designationTitle)) {
            return false;
        }
        String lowerTitle = designationTitle.toLowerCase();
        return lowerTitle.contains("manager") || 
               lowerTitle.contains("lead") || 
               lowerTitle.contains("director") || 
               lowerTitle.contains("head") ||
               lowerTitle.contains("supervisor");
    }

    /**
     * Provides a fallback value if a string is null or empty.
     *
     * @param value
     * @param defaultValue
     * @return Normalized value or default
     */
    public static String normalize(String value, String defaultValue) {
        return StringUtils.hasText(value) ? value.trim() : defaultValue;
    }
}

