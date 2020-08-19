package io.skymind.pathmind.webapp.ui.views.experiment.subscribers;

import com.vaadin.flow.component.UI;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.webapp.bus.events.ExperimentUpdatedBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.ExperimentUpdatedSubscriber;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.ui.utils.NotificationUtils;
import io.skymind.pathmind.webapp.ui.utils.PushUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;
import io.skymind.pathmind.webapp.ui.views.model.ModelView;

import java.util.Optional;
import java.util.function.Supplier;

public class NotificationExperimentUpdatedSubscriber extends ExperimentUpdatedSubscriber {

    private Experiment experiment;

    public NotificationExperimentUpdatedSubscriber(Supplier<Optional<UI>> getUISupplier, Experiment experiment) {
        super(getUISupplier);
        this.experiment = experiment;
    }

    // We can ignore this code for archived experiments since the navbar is not visible for archived experiments.
    public void handleBusEvent(ExperimentUpdatedBusEvent event) {
        PushUtils.push(getUiSupplier().get(), ui -> {
            if(event.isStartedTraining())
                alertThenNotifyStarted(event);
            if(event.isArchive())
                alertThenNotifyArchive(event);
        });
    }

    private void alertThenNotifyArchive(ExperimentUpdatedBusEvent event) {
        NotificationUtils.alertAndThen(
                getUiSupplier(),
                event.getExperiment().isArchived() ? "Experiment Archived" : "Experiment Unarchived",
                event.getExperiment().isArchived() ? "The experiment was archived." : "The experiment was unarchived.",
                ui -> navigateToView(ui, event.getExperiment()));
    }

    private void navigateToView(UI ui, Experiment experiment) {
        if(experiment.isArchived())
            ui.navigate(ModelView.class, experiment.getModelId());
        else
            ExperimentUtils.navigateToExperiment(ui, experiment);
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
        // The last clause is if the archive state has changed OR the experiment isn't currently running.
        System.out.println("........................  " +  getUiSupplier().get().get().getUIId());
        if(isSourceSameUI(event))
            return false;
        return ExperimentUtils.isSameExperiment(event.getExperiment(), experiment) &&
                (event.isStartedTraining() || event.isArchive()) &&
                (event.isArchive() != experiment.isArchived() || !ExperimentUtils.isRunning(experiment));
    }

    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
    }
}
