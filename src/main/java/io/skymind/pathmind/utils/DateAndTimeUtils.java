package io.skymind.pathmind.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateAndTimeUtils
{
	public static final DateTimeFormatter STANDARD_DATE_ONLY_FOMATTER = DateTimeFormatter.ofPattern("MMMM dd yyyy");
	public static final DateTimeFormatter STANDARD_DATE_AND_TIME_FOMATTER = DateTimeFormatter.ofPattern("MMMM dd yyyy HH:mm a");
	public static final DateTimeFormatter STANDARD_DATE_AND_TIME_SHORT_FOMATTER = DateTimeFormatter.ofPattern("MMMM dd HH:mm a");

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
}
