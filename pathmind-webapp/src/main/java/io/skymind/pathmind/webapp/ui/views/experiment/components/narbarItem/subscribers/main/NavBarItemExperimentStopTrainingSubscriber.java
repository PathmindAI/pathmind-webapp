package io.skymind.pathmind.webapp.ui.views.experiment.components.narbarItem.subscribers.main;

import io.skymind.pathmind.shared.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.bus.events.main.ExperimentStopTrainingBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.main.ExperimentStopTrainingSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.components.narbarItem.ExperimentsNavBarItem;

public class NavBarItemExperimentStopTrainingSubscriber extends ExperimentStopTrainingSubscriber {

    private ExperimentsNavBarItem experimentsNavBarItem;

    public NavBarItemExperimentStopTrainingSubscriber(ExperimentsNavBarItem experimentsNavBarItem) {
        super(true);
        this.experimentsNavBarItem = experimentsNavBarItem;
    }

    public void handleBusEvent(ExperimentStopTrainingBusEvent event) {
        experimentsNavBarItem.updateExperiment(event.getExperiment());
    }

    @Override
    public boolean filterBusEvent(ExperimentStopTrainingBusEvent event) {
        return !event.getExperiment().isArchived() && ExperimentUtils.isSameExperiment(experimentsNavBarItem.getExperiment(), event.getExperiment());
    }
}
