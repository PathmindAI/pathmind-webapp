package io.skymind.pathmind.webapp.ui.views.experiment.components.narbarItem.subscribers.main;

import io.skymind.pathmind.webapp.bus.events.main.RunUpdateBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.main.RunUpdateSubscriber;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.components.narbarItem.ExperimentsNavBarItem;

public class NavBarItemRunUpdateSubscriber extends RunUpdateSubscriber {

    private ExperimentsNavBarItem experimentsNavBarItem;

    public NavBarItemRunUpdateSubscriber(ExperimentsNavBarItem experimentsNavBarItem) {
        super();
        this.experimentsNavBarItem = experimentsNavBarItem;
    }

    @Override
    public void handleBusEvent(RunUpdateBusEvent event) {
        ExperimentUtils.addOrUpdateRuns(experimentsNavBarItem.getExperiment(), event.getRuns());
        experimentsNavBarItem.update();
    }

    @Override
    public boolean filterBusEvent(RunUpdateBusEvent event) {
        // If it's archived then we don't need to update anything since there is no NavBar.
        if (event.getExperiment().isArchived()) {
            return false;
        }
        return ExperimentUtils.isSameExperiment(experimentsNavBarItem.getExperiment(), event.getExperiment());
    }
}
