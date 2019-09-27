package io.skymind.pathmind.ui.components.buttons;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;

public class BackButton extends Button
{
	public BackButton(String name, ComponentEventListener<ClickEvent<Button>> clickListener) {
		super(name, clickListener);
		addThemeVariants(ButtonVariant.LUMO_TERTIARY);
	}
}
