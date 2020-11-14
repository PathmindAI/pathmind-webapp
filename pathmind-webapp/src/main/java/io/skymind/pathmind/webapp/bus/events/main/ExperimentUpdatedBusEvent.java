package io.skymind.pathmind.webapp.bus.events.main;

import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.webapp.bus.BusEventType;
import io.skymind.pathmind.webapp.bus.PathmindBusEvent;

public class ExperimentUpdatedBusEvent implements PathmindBusEvent {

    public enum ExperimentUpdateType {
        ExperimentDataUpdate,
        StartTraining,
    }

    private Experiment experiment;
    private ExperimentUpdateType experimentUpdateType;

    public ExperimentUpdatedBusEvent(Experiment experiment) {
        this(experiment, ExperimentUpdateType.ExperimentDataUpdate);
    }

    public ExperimentUpdatedBusEvent(Experiment experiment, ExperimentUpdateType experimentUpdateType) {
        this.experiment = experiment;
        this.experimentUpdateType = experimentUpdateType;
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

    public boolean isStartedTrainingEventType() {
        return ExperimentUpdateType.StartTraining.equals(experimentUpdateType);
    }

    public ExperimentUpdateType getExperimentUpdateType() {
        return experimentUpdateType;
    }

    @Override
    public ExperimentUpdatedBusEvent cloneForEventBus() {
        return new ExperimentUpdatedBusEvent(experiment.deepClone(), experimentUpdateType);
    }
}
