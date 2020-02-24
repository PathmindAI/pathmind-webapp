package io.skymind.pathmind.utils;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.function.SerializableConsumer;

public class DateAndTimeUtils
{
	public static final DateTimeFormatter STANDARD_DATE_ONLY_FOMATTER = DateTimeFormatter.ofPattern("MMM d yyyy");
	public static final DateTimeFormatter STANDARD_DATE_AND_TIME_FOMATTER = DateTimeFormatter.ofPattern("MMM d yyyy H:mm");
	public static final DateTimeFormatter STANDARD_DATE_AND_TIME_SHORT_FOMATTER = DateTimeFormatter.ofPattern("MMM d H:mm");

	public static final String formatDateAndTimeShortFormatter(LocalDateTime localDateTime, String userZoneId) {
		if(localDateTime == null) {
			return "-";
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

	public static String formatETA(long totalSeconds)
	{
		if(totalSeconds == 0)
			return "0 sec";

		long hours = totalSeconds / 3600;
		long minutes = (totalSeconds % 3600) / 60;
		long seconds = totalSeconds % 60;

		if(hours > 10) 
			return "10+ hr";
		else if (hours > 0)
			return hours + " hr";
		else if (minutes > 0)
			return minutes + " min";
		else
			return seconds + " sec";
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
	 * Calls retrieveExtendedClientDetails to read the userTimeZone from client.
	 * This call is done in an async way, and the value is cached by Page, so for each UI, the call is done only once.
	 * <br>
	 * If userTimeZone is available in cache, executes the timeZoneConsumer immediately,
	 * otherwise, it's executed after userTimeZone is read from client side
	 */
	public static void withUserTimeZoneId(SerializableConsumer<String> timeZoneConsumer) {
		UI.getCurrent().getPage().retrieveExtendedClientDetails(details -> timeZoneConsumer.accept(details.getTimeZoneId()));
	}

	/**
	 * Gets the TimeZoneId from client browser
	 */
	public static String getUserTimeZoneId() {
		return UI.getCurrent().getInternals().getExtendedClientDetails().getTimeZoneId();
	}

	/**
	 * This is used in Stripe integration, since Stripe keeps Timestamps as epochSeconds
	 */
	public static LocalDateTime fromEpoch(long epoch) {
		return LocalDateTime.ofInstant(Instant.ofEpochSecond(epoch), ZoneId.systemDefault());
	}
}
