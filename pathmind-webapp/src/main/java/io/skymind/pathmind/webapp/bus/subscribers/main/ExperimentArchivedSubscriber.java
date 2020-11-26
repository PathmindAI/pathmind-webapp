package io.skymind.pathmind.webapp.bus.subscribers.main;

import io.skymind.pathmind.webapp.bus.BusEventType;
import io.skymind.pathmind.webapp.bus.EventBusSubscriber;
import io.skymind.pathmind.webapp.bus.events.main.ExperimentArchivedBusEvent;

public abstract class ExperimentArchivedSubscriber extends EventBusSubscriber<ExperimentArchivedBusEvent> {

    public ExperimentArchivedSubscriber() {
        super();
    }

    @Override
    public BusEventType getEventType() {
        return BusEventType.ExperimentArchived;
    }
}
