package io.skymind.pathmind.webapp.ui.utils;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;

public class WrapperUtils {
    public static VerticalLayout wrapVerticalWithNoPaddingOrSpacing(Component... components) {
        VerticalLayout wrapper = new VerticalLayout(components);
        wrapper.setPadding(false);
        wrapper.setSpacing(false);
        return wrapper;
    }
    public static VerticalLayout wrapVerticalWithNoPadding(Component... components) {
        VerticalLayout wrapper = new VerticalLayout(components);
        wrapper.setPadding(false);
        wrapper.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.STRETCH);
        return wrapper;
    }

    public static VerticalLayout wrapVerticalWithNoPaddingOrSpacingAndWidthAuto(Component... components) {
        VerticalLayout wrapper = new VerticalLayout(components);
        wrapper.setPadding(false);
        wrapper.setSpacing(false);
        wrapper.setWidth(null);
        return wrapper;
    }

    public static VerticalLayout wrapWidthFullCenterVertical(Component... components) {
        VerticalLayout verticalLayout = new VerticalLayout(components);
        verticalLayout.setWidthFull();
        verticalLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        return verticalLayout;
    }

    public static HorizontalLayout wrapWidthFullHorizontal(Component... components) {
        HorizontalLayout wrapper = new HorizontalLayout(components);
        wrapper.setWidthFull();
        return wrapper;
    }

    public static HorizontalLayout wrapWidthFullHorizontalNoSpacingAlignCenter(Component... components) {
        HorizontalLayout wrapper = wrapWidthFullHorizontal(components);
        wrapper.setSpacing(false);
        wrapper.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        return wrapper;
    }

    public static HorizontalLayout wrapSizeFullHorizontal(Component... components) {
        HorizontalLayout wrapper = new HorizontalLayout(components);
        wrapper.setSizeFull();
        return wrapper;
    }

    public static HorizontalLayout wrapSizeFullBetweenHorizontal(Component... components) {
        HorizontalLayout wrapper = wrapSizeFullHorizontal(components);
        wrapper.setSpacing(false);
        wrapper.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        return wrapper;
    }

    public static HorizontalLayout wrapWidthFullBetweenHorizontal(Component... components) {
        HorizontalLayout wrapper = wrapWidthFullHorizontal(components);
        wrapper.setSpacing(false);
        wrapper.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        return wrapper;
    }

    public static HorizontalLayout wrapWidthFullCenterHorizontal(Component... components) {
        HorizontalLayout wrapper = wrapWidthFullHorizontal(components);
        wrapper.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        return wrapper;
    }

    public static HorizontalLayout wrapSizeFullCenterHorizontal(Component... components) {
        HorizontalLayout wrapper = new HorizontalLayout(components);
        wrapper.setSizeFull();
        wrapper.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        wrapper.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        return wrapper;
    }

    public static HorizontalLayout wrapWidthFullRightHorizontal(Component... components) {
        HorizontalLayout horizontalLayout = wrapWidthFullHorizontal(components);
        horizontalLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        return horizontalLayout;
    }

    public static SplitLayout wrapCenterAlignmentFullSplitLayoutHorizontal(Component primaryComponent, Component secondaryComponent) {
        return wrapCenterAlignmentFullSplitLayoutHorizontal(primaryComponent, secondaryComponent, 50);
    }

    public static SplitLayout wrapCenterAlignmentFullSplitLayoutHorizontal(Component primaryComponent, Component secondaryComponent, double splitterPosition) {
        return wrapCenterAlignmentFullSplitLayout(primaryComponent, secondaryComponent, splitterPosition, SplitLayout.Orientation.HORIZONTAL);
    }

    public static SplitLayout wrapCenterAlignmentFullSplitLayoutVertical(Component primaryComponent, Component secondaryComponent, double splitterPosition) {
        return wrapCenterAlignmentFullSplitLayout(primaryComponent, secondaryComponent, splitterPosition, SplitLayout.Orientation.VERTICAL);
    }

    private static SplitLayout wrapCenterAlignmentFullSplitLayout(Component primaryComponent, Component secondaryComponent, double splitterPosition, SplitLayout.Orientation orientation) {
        SplitLayout splitLayout = new SplitLayout(primaryComponent, secondaryComponent);
        splitLayout.setOrientation(orientation);
        splitLayout.setSizeFull();
        splitLayout.setSplitterPosition(splitterPosition);
        return splitLayout;
    }
}
