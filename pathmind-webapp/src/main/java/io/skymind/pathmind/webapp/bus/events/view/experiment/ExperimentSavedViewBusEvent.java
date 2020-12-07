package io.skymind.pathmind.webapp.bus.events.view.experiment;

import io.skymind.pathmind.webapp.bus.BusEventType;
import io.skymind.pathmind.webapp.bus.PathmindViewBusEvent;

public class ExperimentSavedViewBusEvent implements PathmindViewBusEvent {

    public ExperimentSavedViewBusEvent() {
    }

    @Override
    public BusEventType getEventType() {
        return BusEventType.ExperimentSaved;
    }

    @Override
    public ExperimentSavedViewBusEvent cloneForEventBus() {
        // Since there's no internal data we can just return the same instance rather than clone it.
        return this;
    }
}
