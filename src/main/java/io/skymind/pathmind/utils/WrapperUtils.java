package io.skymind.pathmind.utils;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;

public class WrapperUtils
{
	public static HorizontalLayout wrapCenterAlignmentHorizontal(Component... components) {
		HorizontalLayout wrapper = new HorizontalLayout(components);
		wrapper.setWidthFull();
		wrapper.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
		return wrapper;
	}

	public static HorizontalLayout wrapCenterAlignmentFullHorizontal(Component... components) {
		HorizontalLayout wrapper = wrapCenterAlignmentHorizontal(components);
		wrapper.setSizeFull();
		wrapper.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
		return wrapper;
	}

	public static HorizontalLayout wrapCenterAlignmentFullWidthHorizontal(Component... components) {
		HorizontalLayout wrapper = wrapCenterAlignmentHorizontal(components);
		wrapper.setWidthFull();
		wrapper.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
		return wrapper;
	}

	public static HorizontalLayout wrapFullWidthHorizontal(Component... components) {
		HorizontalLayout wrapper = wrapCenterAlignmentHorizontal(components);
		wrapper.setWidthFull();
		return wrapper;
	}

	public static VerticalLayout wrapCenterAlignmentFullVertical(Component... components) {
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setWidthFull();
		verticalLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
		verticalLayout.add(components);
		return verticalLayout;
	}

	public static VerticalLayout wrapCenteredFormVertical(Component... components) {
		VerticalLayout verticalLayout = new VerticalLayout(components);
		verticalLayout.setWidth(UIConstants.CENTERED_FORM_WIDTH);
		verticalLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
		return verticalLayout;
	}

	public static HorizontalLayout wrapCenteredFormHorizontal(Component... components) {
		HorizontalLayout horizontalLayout = new HorizontalLayout(components);
		horizontalLayout.setWidth(UIConstants.CENTERED_FORM_WIDTH);
		return horizontalLayout;
	}

	/**
	 * TODO -> The only way it seems to work is if you setup the style, the HorizontalLayout otherwise doesn't
	 * seem to want to respect the right alignment of the rightComponent:
	 * https://vaadin.com/forum/thread/17198105/button-alignment-in-horizontal-layout
	 */
	public static HorizontalLayout wrapLeftAndRightAligned(Component leftComponent, Component rightComponent)
	{
		HorizontalLayout horizontalLayout = new HorizontalLayout(leftComponent, rightComponent);
		horizontalLayout.setWidthFull();
		rightComponent.getElement().getStyle().set("margin-left", "auto");
		return horizontalLayout;
	}

	public static SplitLayout wrapCenterAlignmentFullSplitLayoutVertical(Component primaryComponent, Component secondaryComponent) {
		SplitLayout splitLayout = new SplitLayout(primaryComponent, secondaryComponent);
		splitLayout.setOrientation(SplitLayout.Orientation.VERTICAL);
		splitLayout.setSizeFull();
		splitLayout.setSplitterPosition(50);
		return splitLayout;
	}

	public static SplitLayout wrapCenterAlignmentFullSplitLayoutHorizontal(Component primaryComponent, Component secondaryComponent) {
		SplitLayout splitLayout = new SplitLayout(primaryComponent, secondaryComponent);
		splitLayout.setOrientation(SplitLayout.Orientation.HORIZONTAL);
		splitLayout.setSizeFull();
		splitLayout.setSplitterPosition(50);
		return splitLayout;
	}

}
