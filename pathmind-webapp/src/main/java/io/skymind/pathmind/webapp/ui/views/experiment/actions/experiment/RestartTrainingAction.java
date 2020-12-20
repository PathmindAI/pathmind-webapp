package io.skymind.pathmind.webapp.ui.views.experiment.actions.experiment;

import io.skymind.pathmind.db.dao.RunDAO;
import io.skymind.pathmind.services.TrainingService;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;
import io.skymind.pathmind.webapp.ui.views.experiment.utils.ExperimentCapLimitVerifier;

public class RestartTrainingAction {

    public static void restartTraining(ExperimentView experimentView, RunDAO runDAO, TrainingService trainingService) {
        if (!ExperimentCapLimitVerifier.isUserWithinCapLimits(runDAO, experimentView.getUserCaps(), experimentView.getSegmentIntegrator())) {
            return;
        }
        Experiment experiment = experimentView.getExperiment();
        trainingService.startRun(experiment);
        experimentView.getSegmentIntegrator().restartTraining();
        // IMPORTANT -> Note that trainingService.startRun(experiment) alters the experiment instance and so it needs to be reloaded. The only question
        // is whether we need to reload everything or if we can just reload some parts. Due to time constraints we're going to reload everything for now
        // through the setExperiment() instead of just using updateDetailsForExperiment().
        experimentView.setExperiment(experiment);
        // TODO -> TICKET -> https://github.com/SkymindIO/pathmind-webapp/issues/2598 Possible bug found? Why aren't we firing an event for the other browsers?
    }
}
