package io.skymind.pathmind.webapp.ui.views.experiment.components.narbarItem.action;

import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.security.SecurityUtils;
import io.skymind.pathmind.webapp.ui.utils.NotificationUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;

public class NavBarItemCompareExperimentAction {
    public static void compare(Experiment experimentToCompare, ExperimentView experimentView) {

        if (experimentToCompare.isDraft()) {
            NotificationUtils.showError("Cannot compare draft experiment<br>Option shouldn't be available rather than error message");
            return;
        }

        experimentView.showCompareExperimentComponents(true);
        Experiment comparisonExperiment = experimentView.getExperimentDAO().getExperimentIfAllowed(experimentToCompare.getId(), SecurityUtils.getUserId()).get();
        experimentView.setComparisonExperiment(comparisonExperiment);
    }
}
