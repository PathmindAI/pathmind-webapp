package io.skymind.pathmind.webapp.bus.events;

import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.webapp.bus.BusEventType;
import io.skymind.pathmind.webapp.bus.PathmindBusEvent;

public class ExperimentUpdatedBusEvent implements PathmindBusEvent {

    public enum ExperimentUpdateType {
        ExperimentDataUpdate,
        StartTraining,
        Favorite,
        Archive
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

    public boolean isStartedTraining() {
        return ExperimentUpdateType.StartTraining.equals(experimentUpdateType);
    }

    public boolean isArchive() {
        return ExperimentUpdateType.Archive.equals(experimentUpdateType);
    }

    public ExperimentUpdateType getExperimentUpdateType() {
        return experimentUpdateType;
    }
}
