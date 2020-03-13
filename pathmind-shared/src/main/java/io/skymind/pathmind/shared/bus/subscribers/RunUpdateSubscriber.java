package io.skymind.pathmind.shared.bus.subscribers;

import io.skymind.pathmind.shared.bus.BusEventType;
import io.skymind.pathmind.shared.bus.EventBusSubscriber;
import io.skymind.pathmind.shared.bus.events.RunUpdateBusEvent;

public interface RunUpdateSubscriber extends EventBusSubscriber<RunUpdateBusEvent>
{
    @Override
    default BusEventType getEventType() {
        return BusEventType.RunUpdate;
    }
}
