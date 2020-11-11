package io.skymind.pathmind.webapp.ui.views.experiment.subscribers;

import java.util.Optional;
import java.util.function.Supplier;

import com.vaadin.flow.component.UI;

import io.skymind.pathmind.webapp.bus.events.main.ExperimentUpdatedBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.main.ExperimentUpdatedSubscriber;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;

public class ExperimentViewExperimentUpdatedSubscriber extends ExperimentUpdatedSubscriber {

    private ExperimentView experimentView;

    public ExperimentViewExperimentUpdatedSubscriber(Supplier<Optional<UI>> getUISupplier, ExperimentView experimentView) {
        super(getUISupplier);
        this.experimentView = experimentView;
    }

    @Override
    public void handleBusEvent(ExperimentUpdatedBusEvent event) {
        experimentView.updateExperimentComponents();
    }

    @Override
    public boolean filterBusEvent(ExperimentUpdatedBusEvent event) {
        if (experimentView.getExperiment() == null) {
            return false;
        }
        if (experimentView.getExperiment().isArchived()) {
            return ExperimentUtils.isSameExperiment(event.getExperiment(), experimentView.getExperiment());
        } else {
            return ExperimentUtils.isSameModel(experimentView.getExperiment(), event.getModelId());
        }
    }

}