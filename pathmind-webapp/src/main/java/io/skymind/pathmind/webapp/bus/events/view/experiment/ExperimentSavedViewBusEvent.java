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
}
