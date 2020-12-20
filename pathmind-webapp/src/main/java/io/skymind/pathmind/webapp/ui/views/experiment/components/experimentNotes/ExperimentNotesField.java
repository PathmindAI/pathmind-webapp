package io.skymind.pathmind.webapp.ui.views.experiment.components.experimentNotes;

import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.webapp.bus.EventBus;
import io.skymind.pathmind.webapp.bus.events.view.experiment.ExperimentNeedsSavingViewBusEvent;
import io.skymind.pathmind.webapp.ui.components.molecules.NotesField;
import io.skymind.pathmind.webapp.ui.views.experiment.components.ExperimentComponent;

public class ExperimentNotesField extends NotesField implements ExperimentComponent {

    private Experiment experiment;

    public ExperimentNotesField(ExperimentDAO experimentDAO, Runnable segmentIntegratorRunnable, Boolean allowAutoSave, Boolean hideSaveButton) {
        super("Notes",
                "",
                null,
                false,
                allowAutoSave,
                hideSaveButton);
        setSaveConsumer(updatedNotes -> {
            experimentDAO.updateUserNotes(getExperiment().getId(), updatedNotes);
            segmentIntegratorRunnable.run();
        });
        setOnNotesChangeHandler(() -> EventBus.post(new ExperimentNeedsSavingViewBusEvent(experiment)));
    }

    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
        setNotesText(experiment.getUserNotes());
    }

    public Experiment getExperiment() {
        return experiment;
    }

    @Override
    public void updateExperiment() {
        experiment.setUserNotes(getNotesText());
    }
}
