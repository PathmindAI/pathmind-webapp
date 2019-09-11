package io.skymind.pathmind.utils;

import java.time.format.DateTimeFormatter;

public class DateTimeUtils
{
	public static final DateTimeFormatter STANDARD_DATE_TIME_FOMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

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
