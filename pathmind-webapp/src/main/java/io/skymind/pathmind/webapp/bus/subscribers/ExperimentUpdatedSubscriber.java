package io.skymind.pathmind.webapp.bus.subscribers;

import com.vaadin.flow.component.UI;
import io.skymind.pathmind.webapp.bus.BusEventType;
import io.skymind.pathmind.webapp.bus.EventBusSubscriber;
import io.skymind.pathmind.webapp.bus.events.ExperimentCreatedBusEvent;
import io.skymind.pathmind.webapp.bus.events.ExperimentUpdatedBusEvent;

import java.util.Optional;
import java.util.function.Supplier;

public abstract class ExperimentUpdatedSubscriber extends EventBusSubscriber<ExperimentUpdatedBusEvent> {

    public ExperimentUpdatedSubscriber(Supplier<Optional<UI>> getUISupplier) {
        super(getUISupplier);
    }

    public ExperimentUpdatedSubscriber(Supplier<Optional<UI>> getUISupplier, boolean isListenForEventOnSameUI) {
        super(getUISupplier, isListenForEventOnSameUI);
    }

    @Override
    public BusEventType getEventType() {
        return BusEventType.ExperimentUpdate;
    }
}
