package io.skymind.pathmind.webapp.ui.views.experiment.components.trainingStatus.subscribers;

import com.vaadin.flow.component.UI;
import io.skymind.pathmind.webapp.bus.events.main.RunUpdateBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.main.RunUpdateSubscriber;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.ui.utils.PushUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.components.trainingStatus.TrainingStatusDetailsPanel;

import java.util.Optional;
import java.util.function.Supplier;

public class TrainingStatusDetailsPanelRunUpdateSubscriber extends RunUpdateSubscriber {

    private TrainingStatusDetailsPanel trainingStatusDetailsPanel;

    public TrainingStatusDetailsPanelRunUpdateSubscriber(Supplier<Optional<UI>> getUISupplier, TrainingStatusDetailsPanel trainingStatusDetailsPanel) {
        super(getUISupplier);
        this.trainingStatusDetailsPanel = trainingStatusDetailsPanel;
    }

    @Override
    public void handleBusEvent(RunUpdateBusEvent event) {
        PushUtils.push(getUiSupplier().get(), () -> {
            ExperimentUtils.addOrUpdateRuns(trainingStatusDetailsPanel.getExperiment(), event.getRuns());
            trainingStatusDetailsPanel.update();
        });
    }

    @Override
    public boolean filterBusEvent(RunUpdateBusEvent event) {
        return event.getExperimentId() == trainingStatusDetailsPanel.getExperiment().getId();
    }
}
