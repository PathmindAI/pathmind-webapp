package io.skymind.pathmind.webapp.ui.views.experiment.components.narbarItem.subscribers.main;

import io.skymind.pathmind.webapp.bus.events.main.ExperimentFavoriteBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.main.ExperimentFavoriteSubscriber;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.components.narbarItem.ExperimentsNavBarItem;

public class NavBarItemExperimentFavoriteSubscriber extends ExperimentFavoriteSubscriber {

    private ExperimentsNavBarItem experimentsNavBarItem;

    public NavBarItemExperimentFavoriteSubscriber(ExperimentsNavBarItem experimentsNavBarItem) {
        super();
        this.experimentsNavBarItem = experimentsNavBarItem;
    }

    @Override
    public void handleBusEvent(ExperimentFavoriteBusEvent event) {
        experimentsNavBarItem.updateExperiment(event.getExperiment());
    }

    @Override
    public boolean filterBusEvent(ExperimentFavoriteBusEvent event) {
        return !event.getExperiment().isArchived() && ExperimentUtils.isSameExperiment(experimentsNavBarItem.getExperiment(), event.getExperiment());
    }
}
