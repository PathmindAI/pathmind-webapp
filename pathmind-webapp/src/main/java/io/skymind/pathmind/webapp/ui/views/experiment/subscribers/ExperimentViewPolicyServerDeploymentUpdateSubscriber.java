package io.skymind.pathmind.webapp.ui.views.experiment.subscribers;

import io.skymind.pathmind.shared.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.bus.events.main.PolicyServerDeploymentUpdateBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.main.ExperimentPolicyServerDeploymentUpdateSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExperimentViewPolicyServerDeploymentUpdateSubscriber extends ExperimentPolicyServerDeploymentUpdateSubscriber {

    private ExperimentView experimentView;

    public ExperimentViewPolicyServerDeploymentUpdateSubscriber(ExperimentView experimentView) {
        super();
        this.experimentView = experimentView;
    }

    @Override
    public void handleBusEvent(PolicyServerDeploymentUpdateBusEvent event) {
        if (ExperimentUtils.isSameExperiment(experimentView.getExperiment(), event.getExperiment())) {
            synchronized (experimentView.getExperimentLock()) {
                experimentView.getExperimentTitleBar().setExperimentForServePolicyButton(experimentView.getExperiment());
            }
        } else {
            synchronized (experimentView.getComparisonExperimentLock()) {
                experimentView.getComparisonTitleBar().setExperimentForServePolicyButton(experimentView.getExperiment());
            }
        }
    }

    @Override
    public boolean filterBusEvent(PolicyServerDeploymentUpdateBusEvent event) {
        return ExperimentUtils.isSameExperiment(experimentView.getExperiment(), event.getExperiment()) ||
                ExperimentUtils.isSameExperiment(experimentView.getComparisonExperiment(), event.getExperiment());
    }
}
