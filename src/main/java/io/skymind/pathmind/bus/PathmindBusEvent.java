package io.skymind.pathmind.bus;

public interface PathmindBusEvent
{
	// TODO => I'm not sure all that will be needed but I'm sure it will be a lot more involved because
	// in this setup there could easily be racing conditions.

	public BusEventType getEventType();

	// Helper method since most of the time we're going to be passing data objects.
	public default long getEventDataId() {
		return -1;
	}
}
