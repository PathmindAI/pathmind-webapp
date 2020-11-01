package io.skymind.pathmind.webapp.ui.views.experiment.components;

import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.webapp.ui.components.molecules.NotesField;

import java.util.function.Consumer;

public class ExperimentNotesField extends NotesField {

    public ExperimentNotesField(String title, Experiment experiment, Consumer<String> saveConsumer) {
        super(title, experiment.getUserNotes(), saveConsumer);
    }
}
