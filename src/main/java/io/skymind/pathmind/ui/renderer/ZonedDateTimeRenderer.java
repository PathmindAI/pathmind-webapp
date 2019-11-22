package io.skymind.pathmind.ui.renderer;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.data.renderer.BasicRenderer;
import com.vaadin.flow.function.ValueProvider;

/**
 * 
 * A template renderer for presenting {@code LocalDateTime} in users own timezone.
 *
 */
public class ZonedDateTimeRenderer<SOURCE> extends BasicRenderer<SOURCE, LocalDateTime> {

	private DateTimeFormatter formatter;
	private String nullRepresentation = "--";
	private String userTimeZoneId;
	
	public ZonedDateTimeRenderer(ValueProvider<SOURCE, LocalDateTime> valueProvider, DateTimeFormatter formatter) {
		super(valueProvider);
		this.formatter = formatter;
		UI.getCurrent().getPage().retrieveExtendedClientDetails(details -> {
			this.userTimeZoneId = details.getTimeZoneId();
		});
	}

	@Override
    protected String getFormattedValue(LocalDateTime dateTime) {
        return dateTime == null ? nullRepresentation
                : formatDateTimeInUserTimezone(dateTime);
    }

	private String formatDateTimeInUserTimezone(LocalDateTime dateTime) {
		ZonedDateTime zonedDateTime = ZonedDateTime.of(dateTime, ZoneId.systemDefault());
		if (userTimeZoneId != null) {
			zonedDateTime = zonedDateTime.withZoneSameInstant(ZoneId.of(userTimeZoneId));
		}
		return formatter.format(zonedDateTime);
	}
}
