package io.skymind.pathmind.ui.utils;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PushUtils
{
	private static Logger log = LogManager.getLogger(PushUtils.class);

	// TODO -> https://github.com/SkymindIO/pathmind-webapp/issues/41 -> Trying different ways to resolve push
	public static void push(Component component, Command command) {
		component.getUI().ifPresentOrElse(
				ui -> ui.access(command),
				() -> log.error("-------> PUSH FAILED"));
	}

	// TODO -> https://github.com/SkymindIO/pathmind-webapp/issues/41 -> Trying different ways to resolve push
	public static void push(UI ui, Command command) {
		if(ui == null) {
			log.error("-------> PUSH FAILED");
			return;
		}
		ui.access(command);
	}
}
