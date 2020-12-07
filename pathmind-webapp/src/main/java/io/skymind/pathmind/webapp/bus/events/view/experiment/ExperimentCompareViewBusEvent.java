package io.skymind.pathmind.webapp.bus.events.view.experiment;

import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.webapp.bus.BusEventType;
import io.skymind.pathmind.webapp.bus.PathmindViewBusEvent;

public class ExperimentCompareViewBusEvent implements PathmindViewBusEvent {

    private Experiment experiment;
    private boolean isCompare;

    public ExperimentCompareViewBusEvent(Experiment experiment, boolean isCompare) {
        this.experiment = experiment;
        this.isCompare = isCompare;
    }

    @Override
    public BusEventType getEventType() {
        return BusEventType.ExperimentCompare;
    }

    public Experiment getExperiment() {
        return experiment;
    }

    public boolean isCompare() {
        return isCompare;
    }

    @Override
    public ExperimentCompareViewBusEvent cloneForEventBus() {
        return new ExperimentCompareViewBusEvent(experiment.deepClone(), isCompare);
    }
}
