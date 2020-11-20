package io.skymind.pathmind.webapp.bus.subscribers.main;

import io.skymind.pathmind.webapp.bus.BusEventType;
import io.skymind.pathmind.webapp.bus.EventBusSubscriber;
import io.skymind.pathmind.webapp.bus.events.main.ExperimentCreatedBusEvent;

public abstract class ExperimentCreatedSubscriber extends EventBusSubscriber<ExperimentCreatedBusEvent> {
    public ExperimentCreatedSubscriber() {
        super();
    }

    @Override
    public BusEventType getEventType() {
        return BusEventType.ExperimentCreated;
    }
}
