package io.skymind.pathmind.webapp.ui.views.experiment.components.experimentNotes;

import java.util.Optional;
import java.util.function.Supplier;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveObserver;
import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.webapp.bus.EventBus;
import io.skymind.pathmind.webapp.bus.events.view.experiment.ExperimentChangedViewBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.EventBusSubscriberComponent;
import io.skymind.pathmind.webapp.ui.components.molecules.NotesField;
import io.skymind.pathmind.webapp.ui.views.experiment.components.ExperimentComponent;

// TODO -> STEPH -> Delete view/main subscriber folders that are no longer needed.
public class ExperimentNotesField extends NotesField implements BeforeLeaveObserver, EventBusSubscriberComponent, ExperimentComponent {

    private Supplier<Optional<UI>> getUISupplier;

    private Experiment experiment;

    public ExperimentNotesField(Supplier<Optional<UI>> getUISupplier, ExperimentDAO experimentDAO, Runnable segmentIntegratorRunnable, Boolean allowAutoSave, Boolean hideSaveButton) {
        super("Notes",
                "",
                null,
                false,
                allowAutoSave,
                hideSaveButton);
        this.getUISupplier = getUISupplier;
        setSaveConsumer(updatedNotes -> {
            experimentDAO.updateUserNotes(getExperiment().getId(), updatedNotes);
            segmentIntegratorRunnable.run();
        });
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
    public void beforeLeave(BeforeLeaveEvent event) {
        // TODO -> STEPH -> Push to the view as this is really for the whole experiment.
        saveNotes();
    }

    @Override
    public Supplier<Optional<UI>> getUISupplier() {
        return getUISupplier;
    }

    @Override
    public void updateExperiment() {
        experiment.setUserNotes(getNotesText());
    }
}
