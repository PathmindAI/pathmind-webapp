package io.skymind.pathmind.webapp.ui.views.experiment.components.trainingStatus.subscribers;

import com.vaadin.flow.component.UI;
import io.skymind.pathmind.webapp.bus.events.main.PolicyUpdateBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.main.PolicyUpdateSubscriber;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.ui.utils.PushUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.components.trainingStatus.TrainingStatusDetailsPanel;

import java.util.Optional;
import java.util.function.Supplier;

public class TrainingStatusDetailsPanelPolicyUpdateSubscriber extends PolicyUpdateSubscriber {

    private TrainingStatusDetailsPanel trainingStatusDetailsPanel;

    public TrainingStatusDetailsPanelPolicyUpdateSubscriber(Supplier<Optional<UI>> getUISupplier, TrainingStatusDetailsPanel trainingStatusDetailsPanel) {
        super(getUISupplier);
        this.trainingStatusDetailsPanel = trainingStatusDetailsPanel;
    }

    @Override
    public void handleBusEvent(PolicyUpdateBusEvent event) {
        PushUtils.push(getUiSupplier(), ui -> {
            ExperimentUtils.addOrUpdatePolicies(trainingStatusDetailsPanel.getExperiment(), event.getPolicies());
            trainingStatusDetailsPanel.update();
        });
    }

    @Override
    public boolean filterBusEvent(PolicyUpdateBusEvent event) {
        return trainingStatusDetailsPanel.getExperiment().getId() == event.getExperimentId();
    }
}