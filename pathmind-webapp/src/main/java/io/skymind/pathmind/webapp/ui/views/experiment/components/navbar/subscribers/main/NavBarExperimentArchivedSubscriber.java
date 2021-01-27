package io.skymind.pathmind.webapp.ui.views.experiment.components.navbar.subscribers.main;

import io.skymind.pathmind.shared.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.bus.events.main.ExperimentArchivedBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.main.ExperimentArchivedSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.components.navbar.ExperimentsNavBar;

public class NavBarExperimentArchivedSubscriber extends ExperimentArchivedSubscriber {

    private ExperimentsNavBar experimentsNavBar;

    public NavBarExperimentArchivedSubscriber(ExperimentsNavBar experimentsNavBar) {
        super();
        this.experimentsNavBar = experimentsNavBar;
    }

    // We can ignore this code for archived experiments since the navbar is not visible for archived experiments.
    @Override
    public void handleBusEvent(ExperimentArchivedBusEvent event) {
        experimentsNavBar.updateExperiment(event.getExperiment());
    }

    @Override
    public boolean filterBusEvent(ExperimentArchivedBusEvent event) {
        // At this point the navbar only adds/removes elements when an experiment is archived or unarchived.
        return ExperimentUtils.isSameModel(event.getExperiment(), experimentsNavBar.getModelId());
    }
}
