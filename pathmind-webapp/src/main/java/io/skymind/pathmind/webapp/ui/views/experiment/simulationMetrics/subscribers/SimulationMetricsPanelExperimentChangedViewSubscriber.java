package io.skymind.pathmind.webapp.ui.views.experiment.simulationMetrics.subscribers;

import com.vaadin.flow.component.UI;
import io.skymind.pathmind.webapp.bus.events.view.ExperimentChangedViewBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.view.ExperimentChangedViewSubscriber;
import io.skymind.pathmind.webapp.ui.utils.PushUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.simulationMetrics.SimulationMetricsPanel;

import java.util.Optional;
import java.util.function.Supplier;

public class SimulationMetricsPanelExperimentChangedViewSubscriber extends ExperimentChangedViewSubscriber {

    private SimulationMetricsPanel simulationMetricsPanel;

    public SimulationMetricsPanelExperimentChangedViewSubscriber(Supplier<Optional<UI>> getUISupplier, SimulationMetricsPanel simulationMetricsPanel) {
        super(getUISupplier);
        this.simulationMetricsPanel = simulationMetricsPanel;
    }

    @Override
    public void handleBusEvent(ExperimentChangedViewBusEvent event) {
        PushUtils.push(getUiSupplier(), () ->
                simulationMetricsPanel.setExperiment(event.getExperiment()));
    }
}
