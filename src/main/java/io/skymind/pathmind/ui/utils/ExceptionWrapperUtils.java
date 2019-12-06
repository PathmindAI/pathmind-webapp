package io.skymind.pathmind.ui.utils;

import com.vaadin.flow.component.UI;
import io.skymind.pathmind.ui.views.errors.ErrorView;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExceptionWrapperUtils
{
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
