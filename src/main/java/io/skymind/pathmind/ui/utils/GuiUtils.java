package io.skymind.pathmind.ui.utils;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class GuiUtils
{
	private GuiUtils() {
	}

	public static Hr getFullWidthHr() {
		Hr hr = new Hr();
		hr.setWidthFull();
		return hr;
	}

	public static Hr getHr(String width) {
		Hr hr = new Hr();
		hr.setWidth(width);
		return hr;
	}

	public static Div getHeightSpacer(String height) {
		Div div = new Div();
		div.setHeight(height);
		return div;
	}

	// TODO -> Transition to CSS
	public static Label getSubtitleLabel(String text) {
		Label label = new Label(text);
		// TODO -> Set a CSS style here instead.
		label.getStyle().set("font-size", "12px");
		return label;
	}

	public static Label getBoldLabel(String text) {
		Label label = new Label(text);
		label.getStyle().set("font-weight", "bold");
		return label;
	}

	// TODO -> Should point to a css style rather than style properties
	public static Label getLabel(String text, String fontSize, String fontWeight) {
		Label label = new Label(text);
		label.getStyle().set("font-size", fontSize);
		label.getStyle().set("font-weight", fontWeight);
		return label;
	}

	public static void removeMarginsPaddingAndSpacing(VerticalLayout layout) {
		layout.setPadding(false);
		layout.setMargin(false);
		layout.setSpacing(false);
	}

	public static void removeMarginsPaddingAndSpacing(HorizontalLayout layout) {
		layout.setPadding(false);
		layout.setMargin(false);
		layout.setSpacing(false);
	}
}
