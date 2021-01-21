package io.skymind.pathmind.webapp.ui.views.experiment.components.narbarItem.action;

import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.webapp.data.utils.ExperimentGuiUtils;
import io.skymind.pathmind.webapp.ui.utils.ConfirmationUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.AbstractExperimentView;
import io.skymind.pathmind.webapp.ui.views.experiment.components.navbar.ExperimentsNavBar;

public class NavBarItemArchiveExperimentAction {
    public static void archiveExperiment(Experiment experiment, ExperimentsNavBar experimentsNavBar, AbstractExperimentView abstractExperimentView) {
        ConfirmationUtils.archive("Experiment #" + experiment.getName(), () -> {
            synchronized (abstractExperimentView.getExperimentLock()) {
                ExperimentGuiUtils.archiveExperiment(abstractExperimentView.getExperimentDAO(), experiment, true);
                abstractExperimentView.getSegmentIntegrator().archived(Experiment.class, true);
                ExperimentGuiUtils.navigateToFirstUnarchivedOrModel(abstractExperimentView.getUISupplier(), experimentsNavBar.getExperiments());
            }
        });
    }
}
