package io.skymind.pathmind.ui.utils;

import com.vaadin.flow.component.notification.NotificationVariant;

import io.skymind.pathmind.ui.components.CloseableNotification;

public class NotificationUtils {

    public static void showNotification(String html, NotificationVariant variant) {
        CloseableNotification notification = new CloseableNotification(html);
        notification.addThemeVariants(variant);
        notification.open();
    }

    public static void showSuccess(String html) {
        CloseableNotification notification = new CloseableNotification(html);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification.setDuration(-1);
        notification.open();
    }

    public static void showError(String html) {
    	CloseableNotification notification = new CloseableNotification(html);
    	notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
    	notification.setDuration(-1);
    	notification.open();
    }

}
