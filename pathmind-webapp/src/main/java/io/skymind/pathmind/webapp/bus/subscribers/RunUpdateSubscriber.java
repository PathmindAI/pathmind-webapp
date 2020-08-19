package io.skymind.pathmind.webapp.bus.subscribers;

import com.vaadin.flow.component.UI;
import io.skymind.pathmind.webapp.bus.BusEventType;
import io.skymind.pathmind.webapp.bus.EventBusSubscriber;
import io.skymind.pathmind.webapp.bus.events.RunUpdateBusEvent;

import java.util.Optional;
import java.util.function.Supplier;
public abstract class RunUpdateSubscriber extends EventBusSubscriber<RunUpdateBusEvent> {
    public RunUpdateSubscriber(Supplier<Optional<UI>> getUISupplier) {
        super(getUISupplier);
    }

    @Override
    public  BusEventType getEventType() {
        return BusEventType.RunUpdate;
    }
}
