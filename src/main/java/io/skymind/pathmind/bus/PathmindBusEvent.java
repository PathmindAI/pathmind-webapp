package io.skymind.pathmind.bus;

import java.util.Arrays;

public interface PathmindBusEvent
{
	// TODO => I'm not sure all that will be needed but I'm sure it will be a lot more involved because
	// in this setup there could easily be racing conditions.

	public BusEventType getEventType();

	// Helper method since most of the time we're going to be passing data objects.
	public default long getEventDataId() {
		return -1;
	}

	public default boolean isEventType(BusEventType busEventType) {
		return getEventType() == busEventType;
	}

	public default boolean isEventTypes(BusEventType... busEventTypes) {
		return Arrays.stream(busEventTypes).anyMatch(busEventType -> getEventType().equals(busEventType));
	}

	public default boolean isMatchingEventId(long id) {
		return getEventDataId() == id;
	}

	public default boolean isMatchingAtLeastEventId(long... ids) {
		return Arrays.stream(ids).anyMatch(id -> id == getEventDataId());
	}
}
