package io.skymind.pathmind.webapp.bus.events.main;

import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.webapp.bus.BusEventType;
import io.skymind.pathmind.webapp.bus.PathmindBusEvent;

/**
 * Event used to indicate that an experiment has started training. We pass the full
 * experiment instance because we need the status for the navbar and so on.
 */
public class ExperimentStartTrainingBusEvent implements PathmindBusEvent {

    private Experiment experiment;

    public ExperimentStartTrainingBusEvent(Experiment experiment) {
        this.experiment = experiment;
    }

    @Override
    public BusEventType getEventType() {
        return BusEventType.ExperimentStartTraining;
    }

    public Experiment getExperiment() {
        return experiment;
    }

    @Override
    public ExperimentStartTrainingBusEvent cloneForEventBus() {
        return new ExperimentStartTrainingBusEvent(experiment.deepClone());
    }
}
