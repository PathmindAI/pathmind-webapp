package io.skymind.pathmind.webapp.bus.events;

import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.webapp.bus.BusEventType;
import io.skymind.pathmind.webapp.bus.PathmindBusEvent;

public class ExperimentUpdatedBusEvent implements PathmindBusEvent {
    private Experiment experiment;

    public ExperimentUpdatedBusEvent(Experiment experiment) {
        this.experiment = experiment;
    }

    @Override
    public BusEventType getEventType() {
        return BusEventType.ExperimentUpdate;
    }

    public Experiment getExperiment() {
        return experiment;
    }

    public long getModelId() {
        return experiment.getModelId();
    }
}
