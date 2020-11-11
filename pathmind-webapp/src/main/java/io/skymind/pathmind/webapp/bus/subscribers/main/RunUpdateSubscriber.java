package io.skymind.pathmind.webapp.bus.subscribers.main;

import io.skymind.pathmind.webapp.bus.BusEventType;
import io.skymind.pathmind.webapp.bus.EventBusSubscriber;
import io.skymind.pathmind.webapp.bus.events.main.RunUpdateBusEvent;

public abstract class RunUpdateSubscriber extends EventBusSubscriber<RunUpdateBusEvent> {

    public RunUpdateSubscriber() {
        super();
    }

    @Override
    public  BusEventType getEventType() {
        return BusEventType.RunUpdate;
    }
}
