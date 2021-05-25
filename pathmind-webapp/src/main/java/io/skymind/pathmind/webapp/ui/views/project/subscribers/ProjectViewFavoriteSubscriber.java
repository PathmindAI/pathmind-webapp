package io.skymind.pathmind.webapp.ui.views.project.subscribers;

import java.util.List;
import java.util.stream.Collectors;

import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.bus.events.main.ExperimentFavoriteBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.main.ExperimentFavoriteSubscriber;
import io.skymind.pathmind.webapp.ui.views.project.ProjectView;

// Must be a subscriber because it can be set by other browsers and it still needs to be updated on the current view/page.
public class ProjectViewFavoriteSubscriber extends ExperimentFavoriteSubscriber {

    private ProjectView projectView;

    public ProjectViewFavoriteSubscriber(ProjectView projectView) {
        super();
        this.projectView = projectView;
    }

    @Override
    public void handleBusEvent(ExperimentFavoriteBusEvent event) {
        Experiment targetExperiment = projectView.getExperiments()
                .stream()
                .filter(exp -> exp.getId() == event.getExperimentId())
                .findFirst()
                .orElse(null);
        if (targetExperiment != null) {
            targetExperiment.setFavorite(event.isFavorite());
            projectView.getExperimentGrid().getDataProvider().refreshAll();
        }
    }

    @Override
    public boolean filterBusEvent(ExperimentFavoriteBusEvent event) {
        List<Long> experimentIdList = projectView.getExperiments()
                .stream()
                .map(exp -> exp.getId())
                .collect(Collectors.toList());
        return ExperimentUtils.isSameModel(experimentIdList, event.getExperimentId());
    }
}
