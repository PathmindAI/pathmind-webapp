package io.skymind.pathmind.webapp.bus;

import com.vaadin.flow.component.UI;

public interface PathmindBusEvent {

    PathmindBusEvent cloneForEventBus();

    BusEventType getEventType();

    default int getSourceId() {
        if (UI.getCurrent() == null) {
            return -1;
        }
        return UI.getCurrent().getUIId();
    }
}
