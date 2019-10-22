package io.skymind.pathmind.utils;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateAndTimeUtils
{
	public static final DateTimeFormatter STANDARD_DATE_ONLY_FOMATTER = DateTimeFormatter.ofPattern("MMM d yyyy");
	public static final DateTimeFormatter STANDARD_DATE_AND_TIME_FOMATTER = DateTimeFormatter.ofPattern("MMM d yyyy H:mm");
	public static final DateTimeFormatter STANDARD_DATE_AND_TIME_SHORT_FOMATTER = DateTimeFormatter.ofPattern("MMM d H:mm");

	public static final String formatDateAndTimeShortFormatter(LocalDateTime localDateTime) {
		if(localDateTime == null)
			return "--";
		return STANDARD_DATE_AND_TIME_SHORT_FOMATTER.format(localDateTime);
	}

	/**
	 * We could use org.apache.commons.lang3.time.DurationFormatUtils but it seems overkill for what we need and I didn't want to spend the time
	 * to figure out how to conditionally printout the hours, minutes, and seconds.
	 */
	public static final String formatDurationTime(long totalSeconds)
	{
		if(totalSeconds == 0)
			return "0 sec";

		long hours = totalSeconds / 3600;
		long minutes = (totalSeconds % 3600) / 60;
		long seconds = totalSeconds % 60;

		return  (hours > 0 ? (hours + " hr ") : "") +
				(minutes > 0 ? (minutes + " min ") : "") +
				(seconds > 0 ? (seconds + " sec") : "");
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
}
