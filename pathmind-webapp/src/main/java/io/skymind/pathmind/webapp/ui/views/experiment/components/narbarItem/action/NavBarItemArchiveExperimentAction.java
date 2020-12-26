package io.skymind.pathmind.webapp.ui.views.experiment.components.narbarItem.action;

import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.webapp.data.utils.ExperimentGuiUtils;
import io.skymind.pathmind.webapp.ui.utils.ConfirmationUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.DefaultExperimentView;
import io.skymind.pathmind.webapp.ui.views.experiment.components.navbar.ExperimentsNavBar;

public class NavBarItemArchiveExperimentAction {
    public static void archiveExperiment(Experiment experiment, ExperimentDAO experimentDAO, ExperimentsNavBar experimentsNavBar, DefaultExperimentView defaultExperimentView) {
        ConfirmationUtils.archive("Experiment #" + experiment.getName(), () -> {
            synchronized (defaultExperimentView.getExperimentLock()) {
                ExperimentGuiUtils.archiveExperiment(experimentDAO, experiment, true);
                defaultExperimentView.getSegmentIntegrator().archived(Experiment.class, true);
                ExperimentGuiUtils.navigateToFirstUnarchivedOrModel(defaultExperimentView.getUISupplier(), experimentsNavBar.getExperiments());
            }
        });
    }
}
