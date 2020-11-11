package io.skymind.pathmind.webapp.bus.subscribers.view;

import com.vaadin.flow.component.UI;
import io.skymind.pathmind.webapp.bus.BusEventType;
import io.skymind.pathmind.webapp.bus.EventBusSubscriber;
import io.skymind.pathmind.webapp.bus.events.view.SetQueryParameterViewBusEvent;

import java.util.Optional;
import java.util.function.Supplier;

public abstract class SetQueryParameterViewSubscriber extends EventBusSubscriber<SetQueryParameterViewBusEvent> {

    public SetQueryParameterViewSubscriber(Supplier<Optional<UI>> getUISupplier) {
        super(getUISupplier);
    }

    @Override
    public BusEventType getEventType() {
        return BusEventType.SetQueryParameter;
    }
}