package io.skymind.pathmind.webapp.ui.views.experiment.subscribers;

import com.vaadin.flow.component.UI;
import io.skymind.pathmind.shared.utils.PolicyUtils;
import io.skymind.pathmind.webapp.bus.events.main.PolicyUpdateBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.main.PolicyUpdateSubscriber;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.ui.utils.PushUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;

import java.util.Optional;
import java.util.function.Supplier;

public class ExperimentViewPolicyUpdateSubscriber extends PolicyUpdateSubscriber
{
    private ExperimentView experimentView;

    public ExperimentViewPolicyUpdateSubscriber(Supplier<Optional<UI>> getUISupplier, ExperimentView experimentView) {
        super(getUISupplier);
        this.experimentView = experimentView;
    }

    @Override
    public void handleBusEvent(PolicyUpdateBusEvent event) {
        synchronized (experimentView.getExperimentLock()) {
            // Need a check in case the experiment was on hold waiting for the change of experiment to load
            if (event.getExperimentId() != event.getExperimentId())
                return;
            // Update or insert the policy in experiment.getPolicies
            ExperimentUtils.addOrUpdatePolicies(experimentView.getExperiment(), event.getPolicies());
            // This is needed for other subscriber updates that rely on the best policy being updated.
            experimentView.setBestPolicy(PolicyUtils.selectBestPolicy(experimentView.getExperiment().getPolicies()).orElse(null));

            PushUtils.push(getUiSupplier(), () -> {
                // REFACTOR -> This method is currently overloaded and does too much but that is for another PR.
                experimentView.updateDetailsForExperiment();
            });
        }
    }

    @Override
    public boolean filterBusEvent(PolicyUpdateBusEvent event) {
        return experimentView.getExperimentId() == event.getExperimentId();
    }
}