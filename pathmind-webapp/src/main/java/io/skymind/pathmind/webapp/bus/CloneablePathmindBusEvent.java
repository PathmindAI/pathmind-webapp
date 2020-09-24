package io.skymind.pathmind.webapp.bus;

public interface CloneablePathmindBusEvent extends PathmindBusEvent {
    CloneablePathmindBusEvent cloneForEventBus();
}
