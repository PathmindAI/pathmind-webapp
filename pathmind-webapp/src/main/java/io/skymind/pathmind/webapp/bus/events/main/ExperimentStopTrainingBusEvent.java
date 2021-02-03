package io.skymind.pathmind.webapp.bus.events.main;

import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.webapp.bus.BusEventType;
import io.skymind.pathmind.webapp.bus.PathmindBusEvent;

/**
 * Event used to indicate that an experiment has stopped training. We pass the full experiment
 * instance because components like the navbar need to be able to update the status.
 */
public class ExperimentStopTrainingBusEvent implements PathmindBusEvent {

    private Experiment experiment;

    public ExperimentStopTrainingBusEvent(Experiment experiment) {
        super();
        this.experiment = experiment;
    }
    @Override
    public BusEventType getEventType() {
        return BusEventType.ExperimentStopTraining;
    }

    public Experiment getExperiment() {
        return experiment;
    }

    public long getModelId() {
        return experiment.getModelId();
    }

    @Override
    public ExperimentStopTrainingBusEvent cloneForEventBus() {
        return new ExperimentStopTrainingBusEvent(experiment.deepClone());
    }
}
