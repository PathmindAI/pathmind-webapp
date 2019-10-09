package io.skymind.pathmind.bus.data;

import io.skymind.pathmind.bus.BusEventType;
import io.skymind.pathmind.bus.PathmindBusEvent;
import io.skymind.pathmind.data.Policy;

public class PolicyUpdateBusEvent implements PathmindBusEvent
{
	private Policy policy;

	public PolicyUpdateBusEvent(Policy policy)
	{
		if(policy.getRun() == null)
			throw new RuntimeException("Run is null");
		if(policy.getExperiment() == null)
			throw new RuntimeException("Experiment is null");
		if(policy.getModel() == null)
			throw new RuntimeException("Model is null");
		if(policy.getProject() == null)
			throw new RuntimeException("Project is null");

		this.policy = policy;
	}

	@Override
	public BusEventType getEventType() {
		return BusEventType.PolicyUpdate;
	}

	@Override
	public long getEventDataId() {
		return policy.getId();
	}

	public Policy getPolicy() {
		return policy;
	}

	public void setPolicy(Policy policy) {
		this.policy = policy;
	}
}
