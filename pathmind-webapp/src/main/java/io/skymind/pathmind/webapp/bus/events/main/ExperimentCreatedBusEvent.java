package io.skymind.pathmind.webapp.bus.events.main;

import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.webapp.bus.BusEventType;
import io.skymind.pathmind.webapp.bus.PathmindBusEvent;

public class ExperimentCreatedBusEvent implements PathmindBusEvent {

    private Experiment experiment;

    public ExperimentCreatedBusEvent(Experiment experiment) {
        this.experiment = experiment;
    }

    @Override
    public BusEventType getEventType() {
        return BusEventType.ExperimentCreated;
    }

    public Experiment getExperiment() {
        return experiment;
    }

    public long getModelId() {
        return experiment.getModelId();
    }
}
