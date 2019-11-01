package io.skymind.pathmind.ui.utils;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class NotificationUtils
{
	public enum Style {
		Simple,
		Success,
		Warn,
		Error,
		Todo
	}

	private NotificationUtils() {
	}

	public static void showCenteredSimpleNotification(String text, Style style) {
		Label contentLabel = new Label(text);
		applyStyle(contentLabel, style);
		Button closeButton = new Button("Close");
		Notification notification = new Notification(new VerticalLayout(contentLabel, closeButton));
		notification.setDuration(3000);
		closeButton.addClickListener(event -> notification.close());
		notification.setPosition(Notification.Position.MIDDLE);
		notification.open();
	}

	public static void showTopRightInlineNotification(String text, NotificationVariant variant) {
		Span contentLabel = new Span(text);
		contentLabel.setMaxWidth("350px");
		contentLabel.getStyle().set("display", "inline-block");
		contentLabel.getStyle().set("padding-right", "15px");
		Button closeButton = new Button("Close");
		Notification notification = new Notification(contentLabel, closeButton);
		closeButton.addClickListener(event -> notification.close());
		notification.setPosition(Notification.Position.TOP_END);
		notification.setDuration(3000);
		notification.addThemeVariants(variant);
		notification.open();
	}

}
