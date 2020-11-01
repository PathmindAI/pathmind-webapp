package io.skymind.pathmind.webapp.bus.subscribers.main;

import com.vaadin.flow.component.UI;
import io.skymind.pathmind.webapp.bus.BusEventType;
import io.skymind.pathmind.webapp.bus.EventBusSubscriber;
import io.skymind.pathmind.webapp.bus.events.main.ExperimentUpdatedBusEvent;

import java.util.Optional;
import java.util.function.Supplier;

public abstract class ExperimentUpdatedSubscriber extends EventBusSubscriber<ExperimentUpdatedBusEvent> {

    public ExperimentUpdatedSubscriber(Supplier<Optional<UI>> getUISupplier) {
        super(getUISupplier);
    }

    @Override
    public BusEventType getEventType() {
        return BusEventType.ExperimentUpdate;
    }
}
