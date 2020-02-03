package io.skymind.pathmind.ui.utils;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import io.skymind.pathmind.ui.components.SearchBox;

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

	// TODO -> CSS -> Transition to CSS
	public static Span getSubtitleLabel(String text) {
		Span label = new Span(text);
		label.getStyle().set("font-size", "12px");
		return label;
	}

	public static Span getBoldLabel(String text) {
		Span label = new Span(text);
		label.getStyle().set("font-weight", "bold");
		return label;
	}

	// TODO -> CSS -> Should point to a css style rather than style properties
	public static Span getLabel(String text, String fontSize, String fontWeight) {
		Span label = new Span(text);
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

	public static HorizontalLayout getTitleAndSearchBoxBar(String title, SearchBox searchBox) {
		return WrapperUtils.wrapLeftAndRightAligned(
				new Label(title),
				searchBox);
	}

	public static FormLayout getTitleBarFullWidth(int componentCount) {
		FormLayout formLayout = new FormLayout();
		formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("100px", componentCount, FormLayout.ResponsiveStep.LabelsPosition.TOP));
		return formLayout;
	}
}
