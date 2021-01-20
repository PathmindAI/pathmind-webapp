package io.skymind.pathmind.webapp.ui.views.experiment.components.chart.subscribers.main;

import io.skymind.pathmind.webapp.bus.events.main.PolicyUpdateBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.main.PolicyUpdateSubscriber;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.components.chart.HistogramChartPanel;

public class HistogramChartPanelPolicyUpdateSubscriber extends PolicyUpdateSubscriber {
    private HistogramChartPanel histogramChartPanel;

    public HistogramChartPanelPolicyUpdateSubscriber(HistogramChartPanel histogramChartPanel) {
        super();
        this.histogramChartPanel = histogramChartPanel;
    }

    @Override
    public void handleBusEvent(PolicyUpdateBusEvent event) {
        synchronized (histogramChartPanel.getExperimentLock()) {
            // We need to check after the lock is acquired as changing experiments can take up to several seconds.
            if (event.getExperimentId() != histogramChartPanel.getExperiment().getId()) {
                return;
            }
        }
        ExperimentUtils.addOrUpdatePolicies(histogramChartPanel.getExperiment(), event.getPolicies());
        histogramChartPanel.updateChart();
    }

    @Override
    public boolean filterBusEvent(PolicyUpdateBusEvent event) {
        return histogramChartPanel.getExperiment().getId() == event.getExperimentId();
    }
}
