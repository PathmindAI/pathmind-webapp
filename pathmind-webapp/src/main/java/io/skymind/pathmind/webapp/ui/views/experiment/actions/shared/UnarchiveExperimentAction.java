package io.skymind.pathmind.webapp.ui.views.experiment.actions.shared;

import java.util.function.Supplier;

import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.webapp.data.utils.ExperimentGuiUtils;
import io.skymind.pathmind.webapp.ui.utils.ConfirmationUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.DefaultExperimentView;

public class UnarchiveExperimentAction {
    public static void unarchive(DefaultExperimentView experimentView, Supplier<Experiment> getExperimentSupplier, Supplier<Object> getLockSupplier) {
        ConfirmationUtils.unarchive("experiment", () -> {
            synchronized (getLockSupplier.get()) {
                ExperimentGuiUtils.archiveExperiment(experimentView.getExperimentDAO(), getExperimentSupplier.get(), false);
                experimentView.getSegmentIntegrator().archived(Experiment.class, false);
            }
            // There's no need to lock on the view because at that point it's a page reload.
            ExperimentGuiUtils.navigateToExperiment(experimentView.getUI(), getExperimentSupplier.get());
        });
    }
}
