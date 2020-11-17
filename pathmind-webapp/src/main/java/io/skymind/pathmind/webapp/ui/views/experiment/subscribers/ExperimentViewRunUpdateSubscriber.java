package io.skymind.pathmind.webapp.ui.views.experiment.subscribers;

import io.skymind.pathmind.shared.data.Experiment;
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
        if (isSameExperiment(event)) {
            experimentView.getExperiment().setTrainingStatusEnum(event.getExperiment().getTrainingStatusEnum());
            ExperimentUtils.addOrUpdateRuns(experimentView.getExperiment(), event.getRuns());
            ExperimentUtils.updatedRunsForPolicies(experimentView.getExperiment(), event.getRuns());
            experimentView.updateDetailsForExperiment();
            experimentView.updateButtonEnablement();
        } else if (ExperimentUtils.isNewExperimentForModel(event.getExperiment(), experimentView.getExperiments(), experimentView.getExperiment().getModelId())) {
            experimentView.updateExperimentComponents();
        }
    }

    @Override
    public boolean filterBusEvent(RunUpdateBusEvent event) {
        return isSameExperiment(event) || (!experimentView.getExperiment().isArchived() && ExperimentUtils.isSameModel(experimentView.getExperiment(), event.getModelId()));
    }

    private boolean isSameExperiment(RunUpdateBusEvent event) {
        return ExperimentUtils.isSameExperiment(experimentView.getExperiment(), event.getExperiment());
    }
}
