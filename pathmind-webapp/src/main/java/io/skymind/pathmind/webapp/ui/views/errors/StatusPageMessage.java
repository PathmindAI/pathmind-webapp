package io.skymind.pathmind.webapp.ui.views.errors;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Paragraph;

class StatusPageMessage {
    public static Component getMessage() {
        Paragraph statusMessage = new Paragraph();
        statusMessage.add("You can check the current Pathmind system status at ");
        Anchor statusLink = new Anchor("https://status.pathmind.com/", "status.pathmind.com");
        statusLink.setTarget("_blank");
        statusMessage.add(statusLink);
        return statusMessage;
    }
}
