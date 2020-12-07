package io.skymind.pathmind.webapp.bus.subscribers.view.experiment;

import io.skymind.pathmind.webapp.bus.BusEventType;
import io.skymind.pathmind.webapp.bus.EventBusSubscriber;
import io.skymind.pathmind.webapp.bus.events.view.experiment.ExperimentChangedViewBusEvent;

public abstract class ExperimentChangedViewSubscriber extends EventBusSubscriber<ExperimentChangedViewBusEvent> {

    public ExperimentChangedViewSubscriber() {
        super();
    }

    @Override
    public BusEventType getEventType() {
        return BusEventType.ExperimentChanged;
    }
}
