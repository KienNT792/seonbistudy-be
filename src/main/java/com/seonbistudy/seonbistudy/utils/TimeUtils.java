package com.seonbistudy.seonbistudy.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
public final class TimeUtils {

    public static final ZoneId TIME_ZONE = ZoneId.of("Asia/Ho_Chi_Minh");

    private TimeUtils() {
        // Prevent instantiation
    }

    /**
     * Get current LocalDateTime in UTC+7.
     */
    public static LocalDateTime now() {
        return LocalDateTime.now(TIME_ZONE);
    }

    /**
     * Get current LocalDate (date only) in UTC+7.
     */
    public static LocalDate today() {
        return LocalDate.now(TIME_ZONE);
    }

    /**
     * Convert UTC time to UTC+7 (Asia/Ho_Chi_Minh).
     */
    public static LocalDateTime toVietnamTime(LocalDateTime utcTime) {
        if (utcTime == null) return null;
        return utcTime.atZone(ZoneOffset.UTC)
                .withZoneSameInstant(TIME_ZONE)
                .toLocalDateTime();
    }

    /**
     * Convert a LocalDateTime in UTC+7 to UTC time.
     */
    public static LocalDateTime toUtc(LocalDateTime vietnamTime) {
        if (vietnamTime == null) return null;
        return vietnamTime.atZone(TIME_ZONE)
                .withZoneSameInstant(ZoneOffset.UTC)
                .toLocalDateTime();
    }

    /**
     * Calculate the number of days between two LocalDateTime values (using date only).
     */
    public static long daysBetween(LocalDateTime from, LocalDateTime to) {
        if (from == null || to == null) return 0;
        LocalDate fromDate = from.atZone(TIME_ZONE).toLocalDate();
        LocalDate toDate = to.atZone(TIME_ZONE).toLocalDate();
        return Duration.between(fromDate.atStartOfDay(), toDate.atStartOfDay()).toDays();
    }

    /**
     * Format LocalDateTime to readable string (dd/MM/yyyy HH:mm:ss)
     */
    public static String format(LocalDateTime dateTime) {
        if (dateTime == null) return null;
        return dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    }

    /**
     * Parse string (dd/MM/yyyy HH:mm:ss) to LocalDateTime in UTC+7.
     */
    public static LocalDateTime parse(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.isBlank()) return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return LocalDateTime.parse(dateTimeStr, formatter);
    }

    /**
     * Check if two LocalDateTime are in the same day (based on UTC+7).
     */
    public static boolean isSameDay(LocalDateTime a, LocalDateTime b) {
        if (a == null || b == null) return false;
        LocalDate da = a.atZone(TIME_ZONE).toLocalDate();
        LocalDate db = b.atZone(TIME_ZONE).toLocalDate();
        return da.isEqual(db);
    }
}
