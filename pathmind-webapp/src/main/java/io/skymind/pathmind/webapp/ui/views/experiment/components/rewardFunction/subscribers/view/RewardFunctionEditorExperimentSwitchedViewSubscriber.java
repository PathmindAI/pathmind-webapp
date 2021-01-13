package io.skymind.pathmind.webapp.ui.views.experiment.components.rewardFunction.subscribers.view;

import io.skymind.pathmind.webapp.bus.events.view.ExperimentSwitchedViewBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.view.ExperimentSwitchedViewSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.components.rewardFunction.RewardFunctionEditor;

public class RewardFunctionEditorExperimentSwitchedViewSubscriber extends ExperimentSwitchedViewSubscriber {

    private RewardFunctionEditor rewardFunctionEditor;

    public RewardFunctionEditorExperimentSwitchedViewSubscriber(RewardFunctionEditor rewardFunctionEditor) {
        super();
        this.rewardFunctionEditor = rewardFunctionEditor;
    }

    @Override
    public void handleBusEvent(ExperimentSwitchedViewBusEvent event) {
        rewardFunctionEditor.setExperiment(event.getExperiment());
    }
}