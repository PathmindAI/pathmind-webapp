package io.skymind.pathmind.bus.events;

import java.util.List;

import io.skymind.pathmind.bus.BusEventType;
import io.skymind.pathmind.bus.PathmindBusEvent;
import io.skymind.pathmind.data.Policy;

public class PolicyUpdateBusEvent implements PathmindBusEvent
{
	private List<Policy> policies;
	private long experimentId;

	public PolicyUpdateBusEvent(List<Policy> policies)
	{
		policies.forEach(policy -> {
			if(policy.getRun() == null)
				throw new RuntimeException("Run is null");
			if(policy.getExperiment() == null)
				throw new RuntimeException("Experiment is null");
			if(policy.getModel() == null)
				throw new RuntimeException("Model is null");
			if(policy.getProject() == null)
				throw new RuntimeException("Project is null");
			
			experimentId = policy.getExperiment().getId();
		});
		

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
