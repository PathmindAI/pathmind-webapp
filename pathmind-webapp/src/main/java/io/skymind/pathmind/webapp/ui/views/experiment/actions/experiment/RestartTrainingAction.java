package io.skymind.pathmind.webapp.ui.views.experiment.actions.experiment;

import io.skymind.pathmind.db.dao.RunDAO;
import io.skymind.pathmind.services.TrainingService;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;
import io.skymind.pathmind.webapp.ui.views.experiment.utils.ExperimentCapLimitVerifier;

import java.util.function.Supplier;

public class RestartTrainingAction {
    public static void restartTraining(ExperimentView experimentView, Supplier<Experiment> getExperimentSupplier, Runnable updateExperimentViewRunnable, Supplier<Object> getLockSupplier, RunDAO runDAO, TrainingService trainingService) {
        if (!ExperimentCapLimitVerifier.isUserWithinCapLimits(runDAO, experimentView.getUserCaps(), experimentView.getSegmentIntegrator())) {
            return;
        }

        synchronized (getLockSupplier.get()) {
            trainingService.startRun(getExperimentSupplier.get());
            experimentView.getSegmentIntegrator().restartTraining();
            // Note that trainingService.startRun(experiment) alters the experiment instance and so it needs to be re-rendered but NOT reset the experiment itself.
            updateExperimentViewRunnable.run();
            // TODO -> TICKET -> https://github.com/SkymindIO/pathmind-webapp/issues/2598 Possible bug found? Why aren't we firing an event for the other browsers?
        }
    }
}
