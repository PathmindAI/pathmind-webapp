package io.skymind.pathmind.webapp.ui.views.experiment.subscribers;

import io.skymind.pathmind.shared.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.bus.events.main.ExperimentFavoriteBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.main.ExperimentFavoriteSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.NewExperimentView;

// Must be a subscriber because it can be set by other browsers and it still needs to be updated on the current view/page.
public class NewExperimentViewFavoriteSubscriber extends ExperimentFavoriteSubscriber {

    private NewExperimentView newExperimentView;

    public NewExperimentViewFavoriteSubscriber(NewExperimentView newExperimentView) {
        super();
        this.newExperimentView = newExperimentView;
    }

    @Override
    public void handleBusEvent(ExperimentFavoriteBusEvent event) {
        if (ExperimentUtils.isSameExperiment(newExperimentView.getExperiment(), event.getExperimentId())) {
            synchronized (newExperimentView.getExperimentLock()) {
                ExperimentUtils.updateIsFavorite(newExperimentView.getExperiment(), event.isFavorite());
                newExperimentView.updateComponents();
            }
        }
    }

    @Override
    public boolean filterBusEvent(ExperimentFavoriteBusEvent event) {
        return ExperimentUtils.isSameExperiment(newExperimentView.getExperiment(), event.getExperimentId());
    }
}
