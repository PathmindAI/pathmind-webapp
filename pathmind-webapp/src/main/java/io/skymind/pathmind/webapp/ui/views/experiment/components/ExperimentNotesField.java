package io.skymind.pathmind.webapp.ui.views.experiment.components;

import java.util.function.Consumer;

import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.webapp.ui.components.molecules.NotesField;

public class ExperimentNotesField extends NotesField {

    private Experiment experiment;

    public ExperimentNotesField(String title, Experiment experiment, Consumer<String> saveConsumer) {
        super(title, experiment.getUserNotes(), saveConsumer);
        this.experiment = experiment;
    }

    public ExperimentNotesField(String title, Experiment experiment, Consumer<String> saveConsumer, Boolean allowAutoSave, Boolean hideSaveButton) {
        super(title, experiment.getUserNotes(), saveConsumer, false, allowAutoSave, hideSaveButton);
        this.experiment = experiment;
    }

    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
    }

    public Experiment getExperiment() {
        return experiment;
    }

}
