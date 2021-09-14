package io.skymind.pathmind.webapp.ui.views.errors;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Span;

class StatusPageMessage {
    public static Component getMessage() {
        Span statusMessage = new Span();
        statusMessage.add("You can check the current Pathmind system status at ");
        Anchor statusLink = new Anchor("https://status.pathmind.com/", "status.pathmind.com");
        statusLink.setTarget("_blank");
        statusMessage.add(statusLink);
        return statusMessage;
    }
}
