package io.skymind.pathmind.webapp.ui.views.experiment.components.experimentNotes.subscribers.view;

import java.util.function.Consumer;

import io.skymind.pathmind.webapp.bus.events.view.ExperimentSwitchedViewBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.view.ExperimentSwitchedViewSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.components.experimentNotes.ExperimentNotesField;

public class ExperimentNotesFieldExperimentSwitchedViewSubscriber extends ExperimentSwitchedViewSubscriber {

    private ExperimentNotesField experimentNotesField;
    private Consumer<String> saveConsumer;

    public ExperimentNotesFieldExperimentSwitchedViewSubscriber(ExperimentNotesField experimentNotesField, Consumer<String> saveConsumer) {
        super();
        this.experimentNotesField = experimentNotesField;
        this.saveConsumer = saveConsumer;
    }

    @Override
    public void handleBusEvent(ExperimentSwitchedViewBusEvent event) {
        saveConsumer.accept(experimentNotesField.getNotesText());
        experimentNotesField.setExperiment(event.getExperiment());
        experimentNotesField.setNotesText(event.getExperiment().getUserNotes());
    }

}