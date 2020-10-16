package io.skymind.pathmind.webapp.bus.subscribers.main;

import com.vaadin.flow.component.UI;
import io.skymind.pathmind.webapp.bus.BusEventType;
import io.skymind.pathmind.webapp.bus.EventBusSubscriber;
import io.skymind.pathmind.webapp.bus.events.main.UserUpdateBusEvent;

import java.util.Optional;
import java.util.function.Supplier;

public abstract class UserUpdateSubscriber extends EventBusSubscriber<UserUpdateBusEvent> {
    public UserUpdateSubscriber(Supplier<Optional<UI>> getUISupplier) {
        super(getUISupplier);
    }

    @Override
    public BusEventType getEventType() {
        return BusEventType.UserUpdate;
    }
}
