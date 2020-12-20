package io.skymind.pathmind.webapp.ui.views.experiment.components.narbarItem.action;

import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.security.SecurityUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;

public class ExperimentCompareAction {
    public static void compare(Experiment experimentToCompare, ExperimentView experimentView, ExperimentDAO experimentDAO) {
        experimentView.showCompareExperimentComponents(true);
        Experiment comparisonExperiment = experimentDAO.getExperimentIfAllowed(experimentToCompare.getId(), SecurityUtils.getUserId()).get();
        experimentView.setComparisonExperiment(comparisonExperiment);
    }
}
