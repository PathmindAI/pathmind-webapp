package io.skymind.pathmind.webapp.ui.utils;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;

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

    public static void showPersistentNotification(String html) {
        Notification notification = new Notification();
        notification.add(html);
		notification.setPosition(Notification.Position.TOP_CENTER);
    	notification.setDuration(-1);
        notification.open();
    }
}
