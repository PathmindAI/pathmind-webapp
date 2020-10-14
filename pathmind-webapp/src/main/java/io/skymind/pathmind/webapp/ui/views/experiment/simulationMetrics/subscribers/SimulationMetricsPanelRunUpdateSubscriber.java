package io.skymind.pathmind.webapp.ui.views.experiment.simulationMetrics.subscribers;

import com.vaadin.flow.component.UI;
import io.skymind.pathmind.shared.constants.RunStatus;
import io.skymind.pathmind.webapp.bus.events.RunUpdateBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.RunUpdateSubscriber;
import io.skymind.pathmind.webapp.ui.utils.PushUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.simulationMetrics.SimulationMetricsPanel;

import java.util.Optional;
import java.util.function.Supplier;

public class SimulationMetricsPanelRunUpdateSubscriber extends RunUpdateSubscriber {

    private SimulationMetricsPanel simulationMetricsPanel;
    // Once we show it's always a show.
    private boolean isShow = false;

    public SimulationMetricsPanelRunUpdateSubscriber(Supplier<Optional<UI>> getUISupplier, SimulationMetricsPanel simulationMetricsPanel) {
        super(getUISupplier);
        this.simulationMetricsPanel = simulationMetricsPanel;
    }

    @Override
    public void handleBusEvent(RunUpdateBusEvent event) {
        PushUtils.push(getUiSupplier(), () -> {
            // TODO -> FIONA -> please add the code here
            // simulationMetricsPanel.showfullPanel() or whatever it does.
        });
    }

    @Override
    public boolean filterBusEvent(RunUpdateBusEvent event) {
        // No need to do anything if it's already showing.
        if(isShow)
            return false;
        if(simulationMetricsPanel.getExperiment().getId() != event.getRun().getExperimentId())
            return false;
        if(event.getRun().getExperiment().getTrainingStatusEnum() == RunStatus.NotStarted || event.getRun().getExperiment().getTrainingStatusEnum() == RunStatus.Starting)
            return false;

        isShow = true;
        return true;
    }
}
