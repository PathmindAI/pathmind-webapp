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

                // Archive the experiment.
                ExperimentGuiUtils.archiveExperiment(abstractExperimentView.getExperimentDAO(), experiment, true);
                abstractExperimentView.getSegmentIntegrator().archived(Experiment.class, true);

                // Test to see if it's under comparison mode
                if (abstractExperimentView instanceof ExperimentView &&
                        ((ExperimentView) abstractExperimentView).isComparisonMode()) {
                    Experiment comparisonExperiment = ((ExperimentView) abstractExperimentView).getComparisonExperiment();
                    ((ExperimentView) abstractExperimentView).leaveComparisonMode();
                    if (ExperimentUtils.isSameExperiment(experiment, abstractExperimentView.getExperiment())) {
                        // If the current experiment is archived, set the comparison experiment as current experiment
                        abstractExperimentView.setExperiment(comparisonExperiment);
                        experimentsNavBar.removeExperiment(experiment);
                        experimentsNavBar.setCurrentExperiment(comparisonExperiment);
                    } else if (ExperimentUtils.isSameExperiment(experiment, comparisonExperiment)) {
                        // If the comparison experiment is archived
                        experimentsNavBar.removeExperiment(comparisonExperiment);
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
