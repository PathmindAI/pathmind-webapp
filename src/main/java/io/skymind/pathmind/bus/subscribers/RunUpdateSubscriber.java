package io.skymind.pathmind.bus.subscribers;

import io.skymind.pathmind.bus.BusEventType;
import io.skymind.pathmind.bus.EventBusSubscriber;
import io.skymind.pathmind.bus.events.RunUpdateBusEvent;

public interface RunUpdateSubscriber extends EventBusSubscriber<RunUpdateBusEvent>
{
    @Override
    default BusEventType getEventType() {
        return BusEventType.RunUpdate;
    }
}
