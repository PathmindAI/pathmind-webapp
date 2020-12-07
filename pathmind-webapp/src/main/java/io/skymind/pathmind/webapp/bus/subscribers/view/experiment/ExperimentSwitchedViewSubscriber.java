package io.skymind.pathmind.webapp.bus.subscribers.view.experiment;

import io.skymind.pathmind.webapp.bus.BusEventType;
import io.skymind.pathmind.webapp.bus.EventBusSubscriber;
import io.skymind.pathmind.webapp.bus.events.view.experiment.ExperimentSwitchedViewBusEvent;

public abstract class ExperimentSwitchedViewSubscriber extends EventBusSubscriber<ExperimentSwitchedViewBusEvent> {

    public ExperimentSwitchedViewSubscriber() {
        super();
    }

    @Override
    public BusEventType getEventType() {
        return BusEventType.ExperimentSwitched;
    }
}
