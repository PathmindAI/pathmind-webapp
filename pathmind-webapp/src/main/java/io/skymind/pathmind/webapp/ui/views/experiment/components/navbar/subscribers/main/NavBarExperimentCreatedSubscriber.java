package io.skymind.pathmind.webapp.ui.views.experiment.components.navbar.subscribers.main;

import io.skymind.pathmind.shared.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.bus.events.main.ExperimentCreatedBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.main.ExperimentCreatedSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.components.navbar.ExperimentsNavBar;

public class NavBarExperimentCreatedSubscriber extends ExperimentCreatedSubscriber {

    private ExperimentsNavBar experimentsNavBar;

    public NavBarExperimentCreatedSubscriber(ExperimentsNavBar experimentsNavBar) {
        super();
        this.experimentsNavBar = experimentsNavBar;
    }

    @Override
    public void handleBusEvent(ExperimentCreatedBusEvent event) {
        experimentsNavBar.addExperiment(event.getExperiment());
    }

    @Override
    public boolean filterBusEvent(ExperimentCreatedBusEvent event) {
        return ExperimentUtils.isNewExperimentForModel(event.getExperiment(), experimentsNavBar.getExperiments(), experimentsNavBar.getModelId());
    }
}
