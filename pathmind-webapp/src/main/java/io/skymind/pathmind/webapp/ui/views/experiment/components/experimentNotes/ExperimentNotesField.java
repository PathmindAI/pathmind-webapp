package io.skymind.pathmind.webapp.ui.views.experiment.components.experimentNotes;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.webapp.bus.EventBus;
import io.skymind.pathmind.webapp.bus.events.view.ExperimentChangedViewBusEvent;
import io.skymind.pathmind.webapp.ui.components.molecules.NotesField;
import io.skymind.pathmind.webapp.ui.views.experiment.components.experimentNotes.subscribers.view.ExperimentNotesFieldExperimentSwitchedViewSubscriber;

public class ExperimentNotesField extends NotesField {

    private Supplier<Optional<UI>> getUISupplier;

    private Experiment experiment;

    public ExperimentNotesField(Supplier<Optional<UI>> getUISupplier, String title, Experiment experiment, Consumer<String> saveConsumer) {
        this(getUISupplier, title, experiment, saveConsumer, false, false);
    }

    public ExperimentNotesField(Supplier<Optional<UI>> getUISupplier, String title, Experiment experiment, Consumer<String> saveConsumer, Boolean allowAutoSave, Boolean hideSaveButton) {
        super(title, experiment.getUserNotes(), saveConsumer, false, allowAutoSave, hideSaveButton);
        this.getUISupplier = getUISupplier;
        this.experiment = experiment.shallowClone();
        setOnNotesChangeHandler(() -> EventBus.post(new ExperimentChangedViewBusEvent(experiment)));
    }

    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
    }

    public Experiment getExperiment() {
        return experiment;
    }

    @Override
    protected void onDetach(DetachEvent event) {
        EventBus.unsubscribe(this);
    }

    @Override
    protected void onAttach(AttachEvent event) {
        EventBus.subscribe(this, getUISupplier,
                new ExperimentNotesFieldExperimentSwitchedViewSubscriber(this));
    }
}
