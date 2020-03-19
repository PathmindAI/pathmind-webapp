package io.skymind.pathmind.webapp.ui.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;

import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;

public class CloseableNotification extends Notification {

	public CloseableNotification(String html) {
		Span contentLabel = new Span();
		contentLabel.getElement().setProperty("innerHTML", html);
		contentLabel.setMaxWidth("350px");
		contentLabel.getStyle().set("display", "inline-block");
		contentLabel.getStyle().set("padding-right", "15px");

		Button closeButton = new Button(VaadinIcon.CLOSE_SMALL.create());
		closeButton.addClickListener(event -> close());
		add(WrapperUtils.wrapSizeFullCenterHorizontal(contentLabel, closeButton));
		setPosition(Notification.Position.TOP_CENTER);
		setDuration(5000);
	}
}
