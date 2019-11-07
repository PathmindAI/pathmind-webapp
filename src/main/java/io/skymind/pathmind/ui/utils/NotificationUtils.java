package io.skymind.pathmind.ui.utils;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;

public class NotificationUtils {
    public static void showNotification(String text, NotificationVariant variant) {
        Span contentLabel = new Span(text);
        contentLabel.setMaxWidth("350px");
        contentLabel.getStyle().set("display", "inline-block");
        contentLabel.getStyle().set("padding-right", "15px");
        Button closeButton = new Button("Close");
        Notification notification = new Notification(contentLabel, closeButton);
        closeButton.addClickListener(event -> notification.close());
        notification.setPosition(Notification.Position.TOP_CENTER);
        notification.setDuration(5000);
        notification.addThemeVariants(variant);
        notification.open();
    }

}
