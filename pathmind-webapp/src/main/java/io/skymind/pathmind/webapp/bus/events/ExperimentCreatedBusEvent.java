package io.skymind.pathmind.webapp.bus.events;

import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.webapp.bus.BusEventType;
import io.skymind.pathmind.webapp.bus.CloneablePathmindBusEvent;

public class ExperimentCreatedBusEvent implements CloneablePathmindBusEvent {
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

    @Override
    public ExperimentCreatedBusEvent cloneForEventBus() {
        return new ExperimentCreatedBusEvent(experiment.deepClone());
    }
}
