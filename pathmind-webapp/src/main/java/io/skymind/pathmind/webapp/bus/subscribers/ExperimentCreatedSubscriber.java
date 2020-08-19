package io.skymind.pathmind.webapp.bus.subscribers;

import com.vaadin.flow.component.UI;
import io.skymind.pathmind.webapp.bus.BusEventType;
import io.skymind.pathmind.webapp.bus.EventBusSubscriber;
import io.skymind.pathmind.webapp.bus.events.ExperimentCreatedBusEvent;
import io.skymind.pathmind.webapp.bus.events.PolicyUpdateBusEvent;

import java.util.Optional;
import java.util.function.Supplier;

public abstract class ExperimentCreatedSubscriber extends EventBusSubscriber<ExperimentCreatedBusEvent> {
    public ExperimentCreatedSubscriber(Supplier<Optional<UI>> getUISupplier) {
        super(getUISupplier);
    }

    @Override
    public BusEventType getEventType() {
        return BusEventType.ExperimentCreated;
    }
}
