package io.skymind.pathmind.webapp.ui.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;

import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;

public class CloseableNotification extends Notification {

	public CloseableNotification(String html) {
        this(html, true, null);
	}

	public CloseableNotification(String html, Boolean isCloseable, Button actionButton) {
        long durationBeforeClose = isCloseable ? 5000 : -1;
		Span contentLabel = LabelFactory.createLabel("", "closeable-notification-text-label");
		contentLabel.getElement().setProperty("innerHTML", html);
        
        if (actionButton == null) {
            actionButton = new Button(VaadinIcon.CLOSE_SMALL.create());
            actionButton.addClickListener(event -> close());
        }
        
		add(WrapperUtils.wrapSizeFullCenterHorizontal(contentLabel, actionButton));
        setPosition(Notification.Position.TOP_CENTER);
        setDuration(durationBeforeClose);
	}
}
