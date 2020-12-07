package io.skymind.pathmind.webapp.bus.subscribers.view.experiment;

import io.skymind.pathmind.webapp.bus.BusEventType;
import io.skymind.pathmind.webapp.bus.EventBusSubscriber;
import io.skymind.pathmind.webapp.bus.events.view.experiment.ExperimentRewardVariableSelectedViewBusEvent;

public abstract class ExperimentRewardVariableSelectedViewSubscriber extends EventBusSubscriber<ExperimentRewardVariableSelectedViewBusEvent> {

    public ExperimentRewardVariableSelectedViewSubscriber() {
        super();
    }

    @Override
    public BusEventType getEventType() {
        return BusEventType.RewardVariableSelected;
    }
}
