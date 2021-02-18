package io.skymind.pathmind.webapp.ui.views.experiment.subscribers;

import io.skymind.pathmind.shared.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.bus.events.main.ExperimentFavoriteBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.main.ExperimentFavoriteSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;

// Must be a subscriber because it can be set by other browsers and it still needs to be updated on the current view/page.
public class ExperimentViewFavoriteSubscriber extends ExperimentFavoriteSubscriber {

    private ExperimentView experimentView;

    public ExperimentViewFavoriteSubscriber(ExperimentView experimentView) {
        super();
        this.experimentView = experimentView;
    }

    @Override
    public void handleBusEvent(ExperimentFavoriteBusEvent event) {
        if (ExperimentUtils.isSameExperiment(experimentView.getExperiment(), event.getExperimentId())) {
            synchronized (experimentView.getExperimentLock()) {
                ExperimentUtils.updateIsFavorite(experimentView.getExperiment(), event.isFavorite());
                experimentView.updateComponents();
            }
        } else {
            synchronized (experimentView.getComparisonExperimentLock()) {
                ExperimentUtils.updateIsFavorite(experimentView.getComparisonExperiment(), event.isFavorite());
                experimentView.updateComparisonComponents();
            }
        }
    }

    @Override
    public boolean filterBusEvent(ExperimentFavoriteBusEvent event) {
        return ExperimentUtils.isSameExperiment(experimentView.getExperiment(), event.getExperimentId()) ||
        ExperimentUtils.isSameExperiment(experimentView.getComparisonExperiment(), event.getExperimentId());
    }
}
