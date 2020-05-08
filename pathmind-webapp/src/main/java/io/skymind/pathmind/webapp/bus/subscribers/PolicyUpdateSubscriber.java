package io.skymind.pathmind.webapp.bus.subscribers;

import io.skymind.pathmind.webapp.bus.BusEventType;
import io.skymind.pathmind.webapp.bus.EventBusSubscriber;
import io.skymind.pathmind.webapp.bus.events.PolicyUpdateBusEvent;

public interface PolicyUpdateSubscriber extends EventBusSubscriber<PolicyUpdateBusEvent> {
    @Override
    default BusEventType getEventType() {
        return BusEventType.PolicyUpdate;
    }
}
