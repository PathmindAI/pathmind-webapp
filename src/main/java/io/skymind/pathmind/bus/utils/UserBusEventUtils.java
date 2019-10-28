package io.skymind.pathmind.bus.utils;

import io.skymind.pathmind.bus.BusEventType;
import io.skymind.pathmind.bus.PathmindBusEvent;
import io.skymind.pathmind.bus.data.UserUpdateBusEvent;
import io.skymind.pathmind.data.PathmindUser;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class UserBusEventUtils
{

	public static Disposable consumerBusEventBasedOnUserUpdate(Flux<PathmindBusEvent> consumer,
															   Supplier<PathmindUser> userSupplier, Consumer<PathmindUser> userConsumer)
	{
		return consumer.filter(busEvent -> busEvent.getEventType().equals(BusEventType.UserUpdate))
				.filter(busEvent -> userSupplier.get() != null)
				.filter(busEvent -> isEventBusUserSame(busEvent, userSupplier.get()))
				.subscribe(busEvent -> userConsumer.accept(((UserUpdateBusEvent) busEvent).getPathmindUser()));
	}

	private static boolean isEventBusUserSame(PathmindBusEvent busEvent, PathmindUser user) {
		return ((UserUpdateBusEvent) busEvent).getPathmindUser().getId() == user.getId();
	}
}
