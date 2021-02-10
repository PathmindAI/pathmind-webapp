package io.skymind.pathmind.webapp.bus.events.main;

import io.skymind.pathmind.shared.data.PathmindUser;
import io.skymind.pathmind.webapp.bus.BusEventType;
import io.skymind.pathmind.webapp.bus.PathmindBusEvent;

/**
 * Event used to indicate that there has been a change to the user. This is mainly used to update
 * the user's name on the top right of the screen should it be changed.
 */
public class UserUpdateBusEvent implements PathmindBusEvent {

    private PathmindUser user;

    public UserUpdateBusEvent(PathmindUser user) {
        this.user = user;
    }

    @Override
    public BusEventType getEventType() {
        return BusEventType.UserUpdate;
    }

    public PathmindUser getPathmindUser() {
        return user;
    }

    @Override
    public UserUpdateBusEvent cloneForEventBus() {
        return new UserUpdateBusEvent(user.shallowClone());
    }
}
