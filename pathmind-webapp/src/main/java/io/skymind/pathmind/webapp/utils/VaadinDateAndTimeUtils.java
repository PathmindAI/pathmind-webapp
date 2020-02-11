package io.skymind.pathmind.webapp.utils;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.function.SerializableConsumer;

/**
 * Date time utils which have a dependency to Vaadin classes.
 */
public class VaadinDateAndTimeUtils {

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
}
