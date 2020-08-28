package io.skymind.pathmind.webapp.ui.views.experiment.subscribers;

import com.vaadin.flow.component.UI;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.webapp.bus.events.ExperimentUpdatedBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.ExperimentUpdatedSubscriber;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.ui.utils.NotificationUtils;
import io.skymind.pathmind.webapp.ui.utils.PushUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class NotificationExperimentUpdatedSubscriber extends ExperimentUpdatedSubscriber {

    private List<Experiment> experiments;
    private Experiment experiment;

    public NotificationExperimentUpdatedSubscriber(Supplier<Optional<UI>> getUISupplier, List<Experiment> experiments, Experiment experiment) {
        super(getUISupplier);
        this.experiments = experiments;
        this.experiment = experiment;
    }

    // We can ignore this code for archived experiments since the navbar is not visible for archived experiments.
    public void handleBusEvent(ExperimentUpdatedBusEvent event) {
        // We need to update the internal experiments list for the navigation logic.
        ExperimentUtils.updateExperimentInExperimentsList(experiments, event.getExperiment());
        PushUtils.push(getUiSupplier().get(), ui -> {
            if(event.isStartedTrainingEventType())
                alertThenNotifyStarted(event);
            if(event.isArchiveEventType())
                alertThenNotifyArchive(event);
        });
    }

    private void alertThenNotifyArchive(ExperimentUpdatedBusEvent event) {
        NotificationUtils.alertAndThen(
                getUiSupplier(),
                event.getExperiment().isArchived() ? "Experiment Archived" : "Experiment Unarchived",
                event.getExperiment().isArchived() ? "The experiment was archived." : "The experiment was unarchived.",
                ui -> navigateToView(event.getExperiment()));
    }

    private void navigateToView(Experiment experiment) {
        if(experiment.isArchived())
            ExperimentUtils.navigateToFirstUnarchivedOrModel(getUiSupplier(), experiments);
        else
            ExperimentUtils.navigateToExperiment(getUiSupplier().get(), experiment);
    }

    private void alertThenNotifyStarted(ExperimentUpdatedBusEvent event) {
        NotificationUtils.alertAndThen(
                getUiSupplier(),
                "Training Started",
                "The experiment training started.",
                ui -> ui.navigate(ExperimentView.class, event.getExperiment().getId()));
    }

    @Override
    public boolean filterBusEvent(ExperimentUpdatedBusEvent event) {
        return ExperimentUtils.isSameExperiment(event.getExperiment(), experiment) &&
                (event.isStartedTrainingEventType() || event.isArchiveEventType());
    }

    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
    }
}