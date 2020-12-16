package io.skymind.pathmind.webapp.ui.views.experiment.actions.shared;

import java.util.Optional;

import com.vaadin.flow.component.UI;
import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.webapp.data.utils.ExperimentGuiUtils;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.utils.ConfirmationUtils;

public class UnarchiveExperimentAction {
    public static void unarchiveExperiment(ExperimentDAO experimentDAO, Experiment experiment, SegmentIntegrator segmentIntegrator, Optional<UI> ui) {
        ConfirmationUtils.unarchive("experiment", () -> {
            ExperimentGuiUtils.archiveExperiment(experimentDAO, experiment, false);
            segmentIntegrator.archived(Experiment.class, false);
            ExperimentGuiUtils.navigateToExperiment(ui, experiment);
        });
    }
}
