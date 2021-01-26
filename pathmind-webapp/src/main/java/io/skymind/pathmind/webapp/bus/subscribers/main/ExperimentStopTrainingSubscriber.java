package io.skymind.pathmind.webapp.bus.subscribers.main;

import io.skymind.pathmind.webapp.bus.BusEventType;
import io.skymind.pathmind.webapp.bus.EventBusSubscriber;
import io.skymind.pathmind.webapp.bus.events.main.ExperimentStopTrainingBusEvent;

public abstract class ExperimentStopTrainingSubscriber extends EventBusSubscriber<ExperimentStopTrainingBusEvent> {

    public ExperimentStopTrainingSubscriber(boolean isListenForEventOnSameUI) {
        super(isListenForEventOnSameUI);
    }

    @Override
    public BusEventType getEventType() {
        return BusEventType.ExperimentStopTraining;
    }
}
