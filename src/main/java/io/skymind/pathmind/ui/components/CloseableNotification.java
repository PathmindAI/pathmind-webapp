package io.skymind.pathmind.ui.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;

public class CloseableNotification extends Notification {

	public CloseableNotification(String html) {
		Span contentLabel = new Span();
		contentLabel.getElement().setProperty("innerHTML", html);
		contentLabel.setMaxWidth("350px");
		contentLabel.getStyle().set("display", "inline-block");
		contentLabel.getStyle().set("padding-right", "15px");
		add(contentLabel);

		Button closeButton = new Button("Close");
		closeButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		closeButton.addClickListener(event -> close());
		add(closeButton);

		setPosition(Notification.Position.TOP_CENTER);
		setDuration(5000);
	}
}
