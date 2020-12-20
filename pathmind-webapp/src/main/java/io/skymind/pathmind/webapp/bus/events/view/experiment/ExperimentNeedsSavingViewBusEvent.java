package io.skymind.pathmind.webapp.bus.events.view.experiment;

import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.webapp.bus.BusEventType;
import io.skymind.pathmind.webapp.bus.PathmindViewBusEvent;

public class ExperimentNeedsSavingViewBusEvent implements PathmindViewBusEvent {

    private Experiment experiment;

    public ExperimentNeedsSavingViewBusEvent(Experiment experiment) {
        this.experiment = experiment;
    }

    @Override
    public BusEventType getEventType() {
        return BusEventType.ExperimentNeedsSaving;
    }

    public Experiment getExperiment() {
        return experiment;
    }
}
