package io.skymind.pathmind.webapp.bus.subscribers.view.experiment;

import io.skymind.pathmind.webapp.bus.BusEventType;
import io.skymind.pathmind.webapp.bus.EventBusSubscriber;
import io.skymind.pathmind.webapp.bus.events.view.experiment.ExperimentCompareViewBusEvent;

public abstract class ExperimentCompareViewSubscriber extends EventBusSubscriber<ExperimentCompareViewBusEvent> {

    public ExperimentCompareViewSubscriber() {
        super();
    }

    @Override
    public BusEventType getEventType() {
        return BusEventType.ExperimentCompare;
    }
}
