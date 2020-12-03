package io.skymind.pathmind.webapp.ui.views.experiment.components.experimentNotes.subscribers.view;

import java.util.function.Consumer;

import io.skymind.pathmind.webapp.bus.events.main.ExperimentStartTrainingBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.main.ExperimentStartTrainingSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.components.experimentNotes.ExperimentNotesField;

public class ExperimentNotesFieldExperimentStartTrainingViewSubscriber extends ExperimentStartTrainingSubscriber {

    private ExperimentNotesField experimentNotesField;
    private Consumer<String> saveConsumer;

    public ExperimentNotesFieldExperimentStartTrainingViewSubscriber(ExperimentNotesField experimentNotesField, Consumer<String> saveConsumer) {
        super();
        this.experimentNotesField = experimentNotesField;
        this.saveConsumer = saveConsumer;
    }

    @Override
    public void handleBusEvent(ExperimentStartTrainingBusEvent event) {
        saveConsumer.accept(experimentNotesField.getNotesText());
    }
}