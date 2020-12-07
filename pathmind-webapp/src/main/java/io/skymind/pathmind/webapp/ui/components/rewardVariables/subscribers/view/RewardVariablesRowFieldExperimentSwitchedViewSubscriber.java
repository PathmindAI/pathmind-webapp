package io.skymind.pathmind.webapp.ui.components.rewardVariables.subscribers.view;

import io.skymind.pathmind.webapp.bus.events.view.experiment.ExperimentSwitchedViewBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.view.experiment.ExperimentSwitchedViewSubscriber;
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
