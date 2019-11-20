package io.skymind.pathmind.bus.subscribers;

import io.skymind.pathmind.bus.BusEventType;
import io.skymind.pathmind.bus.EventBusSubscriber;
import io.skymind.pathmind.bus.events.UserUpdateBusEvent;

public interface UserUpdateSubscriber extends EventBusSubscriber<UserUpdateBusEvent>
{
    @Override
    default BusEventType getEventType() {
        return BusEventType.UserUpdate;
    }
}
