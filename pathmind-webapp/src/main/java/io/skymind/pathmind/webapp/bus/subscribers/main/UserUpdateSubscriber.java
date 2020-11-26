package io.skymind.pathmind.webapp.bus.subscribers.main;

import io.skymind.pathmind.webapp.bus.BusEventType;
import io.skymind.pathmind.webapp.bus.EventBusSubscriber;
import io.skymind.pathmind.webapp.bus.events.main.UserUpdateBusEvent;

public abstract class UserUpdateSubscriber extends EventBusSubscriber<UserUpdateBusEvent> {

    public UserUpdateSubscriber() {
        super(true);
    }

    @Override
    public BusEventType getEventType() {
        return BusEventType.UserUpdate;
    }
}
