package io.skymind.pathmind.webapp.bus.subscribers.main;

import io.skymind.pathmind.webapp.bus.BusEventType;
import io.skymind.pathmind.webapp.bus.EventBusSubscriber;
import io.skymind.pathmind.webapp.bus.events.main.PolicyServerDeploymentUpdateBusEvent;

public abstract class ExperimentPolicyServerDeploymentUpdateSubscriber extends EventBusSubscriber<PolicyServerDeploymentUpdateBusEvent> {

    public ExperimentPolicyServerDeploymentUpdateSubscriber() {
        super(true);
    }

    @Override
    public BusEventType getEventType() {
        return BusEventType.PolicyServerDeploymentUpdate;
    }
}
