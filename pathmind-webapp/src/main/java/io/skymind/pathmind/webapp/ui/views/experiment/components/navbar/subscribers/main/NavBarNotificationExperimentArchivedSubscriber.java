package io.skymind.pathmind.webapp.ui.views.experiment.components.navbar.subscribers.main;

import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.bus.events.main.ExperimentArchivedBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.main.ExperimentArchivedSubscriber;
import io.skymind.pathmind.webapp.data.utils.ExperimentGuiUtils;
import io.skymind.pathmind.webapp.ui.utils.NotificationUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.components.navbar.ExperimentsNavBar;

public class NavBarNotificationExperimentArchivedSubscriber extends ExperimentArchivedSubscriber {

    private ExperimentsNavBar experimentsNavBar;

    public NavBarNotificationExperimentArchivedSubscriber(ExperimentsNavBar experimentsNavBar) {
        super();
        this.experimentsNavBar = experimentsNavBar;
    }

    // We can ignore this code for archived experiments since the navbar is not visible for archived experiments.
    @Override
    public void handleBusEvent(ExperimentArchivedBusEvent event) {
        // TODO -> STEPH -> This should all be done in one place with the main susbcriber on the view and it updates all the components through the view
        // with view.setExperiment() which propogates.
        ExperimentUtils.updateExperimentInExperimentsList(experimentsNavBar.getExperiments(), event.getExperiment());
        alertThenNotifyArchive(event);
    }

    private void alertThenNotifyArchive(ExperimentArchivedBusEvent event) {
        NotificationUtils.alertAndThen(
                getUiSupplier(),
                event.getExperiment().isArchived() ? "Experiment Archived" : "Experiment Unarchived",
                event.getExperiment().isArchived() ? "The experiment was archived." : "The experiment was unarchived.",
                ui -> navigateToView(event.getExperiment()));
    }

    private void navigateToView(Experiment experiment) {
        if (experiment.isArchived()) {
            ExperimentGuiUtils.navigateToFirstUnarchivedOrModel(getUiSupplier(), experimentsNavBar.getExperiments());
        } else {
            ExperimentGuiUtils.navigateToExperiment(getUiSupplier().get(), experiment);
        }
    }

    @Override
    public boolean filterBusEvent(ExperimentArchivedBusEvent event) {
        return ExperimentUtils.isSameExperiment(event.getExperiment(), experimentsNavBar.getSelectedExperiment());
    }
}
