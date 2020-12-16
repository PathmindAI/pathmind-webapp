package io.skymind.pathmind.webapp.ui.views.experiment.components.narbarItem.subscribers.main;

import io.skymind.pathmind.shared.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.bus.events.main.RunUpdateBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.main.RunUpdateSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.components.narbarItem.ExperimentsNavBarItem;

public class NavBarItemRunUpdateSubscriber extends RunUpdateSubscriber {

    private ExperimentsNavBarItem experimentsNavBarItem;

    public NavBarItemRunUpdateSubscriber(ExperimentsNavBarItem experimentsNavBarItem) {
        super();
        this.experimentsNavBarItem = experimentsNavBarItem;
    }

    @Override
    public void handleBusEvent(RunUpdateBusEvent event) {
        // TODO -> STEPH -> Are we going to be reloading everything? Replace the existing experiment? Etc... Who calls this event. My thinking is that the experiment
        // should already be fully loaded sow e can just update as needed. That's of course assuming that the policy has the full data required (confirm with ExperimentDAO).
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
