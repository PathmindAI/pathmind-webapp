package io.skymind.pathmind.webapp.ui.views.experiment.subscribers.main.newExperiment;

import io.skymind.pathmind.webapp.bus.events.main.ExperimentStartTrainingBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.main.ExperimentStartTrainingSubscriber;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;
import io.skymind.pathmind.webapp.ui.views.experiment.NewExperimentView;

public class NewExperimentViewExperimentStartTrainingSubscriber extends ExperimentStartTrainingSubscriber {

    private NewExperimentView newExperimentView;

    public NewExperimentViewExperimentStartTrainingSubscriber(NewExperimentView newExperimentView) {
        this.newExperimentView = newExperimentView;
    }

    @Override
     public void handleBusEvent(ExperimentStartTrainingBusEvent event) {
        getUiSupplier().get().get().navigate(ExperimentView.class, event.getExperiment().getId());
    }

    @Override
    public boolean filterBusEvent(ExperimentStartTrainingBusEvent event) {
        return ExperimentUtils.isSameExperiment(event.getExperiment(), newExperimentView.getExperiment());
    }
}
