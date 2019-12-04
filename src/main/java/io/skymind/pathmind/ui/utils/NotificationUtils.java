package io.skymind.pathmind.ui.utils;

import com.vaadin.flow.component.notification.NotificationVariant;
import io.skymind.pathmind.ui.components.CloseableNotification;

public class NotificationUtils {

    public static void showNotification(String html, NotificationVariant variant) {
        CloseableNotification notification = new CloseableNotification(html);
        notification.addThemeVariants(variant);
        notification.open();
    }

}
