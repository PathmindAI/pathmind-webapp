package io.skymind.pathmind.bus;

public interface PathmindBusEvent
{
	BusEventType getEventType();

	// STEPH -> Do we still need this?
	// Helper method since most of the time we're going to be passing data objects.
	default long getEventDataId() {
		return -1;
	}

	// STEPH -> Add filter here as a method that as default returns true
//	default boolean isFilter() {
//		return true;
//	}

//	public default boolean isEventType(BusEventType busEventType) {
//		return getEventType() == busEventType;
//	}
//
//	public default boolean isEventTypes(BusEventType... busEventTypes) {
//		return Arrays.stream(busEventTypes).anyMatch(busEventType -> getEventType().equals(busEventType));
//	}
//
//	public default boolean isMatchingEventId(long id) {
//		return getEventDataId() == id;
//	}
//
//	public default boolean isMatchingAtLeastEventId(long... ids) {
//		return Arrays.stream(ids).anyMatch(id -> id == getEventDataId());
//	}
}
