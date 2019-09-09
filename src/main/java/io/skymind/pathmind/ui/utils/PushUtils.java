package io.skymind.pathmind.ui.utils;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PushUtils
{
	private static Logger log = LogManager.getLogger(PushUtils.class);

	public static void push(Component component, Command command) {
		component.getUI().ifPresentOrElse(
				ui -> ui.access(command),
				() -> log.error("-------> PUSH FAILED"));
	}

	// TODO -> Testing to see the issue may be component.getUI() that fails and instead we need to pass in the UI directly.
	public static void push(UI ui, Command command) {
		ui.access(command);
	}
}
