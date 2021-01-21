package io.skymind.pathmind.webapp.ui.views.experiment.components.narbarItem.subscribers.main;

import io.skymind.pathmind.shared.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.bus.events.main.ExperimentUpdatedBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.main.ExperimentUpdatedSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.components.narbarItem.ExperimentsNavBarItem;

public class NavBarItemExperimentUpdatedSubscriber extends ExperimentUpdatedSubscriber {

    private ExperimentsNavBarItem experimentsNavBarItem;

    public NavBarItemExperimentUpdatedSubscriber(ExperimentsNavBarItem experimentsNavBarItem) {
        super(true);
        this.experimentsNavBarItem = experimentsNavBarItem;
    }

    public void handleBusEvent(ExperimentUpdatedBusEvent event) {
        experimentsNavBarItem.updateExperiment(event.getExperiment());
    }

    @Override
    public boolean filterBusEvent(ExperimentUpdatedBusEvent event) {
        return !event.getExperiment().isArchived() && ExperimentUtils.isSameExperiment(experimentsNavBarItem.getExperiment(), event.getExperiment());
    }
}
