package io.skymind.pathmind.webapp.bus.events.main;

import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.webapp.bus.BusEventType;
import io.skymind.pathmind.webapp.bus.PathmindBusEvent;

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
