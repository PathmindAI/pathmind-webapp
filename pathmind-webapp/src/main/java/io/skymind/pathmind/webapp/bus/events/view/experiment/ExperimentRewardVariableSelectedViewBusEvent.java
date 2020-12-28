package io.skymind.pathmind.webapp.bus.events.view.experiment;

import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.webapp.bus.BusEventType;
import io.skymind.pathmind.webapp.bus.PathmindViewBusEvent;

// TODO -> STEPH -> This needs to be moved to an action.
public class ExperimentRewardVariableSelectedViewBusEvent implements PathmindViewBusEvent {

    private RewardVariable rewardVariable;
    private boolean isShow;

    public ExperimentRewardVariableSelectedViewBusEvent(RewardVariable rewardVariable, boolean isShow) {
        this.rewardVariable = rewardVariable;
        this.isShow = isShow;
    }

    @Override
    public BusEventType getEventType() {
        return BusEventType.RewardVariableSelected;
    }

    public RewardVariable getRewardVariable() {
        return rewardVariable;
    }

    public boolean isShow() {
        return isShow;
    }
}
