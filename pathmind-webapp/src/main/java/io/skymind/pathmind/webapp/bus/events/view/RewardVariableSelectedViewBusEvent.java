package io.skymind.pathmind.webapp.bus.events.view;

import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.webapp.bus.BusEventType;
import io.skymind.pathmind.webapp.bus.PathmindViewBusEvent;

public class RewardVariableSelectedViewBusEvent implements PathmindViewBusEvent {

    private RewardVariable rewardVariable;

    public RewardVariableSelectedViewBusEvent(RewardVariable rewardVariable) {
        this.rewardVariable = rewardVariable;
    }

    @Override
    public BusEventType getEventType() {
        return BusEventType.RewardVariableSelected;
    }

    public RewardVariable getRewardVariable() {
        return rewardVariable;
    }
}
