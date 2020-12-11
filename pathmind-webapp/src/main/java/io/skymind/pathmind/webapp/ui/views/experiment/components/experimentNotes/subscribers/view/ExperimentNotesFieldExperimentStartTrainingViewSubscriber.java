package io.skymind.pathmind.webapp.ui.views.experiment.components.experimentNotes.subscribers.view;

import io.skymind.pathmind.webapp.bus.events.main.ExperimentStartTrainingBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.main.ExperimentStartTrainingSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.components.experimentNotes.ExperimentNotesField;

public class ExperimentNotesFieldExperimentStartTrainingViewSubscriber extends ExperimentStartTrainingSubscriber {

    private ExperimentNotesField experimentNotesField;

    public ExperimentNotesFieldExperimentStartTrainingViewSubscriber(ExperimentNotesField experimentNotesField) {
        super();
        this.experimentNotesField = experimentNotesField;
    }

    @Override
    public void handleBusEvent(ExperimentStartTrainingBusEvent event) {
        experimentNotesField.saveNotesToExperiment();
    }
}