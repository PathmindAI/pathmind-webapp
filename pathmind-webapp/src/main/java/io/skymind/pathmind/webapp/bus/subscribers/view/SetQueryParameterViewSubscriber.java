package io.skymind.pathmind.webapp.bus.subscribers.view;

import io.skymind.pathmind.webapp.bus.BusEventType;
import io.skymind.pathmind.webapp.bus.EventBusSubscriber;
import io.skymind.pathmind.webapp.bus.events.view.SetQueryParameterViewBusEvent;

public abstract class SetQueryParameterViewSubscriber extends EventBusSubscriber<SetQueryParameterViewBusEvent> {

    public SetQueryParameterViewSubscriber() {
        super();
    }

    @Override
    public BusEventType getEventType() {
        return BusEventType.SetQueryParameter;
    }
}
