package io.skymind.pathmind.webapp.ui.views.experiment.components.experimentNotes;

import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.webapp.ui.components.molecules.NotesField;
import io.skymind.pathmind.webapp.ui.views.experiment.DefaultExperimentView;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;
import io.skymind.pathmind.webapp.ui.views.experiment.NewExperimentView;
import io.skymind.pathmind.webapp.ui.views.experiment.actions.newExperiment.NeedsSavingAction;
import io.skymind.pathmind.webapp.ui.views.experiment.components.ExperimentComponent;

public class ExperimentNotesField extends NotesField implements ExperimentComponent {

    private Experiment experiment;

    public ExperimentNotesField(DefaultExperimentView defaultExperimentView, ExperimentDAO experimentDAO, Runnable segmentIntegratorRunnable, Boolean allowAutoSave, Boolean hideSaveButton) {
        super("Notes",
                "",
                null,
                false,
                allowAutoSave,
                hideSaveButton);

        if (defaultExperimentView instanceof ExperimentView) {
            setSaveConsumer(updatedNotes -> {
                experimentDAO.updateUserNotes(getExperiment().getId(), updatedNotes);
                segmentIntegratorRunnable.run();
            });
        }

        if (defaultExperimentView instanceof NewExperimentView) {
            setOnNotesChangeHandler(() -> NeedsSavingAction.setNeedsSaving((NewExperimentView) defaultExperimentView));
        }
    }

    public Experiment getExperiment() {
        return experiment;
    }

    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
        setNotesText(experiment.getUserNotes());
    }

    @Override
    public void updateExperiment() {
        experiment.setUserNotes(getNotesText());
    }
}
