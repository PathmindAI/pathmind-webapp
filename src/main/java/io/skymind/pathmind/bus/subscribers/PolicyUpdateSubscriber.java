package io.skymind.pathmind.bus.subscribers;

import io.skymind.pathmind.bus.BusEventType;
import io.skymind.pathmind.bus.EventBusSubscriber;
import io.skymind.pathmind.bus.events.PolicyUpdateBusEvent;

public interface PolicyUpdateSubscriber extends EventBusSubscriber<PolicyUpdateBusEvent>
{
    @Override
    default BusEventType getEventType() {
        return BusEventType.PolicyUpdate;
    }
}
