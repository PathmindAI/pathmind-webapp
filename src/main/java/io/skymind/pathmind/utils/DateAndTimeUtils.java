package io.skymind.pathmind.utils;

import java.time.format.DateTimeFormatter;

public class DateAndTimeUtils
{
	public static final DateTimeFormatter STANDARD_DATE_TIME_FOMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");

	/**
	 * We could use org.apache.commons.lang3.time.DurationFormatUtils but it seems overkill for what we need and I didn't want to spend the time
	 * to figure out how to conditionally printout the hours, minutes, and seconds.
	 */
	public static final String formatTime(long totalSeconds)
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
