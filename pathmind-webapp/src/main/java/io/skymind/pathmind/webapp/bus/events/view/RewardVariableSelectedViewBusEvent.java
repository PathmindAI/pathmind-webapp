package io.skymind.pathmind.webapp.bus.events.view;

import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.webapp.bus.BusEventType;
import io.skymind.pathmind.webapp.bus.PathmindViewBusEvent;

public class RewardVariableSelectedViewBusEvent implements PathmindViewBusEvent {

    private RewardVariable rewardVariable;
    private boolean isShow;

    public RewardVariableSelectedViewBusEvent(RewardVariable rewardVariable, boolean isShow) {
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

    @Override
    public RewardVariableSelectedViewBusEvent cloneForEventBus() {
        return new RewardVariableSelectedViewBusEvent(rewardVariable.deepClone(), isShow);
    }
}
