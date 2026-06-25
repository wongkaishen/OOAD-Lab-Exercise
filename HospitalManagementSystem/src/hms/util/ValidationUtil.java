package hms.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Shared validation helpers for user input.
 */
public final class ValidationUtil {

    private static final DateTimeFormatter DATE_TIME_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private ValidationUtil() {
    }

    public static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static boolean isValidLogin(String username, String password) {
        return !isBlank(username) && !isBlank(password);
    }

    public static boolean isValidPatientInput(String name, int age, String gender) {
        return !isBlank(name) && age > 0 && !isBlank(gender);
    }

    public static boolean isValidDoctorInput(String name, String specialization) {
        return !isBlank(name) && !isBlank(specialization);
    }

    public static LocalDateTime parseDateTime(String value) throws DateTimeParseException {
        return LocalDateTime.parse(value.trim(), DATE_TIME_FORMAT);
    }

    public static String dateTimeFormatHint() {
        return "yyyy-MM-dd HH:mm";
    }
}
