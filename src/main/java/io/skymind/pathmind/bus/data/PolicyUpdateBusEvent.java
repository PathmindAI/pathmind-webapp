package io.skymind.pathmind.bus.data;

import io.skymind.pathmind.bus.BusEventType;
import io.skymind.pathmind.bus.PathmindBusEvent;
import io.skymind.pathmind.data.Policy;

public class PolicyUpdateBusEvent implements PathmindBusEvent
{
	private Policy policy;

	public PolicyUpdateBusEvent(Policy policy) {
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
