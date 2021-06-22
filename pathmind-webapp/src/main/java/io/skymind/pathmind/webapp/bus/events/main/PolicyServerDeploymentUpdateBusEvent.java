package io.skymind.pathmind.webapp.bus.events.main;

import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.services.PolicyServerService;
import io.skymind.pathmind.webapp.bus.BusEventType;
import io.skymind.pathmind.webapp.bus.PathmindBusEvent;
import lombok.Value;

@Value
public class PolicyServerDeploymentUpdateBusEvent implements PathmindBusEvent {

    long runId;
    Experiment experiment;
    PolicyServerService.DeploymentStatus status;

    @Override
    public PathmindBusEvent cloneForEventBus() {
        return new PolicyServerDeploymentUpdateBusEvent(runId,  experiment.deepClone(), status);
    }

    @Override
    public BusEventType getEventType() {
        return BusEventType.PolicyServerDeploymentUpdate;
    }
}
