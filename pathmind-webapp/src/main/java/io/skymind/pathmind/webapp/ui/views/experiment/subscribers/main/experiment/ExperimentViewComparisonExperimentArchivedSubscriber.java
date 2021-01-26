package io.skymind.pathmind.webapp.ui.views.experiment.subscribers.main.experiment;

import io.skymind.pathmind.shared.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.bus.events.main.ExperimentArchivedBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.main.ExperimentArchivedSubscriber;
import io.skymind.pathmind.webapp.ui.utils.NotificationUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;

public class ExperimentViewComparisonExperimentArchivedSubscriber extends ExperimentArchivedSubscriber {

    private ExperimentView experimentView;

    public ExperimentViewComparisonExperimentArchivedSubscriber(ExperimentView experimentView) {
        this.experimentView = experimentView;
    }

    @Override
    public void handleBusEvent(ExperimentArchivedBusEvent event) {
        NotificationUtils.alertAndThen(getUiSupplier(),
                "Comparison Experiment Archived",
                "The comparison experiment was archived.",
                ui -> experimentView.leaveComparisonMode());
    }

    @Override
    public boolean filterBusEvent(ExperimentArchivedBusEvent event) {
        // We only want to filter if it's for the comparisonExperiment, it's in comparison mode, and if
        // the experiment is going to be archived (unarchiving would mean it was not possible to display it).
        // The isSameExperiment() comparison must be last because we have to first be comparisonMode to even
        // have a comparisonExperiment.
        return experimentView.isComparisonMode() &&
                event.getExperiment().isArchived() &&
                ExperimentUtils.isSameExperiment(event.getExperiment(), experimentView.getComparisonExperiment());
    }
}
