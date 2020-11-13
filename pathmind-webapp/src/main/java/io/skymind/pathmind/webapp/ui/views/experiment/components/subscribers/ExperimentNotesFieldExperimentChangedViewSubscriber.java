package io.skymind.pathmind.webapp.ui.views.experiment.components.subscribers;

import io.skymind.pathmind.webapp.bus.events.view.ExperimentChangedViewBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.view.ExperimentChangedViewSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.components.ExperimentNotesField;

public class ExperimentNotesFieldExperimentChangedViewSubscriber extends ExperimentChangedViewSubscriber {

    private ExperimentNotesField experimentNotesField;

    public ExperimentNotesFieldExperimentChangedViewSubscriber(ExperimentNotesField experimentNotesField) {
        super();
        this.experimentNotesField = experimentNotesField;
    }

    @Override
    public void handleBusEvent(ExperimentChangedViewBusEvent event) {
        System.out.println("--------- Experiment Changed! ---------");
        System.out.println("event.getExperiment().getId(): "+event.getExperiment().getId());
        System.out.println("event.getExperiment().getUserNotes(): "+event.getExperiment().getUserNotes());
        experimentNotesField.setNotesText(event.getExperiment().getUserNotes());
    }

}