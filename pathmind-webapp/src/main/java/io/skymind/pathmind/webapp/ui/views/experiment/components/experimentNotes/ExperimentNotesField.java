package io.skymind.pathmind.webapp.ui.views.experiment.components.experimentNotes;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveObserver;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.webapp.bus.EventBus;
import io.skymind.pathmind.webapp.bus.events.view.experiment.ExperimentChangedViewBusEvent;
import io.skymind.pathmind.webapp.ui.components.molecules.NotesField;
import io.skymind.pathmind.webapp.ui.views.experiment.components.experimentNotes.subscribers.view.ExperimentNotesFieldExperimentSavedViewSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.components.experimentNotes.subscribers.view.ExperimentNotesFieldExperimentStartTrainingViewSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.components.experimentNotes.subscribers.view.ExperimentNotesFieldExperimentSwitchedViewSubscriber;

public class ExperimentNotesField extends NotesField  implements BeforeLeaveObserver {

    private Supplier<Optional<UI>> getUISupplier;
    private Consumer<String> saveConsumer;

    private Experiment experiment;

    public ExperimentNotesField(Supplier<Optional<UI>> getUISupplier, Experiment experiment, Consumer<String> saveConsumer, Boolean allowAutoSave, Boolean hideSaveButton) {
        super("Notes", experiment.getUserNotes(), saveConsumer, false, allowAutoSave, hideSaveButton);
        this.getUISupplier = getUISupplier;
        this.saveConsumer = saveConsumer;
        this.experiment = experiment.shallowClone();
        setOnNotesChangeHandler(() -> EventBus.post(new ExperimentChangedViewBusEvent(experiment)));
    }

    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
        setNotesText(experiment.getUserNotes());
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
                new ExperimentNotesFieldExperimentStartTrainingViewSubscriber(this, saveConsumer),
                new ExperimentNotesFieldExperimentSwitchedViewSubscriber(this, saveConsumer),
                new ExperimentNotesFieldExperimentSavedViewSubscriber(this, saveConsumer));
    }

    @Override
    public void beforeLeave(BeforeLeaveEvent event) {
        saveNotes();
    }
}
