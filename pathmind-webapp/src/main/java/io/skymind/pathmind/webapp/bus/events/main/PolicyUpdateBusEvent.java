package io.skymind.pathmind.webapp.bus.events.main;

import java.util.List;
import java.util.stream.Collectors;

import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.webapp.bus.BusEventType;
import io.skymind.pathmind.webapp.bus.PathmindBusEvent;

/**
 * Event used to indicate policies have been updated for an experiment. The experiment
 * details are taken from the first policy in the list of policies. The policies cannot be
 * null or empty, and each policy has to have the appropriate data such as a run, experiment,
 * model, and project. There is also an added safety check to confirm that all policies
 * belong to the same experiment otherwise there will be an IllegalStateException.
 */
public class PolicyUpdateBusEvent implements PathmindBusEvent {

    private List<Policy> policies;
    private Experiment experiment;

    public PolicyUpdateBusEvent(List<Policy> policies) {
        if (policies == null || policies.isEmpty()) {
            throw new IllegalStateException("There is no policy");
        }
        policies.forEach(policy -> {
            if (policy.getRun() == null) {
                throw new IllegalStateException("Run is null");
            }
            if (policy.getExperiment() == null) {
                throw new IllegalStateException("Experiment is null");
            }
            if (policy.getModel() == null) {
                throw new IllegalStateException("Model is null");
            }
            if (policy.getProject() == null) {
                throw new IllegalStateException("Project is null");
            }
        });

        experiment = policies.get(0).getExperiment();

        if (policies.stream().anyMatch(policy -> policy.getExperiment().getId() != experiment.getId())) {
            throw new IllegalStateException("One of the policies is for a different experiment");
        }

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
        return experiment.getId();
    }

    public Experiment getExperiment() {
        return experiment;
    }

    public PolicyUpdateBusEvent cloneForEventBus() {
        return new PolicyUpdateBusEvent(policies.stream()
                .map(policy -> policy.deepClone())
                .collect(Collectors.toList()));
    }
}
