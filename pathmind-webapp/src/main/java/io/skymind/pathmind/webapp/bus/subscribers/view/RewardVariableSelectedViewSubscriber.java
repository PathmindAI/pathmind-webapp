package io.skymind.pathmind.webapp.bus.subscribers.view;

import io.skymind.pathmind.webapp.bus.BusEventType;
import io.skymind.pathmind.webapp.bus.EventBusSubscriber;
import io.skymind.pathmind.webapp.bus.events.view.RewardVariableSelectedViewBusEvent;

public abstract class RewardVariableSelectedViewSubscriber extends EventBusSubscriber<RewardVariableSelectedViewBusEvent> {

    public RewardVariableSelectedViewSubscriber() {
        super();
    }

    @Override
    public BusEventType getEventType() {
        return BusEventType.RewardVariableSelected;
    }
}
