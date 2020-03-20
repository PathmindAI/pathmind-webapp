package io.skymind.pathmind.webapp.ui.utils;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.Command;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class PushUtils
{
	// TODO -> https://github.com/SkymindIO/pathmind-webapp/issues/41 -> Trying different ways to resolve push
	public static void push(Component component, Command command) {
		push(component.getUI(), command);
	}

	// TODO -> https://github.com/SkymindIO/pathmind-webapp/issues/41 -> Trying different ways to resolve push
	public static void push(Optional<UI> optionalUI, Command command) {
		optionalUI.ifPresentOrElse(
				ui -> push(ui, command),
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