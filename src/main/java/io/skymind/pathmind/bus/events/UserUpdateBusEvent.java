package io.skymind.pathmind.bus.events;

import io.skymind.pathmind.bus.BusEventType;
import io.skymind.pathmind.bus.PathmindBusEvent;
import io.skymind.pathmind.data.PathmindUser;

public class UserUpdateBusEvent implements PathmindBusEvent
{
	private PathmindUser user;

	public UserUpdateBusEvent(PathmindUser user) {
		this.user = user;
	}

	@Override
	public BusEventType getEventType() {
		return BusEventType.UserUpdate;
	}

	public PathmindUser getPathmindUser() {
		return user;
	}

	public void setPathmindUser(PathmindUser user) {
		this.user = user;
	}
}
