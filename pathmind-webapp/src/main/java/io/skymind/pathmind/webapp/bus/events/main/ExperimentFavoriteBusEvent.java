package io.skymind.pathmind.webapp.bus.events.main;

import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.webapp.bus.BusEventType;
import io.skymind.pathmind.webapp.bus.PathmindBusEvent;

public class ExperimentFavoriteBusEvent implements PathmindBusEvent {

    private Experiment experiment;

    public ExperimentFavoriteBusEvent(Experiment experiment) {
        this.experiment = experiment;
    }

    @Override
    public BusEventType getEventType() {
        return BusEventType.ExperimentFavorite;
    }

    public Experiment getExperiment() {
        return experiment;
    }
}
