package io.skymind.pathmind.webapp.ui.utils;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.server.Command;

import io.skymind.pathmind.webapp.ui.components.CloseableNotification;

public class NotificationUtils {

    public static void showNotification(String html, NotificationVariant variant) {
        CloseableNotification notification = new CloseableNotification(html);
        notification.addThemeVariants(variant);
        notification.open();
    }

    public static void showSuccess(String html) {
        CloseableNotification notification = new CloseableNotification(html);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification.open();
    }

    public static void showError(String html) {
        CloseableNotification notification = new CloseableNotification(html);
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        notification.setDuration(-1);
        notification.open();
    }

    public static void showPersistentNotification(String html, String buttonText, Command buttonListenerHandler) {
        Notification notification = new Notification();
        Span contentLabel = new Span();
        contentLabel.getElement().setProperty("innerHTML", html);
        contentLabel.setMaxWidth("350px");
        contentLabel.getStyle().set("display", "inline-block");

        Button button = new Button(buttonText);
        button.getStyle().set("min-width", "fit-content");
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        button.addClickListener(e -> buttonListenerHandler.execute());

        notification.add(WrapperUtils.wrapSizeFullCenterHorizontal(contentLabel, button));
        notification.setPosition(Notification.Position.TOP_CENTER);
        notification.setDuration(-1);
        notification.open();
    }
}
