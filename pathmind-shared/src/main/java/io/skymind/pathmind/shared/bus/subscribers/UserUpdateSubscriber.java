package io.skymind.pathmind.shared.bus.subscribers;

import io.skymind.pathmind.shared.bus.BusEventType;
import io.skymind.pathmind.shared.bus.EventBusSubscriber;
import io.skymind.pathmind.shared.bus.events.UserUpdateBusEvent;

public interface UserUpdateSubscriber extends EventBusSubscriber<UserUpdateBusEvent>
{
    @Override
    default BusEventType getEventType() {
        return BusEventType.UserUpdate;
    }
}
