package io.skymind.pathmind.webapp.ui.views.experiment.actions.newExperiment;

import io.skymind.pathmind.db.dao.ObservationDAO;
import io.skymind.pathmind.db.dao.RunDAO;
import io.skymind.pathmind.services.TrainingService;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.webapp.bus.EventBus;
import io.skymind.pathmind.webapp.bus.events.main.ExperimentStartTrainingBusEvent;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;
import io.skymind.pathmind.webapp.ui.views.experiment.NewExperimentView;
import io.skymind.pathmind.webapp.ui.views.experiment.components.rewardFunction.RewardFunctionEditor;
import io.skymind.pathmind.webapp.ui.views.experiment.utils.ExperimentCapLimitVerifier;

public class StartRunAction {

    public static void startRun(NewExperimentView newExperimentView, RewardFunctionEditor rewardFunctionEditor, TrainingService trainingService, RunDAO runDAO, ObservationDAO observationDAO) {

        if (!rewardFunctionEditor.validateBinder()) {
            return;
        }
        if (!ExperimentCapLimitVerifier.isUserWithinCapLimits(runDAO, newExperimentView.getUserCaps(), newExperimentView.getSegmentIntegrator())) {
            return;
        }

        // These two are an exception to the eventbus because we need to save and run rather than just run. Once we recombine things
        // after finishing all the refactorings this will be cleaner. The notes can be saved normally because the run doesn't
        // rely on the information in the notes.
        Experiment experiment = newExperimentView.getUpdatedExperiment();

        newExperimentView.getExperimentDAO().updateRewardFunction(experiment);
        observationDAO.saveExperimentObservations(experiment.getId(), experiment.getSelectedObservations());

        trainingService.startRun(experiment);
        EventBus.post(new ExperimentStartTrainingBusEvent(experiment));
        newExperimentView.getSegmentIntegrator().startTraining();

        newExperimentView.setUnsavedChangesLabel(false);

        // Remove the isNeedsSaving toggle in the NewExperimentView so that the automatic saving mechanism from beforeLeave is not triggered.
        newExperimentView.removeNeedsSaving();
        newExperimentView.getUISupplier().get().ifPresent(ui -> ui.navigate(ExperimentView.class, experiment.getId()));
    }
}
