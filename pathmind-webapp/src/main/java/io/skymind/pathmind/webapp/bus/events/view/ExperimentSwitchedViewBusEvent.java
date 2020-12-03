package io.skymind.pathmind.webapp.bus.events.view;

import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.webapp.bus.BusEventType;
import io.skymind.pathmind.webapp.bus.PathmindViewBusEvent;

public class ExperimentSwitchedViewBusEvent implements PathmindViewBusEvent {

    private Experiment experiment;

    public ExperimentSwitchedViewBusEvent(Experiment experiment) {
        this.experiment = experiment;
    }

    @Override
    public BusEventType getEventType() {
        return BusEventType.ExperimentSwitched;
    }

    public Experiment getExperiment() {
        return experiment;
    }

    @Override
    public ExperimentSwitchedViewBusEvent cloneForEventBus() {
        return new ExperimentSwitchedViewBusEvent(experiment.deepClone());
    }
}
