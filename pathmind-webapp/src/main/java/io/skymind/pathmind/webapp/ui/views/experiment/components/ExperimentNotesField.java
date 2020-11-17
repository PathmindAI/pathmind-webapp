package io.skymind.pathmind.webapp.ui.views.experiment.components;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.vaadin.flow.component.UI;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.webapp.ui.components.molecules.NotesField;

public class ExperimentNotesField extends NotesField {

    private Supplier<Optional<UI>> getUISupplier;
    private Experiment experiment;

    public ExperimentNotesField(Supplier<Optional<UI>> getUISupplier, String title, Experiment experiment, Consumer<String> saveConsumer) {
        super(title, experiment.getUserNotes(), saveConsumer);
        this.getUISupplier = getUISupplier;
        this.experiment = experiment;
    }

    public ExperimentNotesField(Supplier<Optional<UI>> getUISupplier, String title, Experiment experiment, Consumer<String> saveConsumer, Boolean allowAutoSave, Boolean hideSaveButton) {
        super(title, experiment.getUserNotes(), saveConsumer, false, allowAutoSave, hideSaveButton);
        this.getUISupplier = getUISupplier;
        this.experiment = experiment;
    }

    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
    }

    public Experiment getExperiment() {
        return experiment;
    }

}
