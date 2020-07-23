package io.skymind.pathmind.webapp.bus.events;

import com.vaadin.flow.component.UI;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.webapp.bus.BusEventType;
import io.skymind.pathmind.webapp.bus.PathmindBusEvent;

public class ExperimentUpdatedBusEvent implements PathmindBusEvent {
    private Experiment experiment;
    private boolean startedTraining = false;
    private UI ui;

    public ExperimentUpdatedBusEvent(UI ui, Experiment experiment) {
        this.experiment = experiment;
        this.ui = ui;
    }

    public ExperimentUpdatedBusEvent(UI ui, Experiment experiment, boolean startedTraining) {
        this.experiment = experiment;
        this.startedTraining = startedTraining;
        this.ui = ui;
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
        return startedTraining;
    }

    public UI getUi() {
        return ui;
    }
}
