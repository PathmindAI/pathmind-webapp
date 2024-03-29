package io.skymind.pathmind.webapp.ui.views.experiment.components.narbarItem.subscribers.main;

import io.skymind.pathmind.shared.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.bus.events.main.ExperimentFavoriteBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.main.ExperimentFavoriteSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.components.narbarItem.ExperimentsNavBarItem;

// Must be a subscriber because it can be set by other browsers and it still needs to be updated on the current view/page.
public class NavBarItemExperimentFavoriteSubscriber extends ExperimentFavoriteSubscriber {

    private ExperimentsNavBarItem experimentsNavBarItem;

    public NavBarItemExperimentFavoriteSubscriber(ExperimentsNavBarItem experimentsNavBarItem) {
        super();
        this.experimentsNavBarItem = experimentsNavBarItem;
    }

    @Override
    public void handleBusEvent(ExperimentFavoriteBusEvent event) {
        experimentsNavBarItem.setIsFavorite(event.isFavorite());
        experimentsNavBarItem.updateVariableComponentValues();
    }

    @Override
    public boolean filterBusEvent(ExperimentFavoriteBusEvent event) {
        return ExperimentUtils.isSameExperiment(experimentsNavBarItem.getExperiment(), event.getExperimentId());
    }
}
