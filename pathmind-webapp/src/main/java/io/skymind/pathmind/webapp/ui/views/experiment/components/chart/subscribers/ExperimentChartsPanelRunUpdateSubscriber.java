package io.skymind.pathmind.webapp.ui.views.experiment.components.chart.subscribers;

import io.skymind.pathmind.webapp.bus.events.main.RunUpdateBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.main.RunUpdateSubscriber;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.components.chart.ExperimentChartsPanel;

public class ExperimentChartsPanelRunUpdateSubscriber extends RunUpdateSubscriber {

    private ExperimentChartsPanel experimentChartsPanel;
    public ExperimentChartsPanelRunUpdateSubscriber(ExperimentChartsPanel experimentChartsPanel) {
        super();
        this.experimentChartsPanel = experimentChartsPanel;
    }

    @Override
    public void handleBusEvent(RunUpdateBusEvent event) {
        ExperimentUtils.addOrUpdateRuns(experimentChartsPanel.getExperiment(), event.getRuns());
        experimentChartsPanel.getExperiment().updateTrainingStatus();
        experimentChartsPanel.selectVisibleChart();
    }

    @Override
    public boolean filterBusEvent(RunUpdateBusEvent event) {
        return ExperimentUtils.isSameExperiment(experimentChartsPanel.getExperiment(), event.getExperiment());
    }
}