package io.skymind.pathmind.webapp.ui.views.experiment.components.navbar.subscribers.view;

import io.skymind.pathmind.shared.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.bus.events.view.experiment.ExperimentSwitchedViewBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.view.experiment.ExperimentSwitchedViewSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.components.navbar.ExperimentsNavBar;

// TODO -> STEPH -> This needs to be removed and done as part of the view.setExperiment() code.
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
