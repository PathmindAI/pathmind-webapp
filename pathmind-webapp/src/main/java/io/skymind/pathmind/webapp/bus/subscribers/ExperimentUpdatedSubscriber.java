package io.skymind.pathmind.webapp.bus.subscribers;

import io.skymind.pathmind.webapp.bus.BusEventType;
import io.skymind.pathmind.webapp.bus.EventBusSubscriber;
import io.skymind.pathmind.webapp.bus.events.ExperimentCreatedBusEvent;
import io.skymind.pathmind.webapp.bus.events.ExperimentUpdatedBusEvent;

public interface ExperimentUpdatedSubscriber extends EventBusSubscriber<ExperimentUpdatedBusEvent> {
    @Override
    default BusEventType getEventType() {
        return BusEventType.ExperimentUpdate;
    }
}
