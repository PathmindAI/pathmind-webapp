package io.skymind.pathmind.webapp.bus;

import com.vaadin.flow.component.UI;

public interface PathmindBusEvent {

    PathmindBusEvent cloneForEventBus();

    BusEventType getEventType();

    default UI getSourceUI() {
        return UI.getCurrent();
    }
}
