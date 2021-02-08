package io.skymind.pathmind.webapp.ui.views.experiment.actions.shared;

import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.data.utils.ExperimentGuiUtils;
import io.skymind.pathmind.webapp.ui.utils.ConfirmationUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.AbstractExperimentView;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;

public class ArchiveExperimentAction {
    public static void archive(AbstractExperimentView abstractExperimentView) {
        Experiment experiment = abstractExperimentView.getExperiment();
        ConfirmationUtils.archive("Experiment #" + experiment.getName(), () -> {
            synchronized (abstractExperimentView.getExperimentLock()) {

                // Archive the experiment.
                ExperimentGuiUtils.archiveExperiment(abstractExperimentView.getExperimentDAO(), experiment, true);
                abstractExperimentView.getSegmentIntegrator().archived(Experiment.class, true);

                // Navigate to next unarchived experiment.
                ExperimentGuiUtils.navigateToFirstUnarchivedOrModel(abstractExperimentView.getUISupplier(), abstractExperimentView.getNavbarExperiments());

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
