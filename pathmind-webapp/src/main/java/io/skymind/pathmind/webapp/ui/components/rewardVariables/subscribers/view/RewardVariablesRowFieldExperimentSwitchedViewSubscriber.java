package io.skymind.pathmind.webapp.ui.components.rewardVariables.subscribers.view;

import io.skymind.pathmind.webapp.bus.events.view.ExperimentSwitchedViewBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.view.ExperimentSwitchedViewSubscriber;
import io.skymind.pathmind.webapp.ui.components.rewardVariables.RewardVariablesRowField;

public class RewardVariablesRowFieldExperimentSwitchedViewSubscriber extends ExperimentSwitchedViewSubscriber {

    private RewardVariablesRowField rewardVariablesRowField;

    public RewardVariablesRowFieldExperimentSwitchedViewSubscriber(RewardVariablesRowField rewardVariablesRowField) {
        super();
        this.rewardVariablesRowField = rewardVariablesRowField;
    }

    @Override
    public void handleBusEvent(ExperimentSwitchedViewBusEvent event) {
        rewardVariablesRowField.reset();
    }
}
