package io.skymind.pathmind.webapp.bus.subscribers;

import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.UI;
import io.skymind.pathmind.webapp.bus.EventBus;
import io.skymind.pathmind.webapp.bus.EventBusSubscriber;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Supplier;

public interface EventBusSubscriberComponent extends HasElement {

    public Supplier<Optional<UI>> getUISupplier();

    public default void addEventBusSubscribers(EventBusSubscriber subscriber) {
        EventBus.subscribe(getElement().getComponent().get(), getUISupplier(), subscriber);
    }

    public default void addEventBusSubscribers(EventBusSubscriber... subscribers) {
        Arrays.stream(subscribers).forEach(subscriber -> addEventBusSubscribers(subscriber));
    }
}
