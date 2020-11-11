package io.skymind.pathmind.webapp.ui.views.experiment.components.navbar.subscribers;

import io.skymind.pathmind.webapp.bus.events.main.ExperimentUpdatedBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.main.ExperimentUpdatedSubscriber;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.components.navbar.ExperimentsNavBar;

public class NavBarExperimentUpdatedSubscriber extends ExperimentUpdatedSubscriber {

    private ExperimentsNavBar experimentsNavBar;

    public NavBarExperimentUpdatedSubscriber(ExperimentsNavBar experimentsNavBar) {
        super();
        this.experimentsNavBar = experimentsNavBar;
    }

    // We can ignore this code for archived experiments since the navbar is not visible for archived experiments.
    public void handleBusEvent(ExperimentUpdatedBusEvent event) {
        if (event.getExperiment().isArchived()) {
            experimentsNavBar.removeExperiment(event.getExperiment());
        } else {
            experimentsNavBar.addExperiment(event.getExperiment());
        }
    }

    @Override
    public boolean filterBusEvent(ExperimentUpdatedBusEvent event) {
        // At this point the navbar only adds/removes elements when an experiment is archived or unarchived.
        return ExperimentUtils.isSameModel(event.getExperiment(), experimentsNavBar.getModelId()) && event.isArchiveEventType();
    }
}
