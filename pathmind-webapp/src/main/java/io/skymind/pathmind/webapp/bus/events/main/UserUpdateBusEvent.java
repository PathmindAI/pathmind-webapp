package io.skymind.pathmind.webapp.bus.events.main;

import io.skymind.pathmind.shared.data.PathmindUser;
import io.skymind.pathmind.webapp.bus.BusEventType;
import io.skymind.pathmind.webapp.bus.PathmindBusEvent;

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

    public void setPathmindUser(PathmindUser user) {
        this.user = user;
    }

    @Override
    public UserUpdateBusEvent cloneForEventBus() {
        return new UserUpdateBusEvent(user.shallowClone());
    }
}
