package io.skymind.pathmind.webapp.ui.views.experiment.actions.experiment;

import com.vaadin.flow.component.button.Button;
import io.skymind.pathmind.services.TrainingService;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.webapp.bus.EventBus;
import io.skymind.pathmind.webapp.bus.events.main.ExperimentUpdatedBusEvent;
import io.skymind.pathmind.webapp.bus.events.main.PolicyUpdateBusEvent;
import io.skymind.pathmind.webapp.bus.events.main.RunUpdateBusEvent;
import io.skymind.pathmind.webapp.ui.utils.ConfirmationUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;

import java.util.function.Supplier;

public class StopTrainingAction {

    public static void stopTraining(ExperimentView experimentView, Supplier<Experiment> getExperimentSupplier, Runnable updateExperimentRunnable, Supplier<Object> getLockSupplier, TrainingService trainingService, Button stopTrainingButton) {
        ConfirmationUtils.showStopTrainingConfirmationPopup(() -> {
            synchronized (getLockSupplier.get()) {
                trainingService.stopRun(getExperimentSupplier.get());
                experimentView.getSegmentIntegrator().stopTraining();
                stopTrainingButton.setVisible(false);
                updateExperimentRunnable.run();
                fireEvents(getExperimentSupplier.get());
            }
        });
    }

    private static void fireEvents(Experiment experiment) {
        // An event for each policy since we only need to update some of the policies in a run.
        if (experiment.getPolicies() != null && !experiment.getPolicies().isEmpty()) {
            EventBus.post(new PolicyUpdateBusEvent(experiment.getPolicies()));
        }
        // Send run updated event, meaning that all policies under the run is updated.
        // This is needed especially in dashboard, to refresh the item only once per run, instead of after all policy updates
        EventBus.post(new RunUpdateBusEvent(experiment.getRuns()));
        EventBus.post(new ExperimentUpdatedBusEvent(experiment));
    }
}
