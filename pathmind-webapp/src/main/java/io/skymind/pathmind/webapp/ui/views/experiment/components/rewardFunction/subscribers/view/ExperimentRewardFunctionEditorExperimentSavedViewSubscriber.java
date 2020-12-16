package io.skymind.pathmind.webapp.ui.views.experiment.components.rewardFunction.subscribers.view;

import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.webapp.bus.events.view.experiment.ExperimentSwitchedViewBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.view.experiment.ExperimentSwitchedViewSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.components.rewardFunction.RewardFunctionEditor;

public class ExperimentRewardFunctionEditorExperimentSavedViewSubscriber extends ExperimentSwitchedViewSubscriber {

    private RewardFunctionEditor rewardFunctionEditor;
    private ExperimentDAO experimentDAO;

    public ExperimentRewardFunctionEditorExperimentSavedViewSubscriber(RewardFunctionEditor rewardFunctionEditor, ExperimentDAO experimentDAO) {
        super();
        this.rewardFunctionEditor = rewardFunctionEditor;
        this.experimentDAO = experimentDAO;
    }

    // TODO -> STEPH -> Need to be done as part of the view.setExperiment() or whatever it is that NewExperiment will use for save.
    @Override
    public void handleBusEvent(ExperimentSwitchedViewBusEvent event) {
        experimentDAO.updateRewardFunction(rewardFunctionEditor.getExperiment());
    }
}