package io.skymind.pathmind.webapp.bus.events.view;

import io.skymind.pathmind.webapp.bus.BusEventType;
import io.skymind.pathmind.webapp.bus.PathmindViewBusEvent;

public class SetQueryParameterViewBusEvent implements PathmindViewBusEvent {

    private String name;
    private String value;

    public SetQueryParameterViewBusEvent(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public BusEventType getEventType() {
        return BusEventType.SetQueryParameter;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
