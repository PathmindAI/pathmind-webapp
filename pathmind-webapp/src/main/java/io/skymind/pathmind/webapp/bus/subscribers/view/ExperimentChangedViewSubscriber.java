package io.skymind.pathmind.webapp.bus.subscribers.view;

import com.vaadin.flow.component.UI;
import io.skymind.pathmind.webapp.bus.BusEventType;
import io.skymind.pathmind.webapp.bus.EventBusSubscriber;
import io.skymind.pathmind.webapp.bus.events.view.ExperimentChangedViewBusEvent;

import java.util.Optional;
import java.util.function.Supplier;

public abstract class ExperimentChangedViewSubscriber extends EventBusSubscriber<ExperimentChangedViewBusEvent> {

    public ExperimentChangedViewSubscriber(Supplier<Optional<UI>> getUISupplier) {
        super(getUISupplier);
    }

    @Override
    public BusEventType getEventType() {
        return BusEventType.ExperimentChanged;
    }
}
