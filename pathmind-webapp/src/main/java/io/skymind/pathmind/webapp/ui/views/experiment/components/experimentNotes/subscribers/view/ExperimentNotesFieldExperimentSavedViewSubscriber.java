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

    // TODO -> STEPH -> This needs to be done as part of the experimentView.setExperiment() code but it's special because it's also a save and has a popup
    // confirmation dialog window with a callback listener so I'm pushing this off until later.
    @Override
    public void handleBusEvent(ExperimentSwitchedViewBusEvent event) {
        experimentNotesField.saveNotesToExperiment();
    }
}