package io.skymind.pathmind.shared.bus.events;

import io.skymind.pathmind.shared.bus.BusEventType;
import io.skymind.pathmind.shared.bus.PathmindBusEvent;
import io.skymind.pathmind.shared.data.PathmindUser;

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
