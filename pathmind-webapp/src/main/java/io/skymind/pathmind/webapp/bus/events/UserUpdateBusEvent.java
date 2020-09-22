package io.skymind.pathmind.webapp.bus.events;

import io.skymind.pathmind.webapp.bus.BusEventType;
import io.skymind.pathmind.webapp.bus.CloneablePathmindBusEvent;
import io.skymind.pathmind.webapp.bus.PathmindBusEvent;
import io.skymind.pathmind.shared.data.PathmindUser;

public class UserUpdateBusEvent implements CloneablePathmindBusEvent {

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
