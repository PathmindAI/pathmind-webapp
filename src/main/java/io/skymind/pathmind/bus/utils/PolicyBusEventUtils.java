package io.skymind.pathmind.bus.utils;

import io.skymind.pathmind.bus.BusEventType;
import io.skymind.pathmind.bus.PathmindBusEvent;
import io.skymind.pathmind.bus.data.PolicyUpdateBusEvent;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.Policy;
import reactor.core.publisher.Flux;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class PolicyBusEventUtils
{
	/**
	 * PolicySupplier must be a supplier rather than just the value because the policy value we're comparing
	 * against can change after the lambda has been setup.
	 */
	public static void consumerBusEventBasedOnPolicy(Flux<PathmindBusEvent> consumer, Supplier<Policy> policySupplier, Consumer<Policy> policyConsumer)
	{
			consumer.filter(busEvent -> busEvent.getEventType().equals(BusEventType.PolicyUpdate))
				.filter(busEvent -> policySupplier.get() != null)
				.filter(busEvent -> isEventBusPolicyForSameExperiment(busEvent, policySupplier.get()))
				.filter(busEvent -> busEvent.getEventDataId() == policySupplier.get().getId())
				.subscribe(busEvent -> policyConsumer.accept(((PolicyUpdateBusEvent)busEvent).getPolicy()));
	}

	/**
	 * Similar to the above except that we need to check at the experiment level because we care about all policies for the experiment
	 * rather than just a specific policy.
	 */
	public static void consumerBusEventBasedOnExperiment(Flux<PathmindBusEvent> consumer, Supplier<Experiment> experimentSupplier, Consumer<Policy> policyConsumer)
	{
			consumer.filter(busEvent -> busEvent.getEventType().equals(BusEventType.PolicyUpdate))
				.filter(busEvent -> experimentSupplier.get() != null)
				.filter(busEvent -> isEventBusPolicyForSameExperiment(busEvent, experimentSupplier.get()))
				.subscribe(busEvent -> policyConsumer.accept(((PolicyUpdateBusEvent)busEvent).getPolicy()));
	}

	// TODO -> DH -> Policy score updates also need to know about the experiment. I need this because in some cases such as the
	// training list I need to know if the policy is new to the Experiment and so instead of updating it I will instead be
	// adding it as a new training item (policy) in the list.
	private static boolean isEventBusPolicyForSameExperiment(PathmindBusEvent busEvent, Policy policy) {
		return isEventBusPolicyForSameExperiment(busEvent, policy.getExperiment());
	}

	private static boolean isEventBusPolicyForSameExperiment(PathmindBusEvent busEvent, Experiment experiment) {
		return ((PolicyUpdateBusEvent)busEvent).getPolicy().getExperiment().getId() == experiment.getId();
	}
}
