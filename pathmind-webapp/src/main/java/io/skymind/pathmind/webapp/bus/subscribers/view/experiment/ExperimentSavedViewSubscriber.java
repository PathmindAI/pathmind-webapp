package io.skymind.pathmind.webapp.bus.subscribers.view.experiment;

import io.skymind.pathmind.webapp.bus.BusEventType;
import io.skymind.pathmind.webapp.bus.EventBusSubscriber;
import io.skymind.pathmind.webapp.bus.events.view.experiment.ExperimentSavedViewBusEvent;

public abstract class ExperimentSavedViewSubscriber extends EventBusSubscriber<ExperimentSavedViewBusEvent> {

    public ExperimentSavedViewSubscriber() {
        super();
    }

    @Override
    public BusEventType getEventType() {
        return BusEventType.ExperimentSaved;
    }
}
