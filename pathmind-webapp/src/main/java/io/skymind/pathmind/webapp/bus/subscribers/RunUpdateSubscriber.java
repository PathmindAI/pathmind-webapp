package io.skymind.pathmind.webapp.bus.subscribers;

import io.skymind.pathmind.webapp.bus.BusEventType;
import io.skymind.pathmind.webapp.bus.EventBusSubscriber;
import io.skymind.pathmind.webapp.bus.events.RunUpdateBusEvent;

public interface RunUpdateSubscriber extends EventBusSubscriber<RunUpdateBusEvent> {
    @Override
    default BusEventType getEventType() {
        return BusEventType.RunUpdate;
    }
}
