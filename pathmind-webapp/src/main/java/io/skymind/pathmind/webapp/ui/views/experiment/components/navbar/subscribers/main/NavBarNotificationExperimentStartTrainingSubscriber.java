package io.skymind.pathmind.webapp.ui.views.experiment.components.navbar.subscribers.main;

import io.skymind.pathmind.webapp.bus.events.main.ExperimentUpdatedBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.main.ExperimentStartTrainingSubscriber;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.ui.utils.NotificationUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;
import io.skymind.pathmind.webapp.ui.views.experiment.components.navbar.ExperimentsNavBar;

public class NavBarNotificationExperimentStartTrainingSubscriber extends ExperimentStartTrainingSubscriber {

    private ExperimentsNavBar experimentsNavBar;

    public NavBarNotificationExperimentStartTrainingSubscriber(ExperimentsNavBar experimentsNavBar) {
        super();
        this.experimentsNavBar = experimentsNavBar;
    }

    public NavBarNotificationExperimentStartTrainingSubscriber(boolean isListenForEventOnSameUI, ExperimentsNavBar experimentsNavBar) {
        super(isListenForEventOnSameUI);
        this.experimentsNavBar = experimentsNavBar;
    }

    // We can ignore this code for archived experiments since the navbar is not visible for archived experiments.
    @Override
    public void handleBusEvent(ExperimentUpdatedBusEvent event) {
        alertThenNotifyStarted(event);
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
        return ExperimentUtils.isSameExperiment(event.getExperiment(), experimentsNavBar.getSelectedExperiment());
    }
}
