package io.skymind.pathmind.webapp.ui.views.experiment.components.chart.subscribers.main;

import io.skymind.pathmind.shared.utils.ExperimentUtils;
import io.skymind.pathmind.shared.utils.PolicyUtils;
import io.skymind.pathmind.webapp.bus.events.main.PolicyUpdateBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.main.PolicyUpdateSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.components.chart.CompareMetricsChartPanel;

public class CompareMetricsChartPanelPolicyUpdateSubscriber extends PolicyUpdateSubscriber {

    private CompareMetricsChartPanel compareMetricsChartPanel;

    public CompareMetricsChartPanelPolicyUpdateSubscriber(CompareMetricsChartPanel compareMetricsChartPanel) {
        super();
        this.compareMetricsChartPanel = compareMetricsChartPanel;
    }

    @Override
    public void handleBusEvent(PolicyUpdateBusEvent event) {
        synchronized (compareMetricsChartPanel.getExperimentLock()) {
            // We need to check after the lock is acquired as changing experiments can take up to several seconds.
            if (event.getExperimentId() != compareMetricsChartPanel.getExperimentId()) {
                return;
            }

            // TODO -> STEPH -> This should all be done in one place with the main susbcriber on the view and it updates all the components through the view
            // with view.setExperiment() which propogates.
            ExperimentUtils.addOrUpdatePolicies(compareMetricsChartPanel.getExperiment(), event.getPolicies());
            PolicyUtils.updateBestPolicy(compareMetricsChartPanel.getExperiment());
            compareMetricsChartPanel.updateChart();
        }
    }

    @Override
    public boolean filterBusEvent(PolicyUpdateBusEvent event) {
        return compareMetricsChartPanel.getExperimentId() == event.getExperimentId();
    }
}
