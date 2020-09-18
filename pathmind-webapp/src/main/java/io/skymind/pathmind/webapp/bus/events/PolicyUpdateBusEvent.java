package io.skymind.pathmind.webapp.bus.events;

import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.webapp.bus.BusEventType;
import io.skymind.pathmind.webapp.bus.CloneUtils;
import io.skymind.pathmind.webapp.bus.CloneablePathmindBusEvent;

import java.util.List;

public class PolicyUpdateBusEvent implements CloneablePathmindBusEvent {
    private List<Policy> policies;
    private long experimentId;

    public PolicyUpdateBusEvent(List<Policy> policies) {
        if (policies == null || policies.isEmpty()) {
            throw new IllegalStateException("There is no policy");
        }
        policies.forEach(policy -> {
            if (policy.getRun() == null)
                throw new IllegalStateException("Run is null");
            if (policy.getExperiment() == null)
                throw new IllegalStateException("Experiment is null");
            if (policy.getModel() == null)
                throw new IllegalStateException("Model is null");
            if (policy.getProject() == null)
                throw new IllegalStateException("Project is null");
        });

        experimentId = policies.get(0).getExperiment().getId();
        this.policies = policies;
    }

    @Override
    public BusEventType getEventType() {
        return BusEventType.PolicyUpdate;
    }

    public List<Policy> getPolicies() {
        return policies;
    }

    public long getExperimentId() {
        return experimentId;
    }

    @Override
    public PolicyUpdateBusEvent cloneForEventBus() {
        return new PolicyUpdateBusEvent(CloneUtils.cloneUsingSerialization(this.policies));
    }
}
