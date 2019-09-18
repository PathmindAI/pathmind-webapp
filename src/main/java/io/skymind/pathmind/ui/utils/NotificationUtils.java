package io.skymind.pathmind.ui.utils;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class NotificationUtils
{
	enum Style {
		Simple,
		Warn,
		Error,
		Todo
	}

	private NotificationUtils() {
	}

	// TODO -> Remove when done.
	public static void showTodoNotification() {
		showCenteredSimpleNotification("Todo    ", Style.Todo);
	}

	public static void showTodoNotification(String text) {
		showCenteredSimpleNotification("Todo    : " + text, Style.Todo);
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

	// TODO -> Remove and either put in CSS or just remove alltogether
	private static void applyStyle(Label label, Style style) {
		switch(style) {
			case Warn:
				label.getStyle().set("color", "orange");
				return;
			case Error:
				label.getStyle().set("color", "red");
				return;
			case Todo:
				label.getStyle().set("color", "purple");
				return;
		}
	}
}
