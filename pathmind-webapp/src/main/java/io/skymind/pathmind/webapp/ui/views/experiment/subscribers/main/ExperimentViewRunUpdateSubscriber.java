package io.skymind.pathmind.webapp.ui.views.experiment.subscribers.main;

import io.skymind.pathmind.webapp.bus.events.main.RunUpdateBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.main.RunUpdateSubscriber;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExperimentViewRunUpdateSubscriber extends RunUpdateSubscriber {

    private ExperimentView experimentView;

    public ExperimentViewRunUpdateSubscriber(ExperimentView experimentView) {
        super();
        this.experimentView = experimentView;
    }

    @Override
    public void handleBusEvent(RunUpdateBusEvent event) {
        experimentView.getExperiment().setTrainingStatusEnum(event.getExperiment().getTrainingStatusEnum());
        ExperimentUtils.addOrUpdateRuns(experimentView.getExperiment(), event.getRuns());
        ExperimentUtils.updatedRunsForPolicies(experimentView.getExperiment(), event.getRuns());
        experimentView.updateDetailsForExperiment();
        experimentView.updateButtonEnablement();
    }

    @Override
    public boolean filterBusEvent(RunUpdateBusEvent event) {
        return isSameExperiment(event) || (!experimentView.getExperiment().isArchived());
    }

    private boolean isSameExperiment(RunUpdateBusEvent event) {
        return ExperimentUtils.isSameExperiment(experimentView.getExperiment(), event.getExperiment());
    }
}
