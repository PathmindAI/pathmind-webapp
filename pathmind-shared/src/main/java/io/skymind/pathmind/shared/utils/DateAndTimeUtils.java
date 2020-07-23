package io.skymind.pathmind.shared.utils;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateAndTimeUtils {
    public static final DateTimeFormatter STANDARD_DATE_ONLY_FOMATTER = DateTimeFormatter.ofPattern("MMM d, yyyy");
    public static final DateTimeFormatter STANDARD_DATE_AND_TIME_FOMATTER = DateTimeFormatter.ofPattern("MMM d, yyyy h:mm a");
    public static final DateTimeFormatter STANDARD_DATE_AND_TIME_SHORT_FOMATTER = DateTimeFormatter.ofPattern("MMM d h:mm a");

    public static final String formatDateAndTimeShortFormatter(LocalDateTime localDateTime, String userZoneId) {
        if (localDateTime == null) {
            return "—";
        }
        if (userZoneId == null) {
            return STANDARD_DATE_AND_TIME_SHORT_FOMATTER.format(localDateTime);
        } else {
            ZonedDateTime serverDateTime = ZonedDateTime.of(localDateTime, ZoneId.systemDefault());
            ZonedDateTime userDateTime = serverDateTime.withZoneSameInstant(ZoneId.of(userZoneId));
            return STANDARD_DATE_AND_TIME_SHORT_FOMATTER.format(userDateTime);
        }
    }

    /**
     * We could use org.apache.commons.lang3.time.DurationFormatUtils but it seems overkill for what we need and I didn't want to spend the time
     * to figure out how to conditionally printout the hours, minutes, and seconds.
     */
    public static final String formatDurationTime(long totalSeconds) {
        if (totalSeconds == 0)
            return "0 sec";

        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;

        return (hours > 0 ? (hours + " hr ") : "") +
                (minutes > 0 ? (minutes + " min ") : "") +
                (seconds > 0 ? (seconds + " sec") : "");
    }

    public static String formatETA(long totalSeconds) {
        long hours = Math.round(totalSeconds / 3600d);
        long minutes = Math.round((totalSeconds % 3600) / 60d);

        if (hours > 12)
            return "12+ hr";
        else if (hours > 0)
            return hours + " hr";
        else if (minutes > 0)
            return minutes + " min";
        else
            return "less than a minute";
    }

    public static NumberFormat getElapsedTimeNumberFormat() {
        return new NumberFormat() {
            @Override
            public StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition pos) {
                throw new RuntimeException("Invalid use of Elapsed Time Formatter");
            }

            @Override
            public StringBuffer format(long number, StringBuffer toAppendTo, FieldPosition pos) {
                return new StringBuffer(formatDurationTime(number));
            }

            @Override
            public Number parse(String source, ParsePosition parsePosition) {
                return Long.parseLong(source);
            }
        };
    }

    /**
     * This is used in Stripe integration, since Stripe keeps Timestamps as epochSeconds
     */
    public static LocalDateTime fromEpoch(long epoch) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(epoch), ZoneId.systemDefault());
    }
}
