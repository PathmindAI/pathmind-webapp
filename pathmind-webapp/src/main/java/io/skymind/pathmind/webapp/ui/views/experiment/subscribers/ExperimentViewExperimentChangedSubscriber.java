package io.skymind.pathmind.webapp.ui.views.experiment.subscribers;

import java.util.Optional;
import java.util.function.Supplier;

import com.vaadin.flow.component.UI;

import io.skymind.pathmind.webapp.bus.events.view.ExperimentChangedViewBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.view.ExperimentChangedViewSubscriber;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.ui.utils.PushUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;

public class ExperimentViewExperimentChangedSubscriber extends ExperimentChangedViewSubscriber {

    private ExperimentView experimentView;

    public ExperimentViewExperimentChangedSubscriber(Supplier<Optional<UI>> getUISupplier, ExperimentView experimentView) {
        super(getUISupplier);
        this.experimentView = experimentView;
    }

    @Override
    public void handleBusEvent(ExperimentChangedViewBusEvent event) {
        PushUtils.push(getUiSupplier().get(), ui -> experimentView.setExperiment(event.getExperiment()));
    }

    @Override
    public boolean filterBusEvent(ExperimentChangedViewBusEvent event) {
        if (experimentView.getExperiment() == null) {
            return false;
        }
        return ExperimentUtils.isSameModel(experimentView.getExperiment(), event.getExperiment().getModelId()) &&
                !ExperimentUtils.isSameExperiment(event.getExperiment(), experimentView.getExperiment());

    }
}