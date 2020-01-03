package io.skymind.pathmind.ui.views.errors;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.ErrorEvent;
import com.vaadin.flow.server.ErrorHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PathmindErrorHandler implements ErrorHandler {

	@Override
	public void error(ErrorEvent event) {
		log.error(event.getThrowable().getMessage(), event.getThrowable());
		UI.getCurrent().navigate(ErrorView.class);
	}

}
