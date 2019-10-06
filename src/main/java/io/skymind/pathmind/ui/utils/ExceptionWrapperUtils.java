package io.skymind.pathmind.ui.utils;

import com.vaadin.flow.component.UI;
import io.skymind.pathmind.ui.views.errors.ErrorView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ExceptionWrapperUtils
{
	private static Logger log = LogManager.getLogger(ExceptionWrapperUtils.class);

	private ExceptionWrapperUtils() {
	}

	// TODO -> https://github.com/SkymindIO/pathmind-webapp/issues/175 -> Better handle exception errors
	public static void handleButtonClicked(Runnable runnable) {
		try {
			runnable.run();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			UI.getCurrent().getUI().ifPresent(
					ui -> ui.navigate(ErrorView.class));
		}
	}
}
