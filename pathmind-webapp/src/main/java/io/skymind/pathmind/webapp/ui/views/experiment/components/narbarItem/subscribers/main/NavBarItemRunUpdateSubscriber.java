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
        // We have to do it manually here because we could be in another tabbed browser and we don't have a full reload, just the run.
        // TODO -> STEPH -> This should all be done in a single ExperimentUtils method as it will have to be replicated
        //  elsewhere such as ExperimentViewRunUpdateSubscriber.
        experimentsNavBarItem.getExperiment().setTrainingStatusEnum(event.getExperiment().getTrainingStatusEnum());
        ExperimentUtils.addOrUpdateRuns(experimentsNavBarItem.getExperiment(), event.getRuns());
        ExperimentUtils.updateTrainingStatus(experimentsNavBarItem.getExperiment());
        experimentsNavBarItem.updateVariableComponentValues();
    }

    @Override
    public boolean filterBusEvent(RunUpdateBusEvent event) {
        return !!event.getExperiment().isArchived() && ExperimentUtils.isSameExperiment(experimentsNavBarItem.getExperiment(), event.getExperiment());
    }
}
