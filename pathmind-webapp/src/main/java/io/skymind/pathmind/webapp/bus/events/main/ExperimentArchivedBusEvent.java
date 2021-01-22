package io.skymind.pathmind.webapp.bus.events.main;

import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.webapp.bus.BusEventType;
import io.skymind.pathmind.webapp.bus.PathmindBusEvent;

/**
 * Event used to indicate when an experiment has been archived or unarchived. The reason
 * we include the full experiment is because for example if an experiment is unarchived and
 * hence added to the navbar then we need the experiment instance to not only get the experiment's
 * name and so on but also calculate the status, etc.
 */
public class ExperimentArchivedBusEvent implements PathmindBusEvent {

    private Experiment experiment;

    public ExperimentArchivedBusEvent(Experiment experiment) {
        this.experiment = experiment;
    }

    @Override
    public BusEventType getEventType() {
        return BusEventType.ExperimentArchived;
    }

    public Experiment getExperiment() {
        return experiment;
    }

    @Override
    public ExperimentArchivedBusEvent cloneForEventBus() {
        return new ExperimentArchivedBusEvent(experiment.deepClone());
    }
}
