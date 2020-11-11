package io.skymind.pathmind.webapp.ui.views.experiment.components.chart.subscribers;

import io.skymind.pathmind.webapp.bus.events.view.ExperimentChangedViewBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.view.ExperimentChangedViewSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.components.chart.ExperimentChartsPanel;

public class ExperimentChartsPanelExperimentChangedViewSubscriber extends ExperimentChangedViewSubscriber {

    private ExperimentChartsPanel experimentChartsPanel;

    public ExperimentChartsPanelExperimentChangedViewSubscriber(ExperimentChartsPanel experimentChartsPanel) {
        super();
        this.experimentChartsPanel = experimentChartsPanel;
    }

    @Override
    public void handleBusEvent(ExperimentChangedViewBusEvent event) {
        experimentChartsPanel.setupCharts(event.getExperiment(), experimentChartsPanel.getRewardVariables());
    }
}
