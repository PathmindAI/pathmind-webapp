package io.skymind.pathmind.ui.utils;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import io.skymind.pathmind.ui.utils.UIConstants;

// TODO -> Clean up this class and have consistent naming structure. For now I'm just adding helper methods
// until I get a fuller understanding of all what we need.
public class WrapperUtils
{
	public static VerticalLayout wrapFullWidthVertical(Component... components) {
		VerticalLayout wrapper = new VerticalLayout(components);
		wrapper.setWidthFull();
		return wrapper;
	}

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

	public static HorizontalLayout wrapCenterFullWidthHorizontal(Component... components) {
		HorizontalLayout wrapper = wrapCenterAlignmentHorizontal(components);
		wrapper.setWidthFull();
		return wrapper;
	}

	public static HorizontalLayout wrapFullWidthHorizontal(Component... components) {
		HorizontalLayout wrapper = new HorizontalLayout(components);
		wrapper.setWidthFull();
		return wrapper;
	}

	public static VerticalLayout wrapFullSizeVertical(Component... components) {
		VerticalLayout wrapper = new VerticalLayout(components);
		wrapper.setSizeFull();
		return wrapper;
	}

	public static VerticalLayout wrapCenterAlignmentFullVertical(Component... components) {
		VerticalLayout verticalLayout = wrapFullWidthVertical(components);
		verticalLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
		return verticalLayout;
	}

	public static VerticalLayout wrapCenteredFormVertical(Component... components) {
		VerticalLayout verticalLayout = new VerticalLayout(components);
		verticalLayout.setWidth(UIConstants.CENTERED_FORM_WIDTH);
		verticalLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
		return verticalLayout;
	}

	// TODO -> Move this to CSS.
	public static VerticalLayout wrapCenteredFormVerticalBordered(Component... components) {
		VerticalLayout verticalLayout = wrapCenteredFormVertical(components);
		verticalLayout.getStyle().set("border", "solid 1px #ccc");
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
		return wrapCenterAlignmentFullSplitLayoutVertical(primaryComponent, secondaryComponent, 50);
	}

	public static SplitLayout wrapCenterAlignmentFullSplitLayoutVertical(Component primaryComponent, Component secondaryComponent, double splitterPosition) {
		return wrapCenterAlignmentFullSplitLayout(primaryComponent, secondaryComponent, splitterPosition, SplitLayout.Orientation.VERTICAL);
	}

	public static SplitLayout wrapCenterAlignmentFullSplitLayoutHorizontal(Component primaryComponent, Component secondaryComponent) {
		return wrapCenterAlignmentFullSplitLayoutHorizontal(primaryComponent, secondaryComponent, 50);
	}

	public static SplitLayout wrapCenterAlignmentFullSplitLayoutHorizontal(Component primaryComponent, Component secondaryComponent, double splitterPosition) {
		return wrapCenterAlignmentFullSplitLayout(primaryComponent, secondaryComponent, splitterPosition, SplitLayout.Orientation.HORIZONTAL);
	}

	private static SplitLayout wrapCenterAlignmentFullSplitLayout(Component primaryComponent, Component secondaryComponent, double splitterPosition, SplitLayout.Orientation orientation) {
		SplitLayout splitLayout = new SplitLayout(primaryComponent, secondaryComponent);
		splitLayout.setOrientation(orientation);
		splitLayout.setSizeFull();
		splitLayout.setSplitterPosition(splitterPosition);
		return splitLayout;
	}

}
