package io.skymind.pathmind.webapp.ui.views.experiment.components.chart.subscribers;

import java.util.Optional;
import java.util.function.Supplier;

import com.vaadin.flow.component.UI;

import io.skymind.pathmind.webapp.bus.events.main.PolicyUpdateBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.main.PolicyUpdateSubscriber;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.components.chart.AllMetricsChartPanel;

public class AllMetricsChartPanelPolicyUpdateSubscriber extends PolicyUpdateSubscriber {

    private AllMetricsChartPanel allMetricsChartPanel;

    public AllMetricsChartPanelPolicyUpdateSubscriber(Supplier<Optional<UI>> getUISupplier, AllMetricsChartPanel llMetricsChartPanel) {
        super(getUISupplier);
        this.allMetricsChartPanel = allMetricsChartPanel;
    }

    @Override
    public void handleBusEvent(PolicyUpdateBusEvent event) {
        synchronized (allMetricsChartPanel.getExperimentLock()) {
            // We need to check after the lock is acquired as changing experiments can take up to several seconds.
            if (event.getExperimentId() != allMetricsChartPanel.getExperimentId())
                return;

            ExperimentUtils.addOrUpdatePolicies(allMetricsChartPanel.getExperiment(), event.getPolicies());
            allMetricsChartPanel.selectBestPolicy();
            allMetricsChartPanel.pushChartUpdate(getUiSupplier());
        }
    }

    @Override
    public boolean filterBusEvent(PolicyUpdateBusEvent event) {
        return allMetricsChartPanel.getExperimentId() == event.getExperimentId();
    }
}
