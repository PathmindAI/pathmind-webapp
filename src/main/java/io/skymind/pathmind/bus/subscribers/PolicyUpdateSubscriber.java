package io.skymind.pathmind.bus.subscribers;

import io.skymind.pathmind.bus.BusEventType;
import io.skymind.pathmind.bus.EventBusSubscriber;
import io.skymind.pathmind.bus.events.PolicyUpdateBusEvent;
import io.skymind.pathmind.data.Experiment;

public interface PolicyUpdateSubscriber extends EventBusSubscriber<PolicyUpdateBusEvent>
{
    Experiment getExperiment();

    @Override
    default BusEventType getEventType() {
        return BusEventType.PolicyUpdate;
    }

    @Override
    default boolean isFiltered(PolicyUpdateBusEvent event) {
        return getExperiment().getId() == event.getPolicy().getExperiment().getId();
    }
}
