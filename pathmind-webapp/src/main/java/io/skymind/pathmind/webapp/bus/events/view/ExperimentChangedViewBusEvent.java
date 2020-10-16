package io.skymind.pathmind.webapp.bus.events.view;

import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.webapp.bus.BusEventType;
import io.skymind.pathmind.webapp.bus.PathmindViewBusEvent;

public class ExperimentChangedViewBusEvent implements PathmindViewBusEvent {

    private Experiment experiment;

    public ExperimentChangedViewBusEvent(Experiment experiment) {
        this.experiment = experiment;
    }

    @Override
    public BusEventType getEventType() {
        return BusEventType.ExperimentChanged;
    }

    public Experiment getExperiment() {
        return experiment;
    }

    @Override
    public ExperimentChangedViewBusEvent cloneForEventBus() {
        return new ExperimentChangedViewBusEvent(experiment.deepClone());
    }
}
