package io.skymind.pathmind.webapp.ui.views.experiment.components.experimentNotes.subscribers.view;

import java.util.function.Consumer;

import io.skymind.pathmind.webapp.bus.events.view.experiment.ExperimentSwitchedViewBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.view.experiment.ExperimentSwitchedViewSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.components.experimentNotes.ExperimentNotesField;

public class ExperimentNotesFieldExperimentSavedViewSubscriber extends ExperimentSwitchedViewSubscriber {

    private ExperimentNotesField experimentNotesField;
    private Consumer<String> saveConsumer;

    public ExperimentNotesFieldExperimentSavedViewSubscriber(ExperimentNotesField experimentNotesField, Consumer<String> saveConsumer) {
        super();
        this.experimentNotesField = experimentNotesField;
        this.saveConsumer = saveConsumer;
    }

    @Override
    public void handleBusEvent(ExperimentSwitchedViewBusEvent event) {
        saveConsumer.accept(experimentNotesField.getNotesText());
    }
}