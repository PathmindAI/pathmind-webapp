package io.skymind.pathmind.webapp.ui.views.experiment.components.narbarItem.action;

import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.data.utils.ExperimentGuiUtils;
import io.skymind.pathmind.webapp.ui.utils.ConfirmationUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.AbstractExperimentView;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;
import io.skymind.pathmind.webapp.ui.views.experiment.components.navbar.ExperimentsNavBar;

public class NavBarItemArchiveExperimentAction {
    public static void archiveExperiment(Experiment experiment, ExperimentsNavBar experimentsNavBar, AbstractExperimentView abstractExperimentView) {
        ConfirmationUtils.archive("Experiment #" + experiment.getName(), () -> {
            synchronized (abstractExperimentView.getExperimentLock()) {

                // Archive the experiment.
                ExperimentGuiUtils.archiveExperiment(abstractExperimentView.getExperimentDAO(), experiment, true);
                abstractExperimentView.getSegmentIntegrator().archived(Experiment.class, true);

                // If it's the same then navigate to next unarchived otherwise update the navbar.
                if(ExperimentUtils.isSameExperiment(experiment, abstractExperimentView.getExperiment())) {
                    ExperimentGuiUtils.navigateToFirstUnarchivedOrModel(abstractExperimentView.getUISupplier(), experimentsNavBar.getExperiments());
                } else {
                    experimentsNavBar.updateExperiment(experiment);
                }

                // Test to see if it's a comparison experiment and if so close the comparison panel.
                if(abstractExperimentView instanceof ExperimentView &&
                        ((ExperimentView) abstractExperimentView).isComparisonMode() &&
                        ExperimentUtils.isSameExperiment(experiment, ((ExperimentView) abstractExperimentView).getComparisonExperiment())) {
                    ((ExperimentView) abstractExperimentView).leaveComparisonMode();
                }
            }
        });
    }
}
