package io.skymind.pathmind.webapp.ui.views.experiment.actions.experiment;

import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.db.dao.PolicyDAO;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.security.SecurityUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;

public class CompareExperimentAction {

    private ExperimentView experimentView;
    private ExperimentDAO experimentDAO;
    private PolicyDAO policyDAO;

    public CompareExperimentAction(ExperimentView experimentView, ExperimentDAO experimentDAO, PolicyDAO policyDAO) {
        this.experimentView = experimentView;
        this.experimentDAO = experimentDAO;
        this.policyDAO = policyDAO;
    }

    public void compare(Experiment experiment) {
        experimentView.showCompareExperimentComponents(true);
        Experiment comparisonExperiment = experimentDAO.getExperimentIfAllowed(experiment.getId(), SecurityUtils.getUserId()).get();
        comparisonExperiment.setPolicies(policyDAO.getPoliciesForExperiment(experiment.getId()));
        experimentView.getComparisonComponents().forEach(component -> component.setExperiment(comparisonExperiment));
    }
}
