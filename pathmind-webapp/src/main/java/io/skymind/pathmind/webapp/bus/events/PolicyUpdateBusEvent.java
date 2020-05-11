package io.skymind.pathmind.webapp.bus.events;

import java.util.List;

import io.skymind.pathmind.webapp.bus.BusEventType;
import io.skymind.pathmind.webapp.bus.PathmindBusEvent;
import io.skymind.pathmind.shared.data.Policy;

public class PolicyUpdateBusEvent implements PathmindBusEvent {
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
}
