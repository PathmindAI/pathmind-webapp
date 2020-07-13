package io.skymind.pathmind.webapp.bus.events;

import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.webapp.bus.BusEventType;
import io.skymind.pathmind.webapp.bus.PathmindBusEvent;

public class ExperimentUpdatedBusEvent implements PathmindBusEvent {
    private Experiment experiment;
    private boolean startedTraining = false;

    public ExperimentUpdatedBusEvent(Experiment experiment) {
        this.experiment = experiment;
    }

    public ExperimentUpdatedBusEvent(Experiment experiment, boolean startedTraining) {
        this.experiment = experiment;
        this.startedTraining = startedTraining;
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

    public boolean isStartedTraining() {
        return startedTraining;
    }
}
