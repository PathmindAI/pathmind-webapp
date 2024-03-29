package io.skymind.pathmind.webapp.ui.views.experiment.components.simple.shared;

import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.webapp.ui.components.navigation.Breadcrumbs;
import io.skymind.pathmind.webapp.ui.views.experiment.components.ExperimentComponent;

public class ExperimentBreadcrumbs extends Breadcrumbs implements ExperimentComponent {

    public ExperimentBreadcrumbs(Experiment experiment) {
        super(experiment.getProject(), experiment.getModel(), experiment);
    }

    public void setExperiment(Experiment experiment) {
        setText(3, "Experiment #" + experiment.getName());
    }
}
