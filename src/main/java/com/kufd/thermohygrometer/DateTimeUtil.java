package com.kufd.thermohygrometer;

import java.time.*;

public final class DateTimeUtil {
    //return current time in UTC
    public static LocalDateTime now() {
        return LocalDateTime.now(ZoneId.of("UTC"));
    }

    public static LocalDateTime getLocalDateTimeFromMillis(long epochMilli) {
        return LocalDateTime.ofInstant(
            Instant.ofEpochMilli(epochMilli),
            ZoneId.of("UTC")
        );
    }

    public static LocalDateTime convertTimezone(LocalDateTime date, String from, String to) {
        return date.atZone(ZoneId.of(from)).withZoneSameInstant(ZoneId.of(to)).toLocalDateTime();
    }

    public static LocalDateTime convertToUtc(LocalDateTime date, String timezone) {
        return convertTimezone(date, timezone, "UTC");
    }

    public static LocalDateTime convertFromUtc(LocalDateTime date, String timezone) {
        return convertTimezone(date, "UTC", timezone);
    }
}