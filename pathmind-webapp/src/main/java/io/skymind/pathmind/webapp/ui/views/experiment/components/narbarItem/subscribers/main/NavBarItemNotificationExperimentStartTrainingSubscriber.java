package io.skymind.pathmind.webapp.ui.views.experiment.components.narbarItem.subscribers.main;

import io.skymind.pathmind.webapp.bus.events.main.ExperimentStartTrainingBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.main.ExperimentStartTrainingSubscriber;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.components.narbarItem.ExperimentsNavBarItem;

public class NavBarItemNotificationExperimentStartTrainingSubscriber extends ExperimentStartTrainingSubscriber {

    private ExperimentsNavBarItem experimentsNavBarItem;

    public NavBarItemNotificationExperimentStartTrainingSubscriber(ExperimentsNavBarItem experimentsNavBarItem) {
        super();
        this.experimentsNavBarItem = experimentsNavBarItem;
    }

    public void handleBusEvent(ExperimentStartTrainingBusEvent event) {
        experimentsNavBarItem.updateExperiment(event.getExperiment());
    }

    @Override
    public boolean filterBusEvent(ExperimentStartTrainingBusEvent event) {
        return !event.getExperiment().isArchived() && ExperimentUtils.isSameExperiment(experimentsNavBarItem.getExperiment(), event.getExperiment());
    }
}
