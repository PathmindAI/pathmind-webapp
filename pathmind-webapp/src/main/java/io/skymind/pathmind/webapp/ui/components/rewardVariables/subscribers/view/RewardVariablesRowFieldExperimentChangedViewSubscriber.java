package io.skymind.pathmind.webapp.ui.components.rewardVariables.subscribers.view;

import io.skymind.pathmind.webapp.bus.events.view.ExperimentChangedViewBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.view.ExperimentChangedViewSubscriber;
import io.skymind.pathmind.webapp.ui.components.rewardVariables.RewardVariablesRowField;

public class RewardVariablesRowFieldExperimentChangedViewSubscriber extends ExperimentChangedViewSubscriber {

    private RewardVariablesRowField rewardVariablesRowField;

    public RewardVariablesRowFieldExperimentChangedViewSubscriber(RewardVariablesRowField rewardVariablesRowField) {
        super();
        this.rewardVariablesRowField = rewardVariablesRowField;
    }

    @Override
    public void handleBusEvent(ExperimentChangedViewBusEvent event) {
        rewardVariablesRowField.reset();
    }
}
