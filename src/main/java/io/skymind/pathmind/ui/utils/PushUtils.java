package io.skymind.pathmind.ui.utils;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.server.Command;

public class PushUtils
{
	public static void push(Component component, Command command) {
		component.getUI().ifPresent(ui ->
			ui.access(command));
	}
}
