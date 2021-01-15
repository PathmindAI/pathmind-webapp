package io.skymind.pathmind.webapp.ui.views.experiment.components.experimentNotes;

import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.webapp.ui.components.molecules.NotesField;
import io.skymind.pathmind.webapp.ui.views.experiment.DefaultExperimentView;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;
import io.skymind.pathmind.webapp.ui.views.experiment.NewExperimentView;
import io.skymind.pathmind.webapp.ui.views.experiment.actions.newExperiment.NeedsSavingAction;
import io.skymind.pathmind.webapp.ui.views.experiment.components.ExperimentComponent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExperimentNotesField extends NotesField implements ExperimentComponent {

    private Experiment experiment;

    public ExperimentNotesField(DefaultExperimentView defaultExperimentView, ExperimentDAO experimentDAO, Runnable segmentIntegratorRunnable, Boolean allowAutoSave, Boolean hideSaveButton) {
        super("Notes",
                "",
                null,
                false,
                allowAutoSave,
                hideSaveButton);

        switch (defaultExperimentView.getViewUrl()) {
            case Routes.NEW_EXPERIMENT_URL: {
                setOnNotesChangeHandler(() -> NeedsSavingAction.setNeedsSaving((NewExperimentView)defaultExperimentView));
                break;
            }
            case Routes.EXPERIMENT_URL:
            case Routes.SHARED_EXPERIMENT: {
                setSaveConsumer(updatedNotes -> {
                    experimentDAO.updateUserNotes(getExperiment().getId(), updatedNotes);
                    segmentIntegratorRunnable.run();
                });
                break;
            }
            default:
                log.warn("Unknown view to process {}", defaultExperimentView.getClass().getSimpleName());
        }
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
