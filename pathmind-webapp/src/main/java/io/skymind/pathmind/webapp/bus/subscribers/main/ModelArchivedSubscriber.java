package io.skymind.pathmind.webapp.bus.subscribers.main;

import io.skymind.pathmind.webapp.bus.BusEventType;
import io.skymind.pathmind.webapp.bus.EventBusSubscriber;
import io.skymind.pathmind.webapp.bus.events.main.ModelArchivedBusEvent;

public abstract class ModelArchivedSubscriber extends EventBusSubscriber<ModelArchivedBusEvent> {

    public ModelArchivedSubscriber() {
        super();
    }

    @Override
    public BusEventType getEventType() {
        return BusEventType.ModelArchived;
    }
}
