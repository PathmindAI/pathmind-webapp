package io.skymind.pathmind.webapp.ui.views.experiment.components.trainingStatus.subscribers;

import com.vaadin.flow.component.UI;
import io.skymind.pathmind.webapp.bus.events.view.ExperimentChangedViewBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.view.ExperimentChangedViewSubscriber;
import io.skymind.pathmind.webapp.ui.utils.PushUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.components.trainingStatus.TrainingStatusDetailsPanel;

import java.util.Optional;
import java.util.function.Supplier;

public class TrainingStatusDetailsPanelExperimentChangedViewSubscriber extends ExperimentChangedViewSubscriber {

    private TrainingStatusDetailsPanel trainingStatusDetailsPanel;

    public TrainingStatusDetailsPanelExperimentChangedViewSubscriber(Supplier<Optional<UI>> getUISupplier, TrainingStatusDetailsPanel trainingStatusDetailsPanel) {
        super(getUISupplier);
        this.trainingStatusDetailsPanel = trainingStatusDetailsPanel;
    }

    @Override
    public void handleBusEvent(ExperimentChangedViewBusEvent event) {
        PushUtils.push(getUiSupplier(), () ->
                trainingStatusDetailsPanel.setExperiment(event.getExperiment()));
    }
}
