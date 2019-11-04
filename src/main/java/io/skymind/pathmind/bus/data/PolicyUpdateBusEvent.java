package io.skymind.pathmind.bus.data;

import io.skymind.pathmind.bus.BusEventType;
import io.skymind.pathmind.bus.PathmindBusEvent;
import io.skymind.pathmind.data.Policy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PolicyUpdateBusEvent implements PathmindBusEvent
{
	private static Logger log = LogManager.getLogger(PolicyUpdateBusEvent.class);

	private Policy policy;
	private int test;

	private static int counter = 0;

	public PolicyUpdateBusEvent(Policy policy)
	{
		test = counter++;
		log.info(".................... CREATE PolicyUpdateBusEvent : " + test);

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

	public int getTest() {
		return test;
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
