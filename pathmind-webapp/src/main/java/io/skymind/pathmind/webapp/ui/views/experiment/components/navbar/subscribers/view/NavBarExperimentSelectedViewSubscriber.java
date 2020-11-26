package io.skymind.pathmind.webapp.ui.views.experiment.components.navbar.subscribers.view;

import io.skymind.pathmind.webapp.bus.events.view.ExperimentChangedViewBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.view.ExperimentChangedViewSubscriber;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.components.navbar.ExperimentsNavBar;

public class NavBarExperimentSelectedViewSubscriber extends ExperimentChangedViewSubscriber {

    private ExperimentsNavBar experimentsNavBar;

    public NavBarExperimentSelectedViewSubscriber(ExperimentsNavBar experimentsNavBar) {
        super();
        this.experimentsNavBar = experimentsNavBar;
    }

    @Override
    public void handleBusEvent(ExperimentChangedViewBusEvent event) {
        experimentsNavBar.setCurrentExperiment(event.getExperiment());
    }

    @Override
    public boolean filterBusEvent(ExperimentChangedViewBusEvent event) {
        return ExperimentUtils.isSameModel(event.getExperiment(), experimentsNavBar.getModelId());
    }
}
