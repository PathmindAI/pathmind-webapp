package io.skymind.pathmind.webapp.bus.subscribers;

import io.skymind.pathmind.webapp.bus.BusEventType;
import io.skymind.pathmind.webapp.bus.EventBusSubscriber;
import io.skymind.pathmind.webapp.bus.events.UserUpdateBusEvent;

public interface UserUpdateSubscriber extends EventBusSubscriber<UserUpdateBusEvent> {
    @Override
    default BusEventType getEventType() {
        return BusEventType.UserUpdate;
    }
}
