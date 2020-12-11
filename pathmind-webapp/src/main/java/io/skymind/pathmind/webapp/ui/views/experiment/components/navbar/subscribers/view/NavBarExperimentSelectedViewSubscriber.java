package io.skymind.pathmind.webapp.ui.views.experiment.components.navbar.subscribers.view;

import io.skymind.pathmind.webapp.bus.events.view.ExperimentSwitchedViewBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.view.ExperimentSwitchedViewSubscriber;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.components.navbar.ExperimentsNavBar;

public class NavBarExperimentSelectedViewSubscriber extends ExperimentSwitchedViewSubscriber {

    private ExperimentsNavBar experimentsNavBar;

    public NavBarExperimentSelectedViewSubscriber(ExperimentsNavBar experimentsNavBar) {
        super();
        this.experimentsNavBar = experimentsNavBar;
    }

    @Override
    public void handleBusEvent(ExperimentSwitchedViewBusEvent event) {
        experimentsNavBar.setCurrentExperiment(event.getExperiment());
    }

    @Override
    public boolean filterBusEvent(ExperimentSwitchedViewBusEvent event) {
        return ExperimentUtils.isSameModel(event.getExperiment(), experimentsNavBar.getModelId());
    }
}
