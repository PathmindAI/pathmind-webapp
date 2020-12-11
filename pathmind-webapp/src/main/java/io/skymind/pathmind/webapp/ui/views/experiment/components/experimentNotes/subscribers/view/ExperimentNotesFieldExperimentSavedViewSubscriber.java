package io.skymind.pathmind.webapp.ui.views.experiment.components.experimentNotes.subscribers.view;

import io.skymind.pathmind.webapp.bus.events.view.experiment.ExperimentSwitchedViewBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.view.experiment.ExperimentSwitchedViewSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.components.experimentNotes.ExperimentNotesField;

public class ExperimentNotesFieldExperimentSavedViewSubscriber extends ExperimentSwitchedViewSubscriber {

    private ExperimentNotesField experimentNotesField;

    public ExperimentNotesFieldExperimentSavedViewSubscriber(ExperimentNotesField experimentNotesField) {
        super();
        this.experimentNotesField = experimentNotesField;
    }

    @Override
    public void handleBusEvent(ExperimentSwitchedViewBusEvent event) {
        experimentNotesField.saveNotesToExperiment();
    }
}