package io.skymind.pathmind.ui.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;

public class CloseableNotification extends Notification {

	public CloseableNotification(String text) {
		Span contentLabel = new Span(text);
		contentLabel.setMaxWidth("350px");
		contentLabel.getStyle().set("display", "inline-block");
		contentLabel.getStyle().set("padding-right", "15px");
		add(contentLabel);

		Button closeButton = new Button("Close");
		closeButton.addClickListener(event -> close());
		add(closeButton);

		setPosition(Notification.Position.TOP_CENTER);
		setDuration(5000);
	}
}
