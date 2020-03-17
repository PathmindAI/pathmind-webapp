package io.skymind.pathmind.webapp.ui.utils;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.components.SearchBox;

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
				LabelFactory.createLabel(title),
				searchBox);
	}

	public static FormLayout getTitleBarFullWidth(int componentCount) {
		FormLayout formLayout = new FormLayout();
		formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("100px", componentCount, FormLayout.ResponsiveStep.LabelsPosition.TOP));
		return formLayout;
	}
}
