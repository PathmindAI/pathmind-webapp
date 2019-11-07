package io.skymind.pathmind.bus;

import com.vaadin.flow.component.UI;

import java.util.Optional;

public interface EventBusSubscriber<T>
{
    BusEventType getEventType();
    void handleEvent(T event);
    Optional<UI> getUI();

    default boolean isFiltered(T event) {
        return true;
    }
}