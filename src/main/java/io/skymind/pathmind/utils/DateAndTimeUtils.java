package io.skymind.pathmind.utils;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.data.provider.DataProvider;

public class DateAndTimeUtils
{
	public static final DateTimeFormatter STANDARD_DATE_ONLY_FOMATTER = DateTimeFormatter.ofPattern("MMM d yyyy");
	public static final DateTimeFormatter STANDARD_DATE_AND_TIME_FOMATTER = DateTimeFormatter.ofPattern("MMM d yyyy H:mm");
	public static final DateTimeFormatter STANDARD_DATE_AND_TIME_SHORT_FOMATTER = DateTimeFormatter.ofPattern("MMM d H:mm");

	public static final String formatDateAndTimeShortFormatter(LocalDateTime localDateTime) {
		return formatDateAndTimeShortFormatter(localDateTime, getUserZone());
	}
	public static final String formatDateAndTimeShortFormatter(LocalDateTime localDateTime, ZoneId userZone) {
		if(localDateTime == null)
			return "-";
		ZonedDateTime serverDateTime = ZonedDateTime.of(localDateTime, ZoneId.systemDefault());
		ZonedDateTime userDateTime = serverDateTime.withZoneSameLocal(userZone);
		return STANDARD_DATE_AND_TIME_SHORT_FOMATTER.format(userDateTime);
	}
	
	private static final ZoneId getUserZone() {
		String zoneId = null;
		if (UI.getCurrent().getInternals().getExtendedClientDetails() != null) {
			zoneId = UI.getCurrent().getInternals().getExtendedClientDetails().getTimeZoneId();
		}
		if (zoneId == null) {
			return ZoneId.systemDefault();
		} else {
			return ZoneId.of(zoneId);
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
	
	public static void refreshAfterRetrivingTimezone(UI ui, DataProvider<?, ?> dataProvider) {
		if (ui.getInternals().getExtendedClientDetails() == null) {
			ui.getPage().retrieveExtendedClientDetails(details -> {
				dataProvider.refreshAll();
			});
		}
	}
}
