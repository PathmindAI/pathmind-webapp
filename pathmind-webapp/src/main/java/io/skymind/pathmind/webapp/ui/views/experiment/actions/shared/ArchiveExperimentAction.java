package io.skymind.pathmind.webapp.ui.views.experiment.actions.shared;

import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.data.utils.ExperimentGuiUtils;
import io.skymind.pathmind.webapp.ui.utils.ConfirmationUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.AbstractExperimentView;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;
import io.skymind.pathmind.webapp.ui.views.experiment.components.navbar.ExperimentsNavBar;

public class ArchiveExperimentAction {
    public static void archive(Experiment experiment, AbstractExperimentView abstractExperimentView) {
        ConfirmationUtils.archive("Experiment #" + experiment.getName(), () -> {
            synchronized (abstractExperimentView.getExperimentLock()) {

                ExperimentsNavBar experimentsNavBar = abstractExperimentView.getExperimentsNavbar();
                // The experiment passed by reference here is from the Experiment / New Experiment View
                // so we need to set the equivalent experiment in the experimentsNavBar list to use the same reference
                experimentsNavBar.setExperiment(experiment);

                // Archive the experiment.
                ExperimentGuiUtils.archiveExperiment(abstractExperimentView.getExperimentDAO(), experiment, true);
                abstractExperimentView.getSegmentIntegrator().archived(Experiment.class, true);

                // Test to see if it's under comparison mode on ExperimentView
                if (abstractExperimentView instanceof ExperimentView &&
                        ((ExperimentView) abstractExperimentView).isComparisonMode()) {

                    Experiment comparisonExperiment = ((ExperimentView) abstractExperimentView).getComparisonExperiment();

                    ((ExperimentView) abstractExperimentView).leaveComparisonMode();
                    experimentsNavBar.updateExperiment(experiment);

                    if (ExperimentUtils.isSameExperiment(experiment, abstractExperimentView.getExperiment())) {
                        // If the current experiment is archived, set the comparison experiment as current experiment
                        abstractExperimentView.setExperiment(comparisonExperiment);
                        experimentsNavBar.setCurrentExperiment(comparisonExperiment);
                    }
                } else if (ExperimentUtils.isSameExperiment(experiment, abstractExperimentView.getExperiment())) {
                    // If it's not under comparison mode and is the same experiment then navigate to next unarchived otherwise update the navbar.
                    ExperimentGuiUtils.navigateToFirstUnarchivedOrModel(abstractExperimentView.getUISupplier(), experimentsNavBar.getExperiments());
                } else {
                    experimentsNavBar.updateExperiment(experiment);
                }
            }
        });
    }
}
