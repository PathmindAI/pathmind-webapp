package io.skymind.pathmind.webapp.bus.subscribers;

import io.skymind.pathmind.webapp.bus.BusEventType;
import io.skymind.pathmind.webapp.bus.EventBusSubscriber;
import io.skymind.pathmind.webapp.bus.events.ExperimentCreatedBusEvent;
import io.skymind.pathmind.webapp.bus.events.PolicyUpdateBusEvent;

public interface ExperimentCreatedSubscriber extends EventBusSubscriber<ExperimentCreatedBusEvent> {
    @Override
    default BusEventType getEventType() {
        return BusEventType.ExperimentCreated;
    }
}
