package io.skymind.pathmind.webapp.bus.events.main;

import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.webapp.bus.BusEventType;
import io.skymind.pathmind.webapp.bus.PathmindBusEvent;

/**
 * Event used to indicate when a new experiment has been created. We have to include
 * the full experiment instance because this is used to add the new experiment
 * to the navbar and so on.
 */
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

    @Override
    public ExperimentCreatedBusEvent cloneForEventBus() {
        return new ExperimentCreatedBusEvent(experiment.deepClone());
    }
}
