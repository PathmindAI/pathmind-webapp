package io.skymind.pathmind.webapp.bus.subscribers.main;

import io.skymind.pathmind.webapp.bus.BusEventType;
import io.skymind.pathmind.webapp.bus.EventBusSubscriber;
import io.skymind.pathmind.webapp.bus.events.main.ExperimentUpdatedBusEvent;

public abstract class ExperimentStartTrainingSubscriber extends EventBusSubscriber<ExperimentUpdatedBusEvent> {

    public ExperimentStartTrainingSubscriber() {
    }

    public ExperimentStartTrainingSubscriber(boolean isListenForEventOnSameUI) {
        super(isListenForEventOnSameUI);
    }

    @Override
    public BusEventType getEventType() {
        return BusEventType.ExperimentStartTraining;
    }
}
