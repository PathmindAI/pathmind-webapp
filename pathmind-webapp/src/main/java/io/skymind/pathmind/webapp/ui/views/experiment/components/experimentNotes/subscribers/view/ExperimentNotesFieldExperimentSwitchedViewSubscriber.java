package io.skymind.pathmind.webapp.ui.views.experiment.components.experimentNotes.subscribers.view;

import io.skymind.pathmind.webapp.bus.events.view.ExperimentSwitchedViewBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.view.ExperimentSwitchedViewSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.components.experimentNotes.ExperimentNotesField;

public class ExperimentNotesFieldExperimentSwitchedViewSubscriber extends ExperimentSwitchedViewSubscriber {

    private ExperimentNotesField experimentNotesField;

    public ExperimentNotesFieldExperimentSwitchedViewSubscriber(ExperimentNotesField experimentNotesField) {
        super();
        this.experimentNotesField = experimentNotesField;
    }

    @Override
    public void handleBusEvent(ExperimentSwitchedViewBusEvent event) {
        experimentNotesField.setExperiment(event.getExperiment());
        experimentNotesField.setNotesText(event.getExperiment().getUserNotes());
    }

}