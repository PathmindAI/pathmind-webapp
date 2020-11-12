package io.skymind.pathmind.webapp.ui.views.experiment.subscribers;

import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.webapp.bus.events.main.ExperimentUpdatedBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.main.ExperimentUpdatedSubscriber;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;
import io.skymind.pathmind.webapp.ui.views.experiment.NewExperimentView;

public class NewExperimentViewExperimentUpdatedSubscriber extends ExperimentUpdatedSubscriber {

    private NewExperimentView newExperimentView;

    public NewExperimentViewExperimentUpdatedSubscriber(NewExperimentView newExperimentView) {
        this.newExperimentView = newExperimentView;
    }

    @Override
    public void handleBusEvent(ExperimentUpdatedBusEvent event) {
        if (ExperimentUtils.isSameExperiment(event.getExperiment(), newExperimentView.getExperiment())) {
            if (event.isStartedTrainingEventType()) {
                getUiSupplier().get().get().navigate(ExperimentView.class, event.getExperiment().getId());
            }
        } else {
            if (ExperimentUtils.isSameModel(newExperimentView.getExperiment(), event.getModelId())) {
                newExperimentView.updateExperimentComponents();
            }
        }
    }

}
