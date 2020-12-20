package io.skymind.pathmind.webapp.bus.events.view.experiment;

import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.webapp.bus.BusEventType;
import io.skymind.pathmind.webapp.bus.PathmindViewBusEvent;

// TODO -> STEPH -> Should either be removed and be an action or should be moved to the event main package.
public class ExperimentChangedViewBusEvent implements PathmindViewBusEvent {

    private Experiment experiment;

    public ExperimentChangedViewBusEvent(Experiment experiment) {
        this.experiment = experiment;
    }

    @Override
    public BusEventType getEventType() {
        return BusEventType.ExperimentChanged;
    }

    public Experiment getExperiment() {
        return experiment;
    }
}
