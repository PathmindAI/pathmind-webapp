package io.skymind.pathmind.webapp.bus.subscribers;

import com.vaadin.flow.component.UI;
import io.skymind.pathmind.webapp.bus.BusEventType;
import io.skymind.pathmind.webapp.bus.EventBusSubscriber;
import io.skymind.pathmind.webapp.bus.events.PolicyUpdateBusEvent;

import java.util.Optional;
import java.util.function.Supplier;

public abstract class PolicyUpdateSubscriber extends EventBusSubscriber<PolicyUpdateBusEvent> {
    public PolicyUpdateSubscriber(Supplier<Optional<UI>> getUISupplier) {
        super(getUISupplier);
    }

    @Override
    public BusEventType getEventType() {
        return BusEventType.PolicyUpdate;
    }
}
