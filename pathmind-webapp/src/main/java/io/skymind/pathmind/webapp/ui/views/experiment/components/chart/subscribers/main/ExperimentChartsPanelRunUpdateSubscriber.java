package io.skymind.pathmind.webapp.ui.views.experiment.components.chart.subscribers.main;

import io.skymind.pathmind.shared.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.bus.events.main.RunUpdateBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.main.RunUpdateSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.components.chart.ExperimentChartsPanel;

public class ExperimentChartsPanelRunUpdateSubscriber extends RunUpdateSubscriber {

    private ExperimentChartsPanel experimentChartsPanel;

    public ExperimentChartsPanelRunUpdateSubscriber(ExperimentChartsPanel experimentChartsPanel) {
        super();
        this.experimentChartsPanel = experimentChartsPanel;
    }

    @Override
    public void handleBusEvent(RunUpdateBusEvent event) {
        // TODO -> STEPH -> This should all be done in one place with the main susbcriber on the view and it updates all the components through the view
        // with view.setExperiment() which propogates.
        ExperimentUtils.addOrUpdateRuns(experimentChartsPanel.getExperiment(), event.getRuns());
        experimentChartsPanel.getExperiment().updateTrainingStatus();
        experimentChartsPanel.selectVisibleChart();
    }

    @Override
    public boolean filterBusEvent(RunUpdateBusEvent event) {
        return ExperimentUtils.isSameExperiment(experimentChartsPanel.getExperiment(), event.getExperiment());
    }
}