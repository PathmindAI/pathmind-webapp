package io.skymind.pathmind.ui.views.errors;

import java.time.LocalDateTime;
import java.util.Random;

import com.vaadin.flow.server.ErrorEvent;
import com.vaadin.flow.server.ErrorHandler;

import io.skymind.pathmind.ui.utils.NotificationUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PathmindErrorHandler implements ErrorHandler {

	@Override
	public void error(ErrorEvent event) {
		String errorId = generateUniqueErrorId();
		
		log.error(String.format("Error #%s: %s", errorId, event.getThrowable().getMessage()), event.getThrowable());
		String errorMessage = String.format("<b>An unexpected error occurred</b><br>Please contact Skymind for assistance.<br>#%s", errorId);
		
		NotificationUtils.showError(errorMessage);
	}
	
	/**
	 * Generates a unique error id of length 12 chars
	 * 6 random numbers - HHmmss
	 */
	public static String generateUniqueErrorId() {
		int start = 48; // number 0
		int end = 57; // number 9
		Random random = new Random();
		String generated = random.ints(start, end + 1)
				.limit(6)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
				.toString();
		return String.format("%s-%2$tH%2$tM%2$tS", generated , LocalDateTime.now());
	}

}
