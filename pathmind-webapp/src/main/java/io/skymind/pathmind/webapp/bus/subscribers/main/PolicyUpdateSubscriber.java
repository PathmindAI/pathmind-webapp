package io.skymind.pathmind.webapp.bus.subscribers.main;

import io.skymind.pathmind.webapp.bus.BusEventType;
import io.skymind.pathmind.webapp.bus.EventBusSubscriber;
import io.skymind.pathmind.webapp.bus.events.main.PolicyUpdateBusEvent;

public abstract class PolicyUpdateSubscriber extends EventBusSubscriber<PolicyUpdateBusEvent> {
    public PolicyUpdateSubscriber() {
        super();
    }

    @Override
    public BusEventType getEventType() {
        return BusEventType.PolicyUpdate;
    }
}
