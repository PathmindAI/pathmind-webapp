package io.skymind.pathmind.shared.bus.subscribers;

import io.skymind.pathmind.shared.bus.BusEventType;
import io.skymind.pathmind.shared.bus.EventBusSubscriber;
import io.skymind.pathmind.shared.bus.events.PolicyUpdateBusEvent;

public interface PolicyUpdateSubscriber extends EventBusSubscriber<PolicyUpdateBusEvent>
{
    @Override
    default BusEventType getEventType() {
        return BusEventType.PolicyUpdate;
    }
}
