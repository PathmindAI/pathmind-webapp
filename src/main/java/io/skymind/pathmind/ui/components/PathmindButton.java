package io.skymind.pathmind.ui.components;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;

public class PathmindButton extends Button {

	public PathmindButton() {
	}

	public PathmindButton(String text) {
		super(text);
	}

	public PathmindButton(Component icon) {
		super(icon);
	}

	public PathmindButton(String text, Component icon) {
		super(text, icon);
	}

	public PathmindButton(String text, ComponentEventListener<ClickEvent<Button>> clickListener) {
		super(text, clickListener);
	}

	public PathmindButton(Component icon, ComponentEventListener<ClickEvent<Button>> clickListener) {
		super(icon, clickListener);
	}

	public PathmindButton(String text, Component icon, ComponentEventListener<ClickEvent<Button>> clickListener) {
		super(text, icon, clickListener);
	}

	public String getTitle() {
		return getElement().getAttribute("title");
	}

	public void setTitle(String title) {
		getElement().setAttribute("title", title);
	}
}
