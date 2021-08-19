package io.skymind.pathmind.webapp.ui.utils;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class GuiUtils {
    private GuiUtils() {
    }

    public static Hr getFullWidthHr() {
        Hr hr = new Hr();
        hr.setWidthFull();
        return hr;
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

    public static Button getPrimaryButton(String text, ComponentEventListener<ClickEvent<Button>> clickListener) {
        return getPrimaryButton(text, null, clickListener);
    }

    public static Button getPrimaryButton(String text, ComponentEventListener<ClickEvent<Button>> clickListener, boolean isVisible) {
        return getPrimaryButton(text, null, clickListener, isVisible);
    }

    public static Button getPrimaryButton(String text, Component icon, ComponentEventListener<ClickEvent<Button>> clickListener) {
        return getPrimaryButton(text, icon, clickListener, true);
    }

    public static Button getPrimaryButton(String text, Component icon, ComponentEventListener<ClickEvent<Button>> clickListener, boolean isVisible) {
        Button button = icon == null ? new Button(text, clickListener) : new Button(text, icon, clickListener);
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        button.setVisible(isVisible);
        return button;
    }

    public static Button getIconButton(Component icon, ComponentEventListener<ClickEvent<Button>> clickListener) {
        Button button = new Button(icon, clickListener);
        button.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        return button;
    }
}
