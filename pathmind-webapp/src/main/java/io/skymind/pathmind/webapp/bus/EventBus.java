package io.skymind.pathmind.webapp.bus;

import com.vaadin.flow.component.Component;
import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.shared.data.Run;
import io.skymind.pathmind.webapp.bus.events.PolicyUpdateBusEvent;
import io.skymind.pathmind.webapp.bus.events.RunUpdateBusEvent;
import org.springframework.security.concurrent.DelegatingSecurityContextExecutorService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * For now I've implemented a custom EventBus for several reasons. Should we need to extend the EventBus then we should
 * go with a standard library/framework. In the meantime however this will cover our needs, is very simple, and gives us several big
 * performance advantages as a side effect.
 * <p>
 * Firstly the EventBus is a singleton. In terms of data structure the EventBus is basically a HashMap of EventTypes which
 * then contain a list of all the Subscribers for each EventType.
 * <p>
 * To keep things very simple, and based on our expected usage patterns, I specifically selected the data structures to
 * be thread safe. The Map is an UnModifiableMap because once we create a Map entry for each EventType we should never
 * need to modify the Map again. It could be a ConcurrentHashMap but there's no real advantage since it's not
 * modifiable. The list of subscribers uses a CopyOnWriteArrayList so that it can be thread safe. This way if a subscriber
 * unsubscribes in the middle of an action then that list entry will just be null rather than throw an Exception. As a result
 * we do have to check in that the subscriber is not null before we can fire an event. And since it's a copy of the subscriber
 * the worse case is fire an event to a screen that has unsubscribed (which is not that bad) but even so it should not be possible
 * since that can only be done on a detach, and if that's the case then the getUI().isPresent() check will fail and
 * the event will NOT be fired.
 * <p>
 * Since it's a custom EventBus we can then benefit from filtering on the event BEFORE the event is fired, saving us from creating
 * extra threads only to have all the views/components filter after the fact, not to mention saving us from creating a bunch
 * of threads. And since we're creating threads to fire events (so that there is no blocking) we can just stream through
 * the subscriber list. The subscriber list should be short. If it ever becomes a problem we can always parallelStream
 * but for now I didn't think that was worth it.
 * <p>
 * This type of thread pool is used as recommended by the API: "These pools will typically improve the performance of
 * programs that execute many short-lived asynchronous tasks". The ExecutorService.shutdown() method is not hooked in
 * since there's no point because the only time we'll need this is when the server is shutting down, at which point
 * it's going to be shutting down hard anyways.
 */
public class EventBus {
    private static final EventBus EVENT_BUS = new EventBus();
    private static ConcurrentHashMap<Component, List<EventBusSubscriber>> componentSubscribers = new ConcurrentHashMap<>();

    private static final ExecutorService EXECUTOR_SERVICE = new DelegatingSecurityContextExecutorService(Executors.newCachedThreadPool());

    private Map<BusEventType, List<EventBusSubscriber>> subscribers;

    private EventBus() {
        Map<BusEventType, List<EventBusSubscriber>> subscribersModifiable = new HashMap<>();
        Arrays.stream(BusEventType.values()).forEach(
                busEventType -> subscribersModifiable.put(busEventType, new CopyOnWriteArrayList<EventBusSubscriber>()));
        subscribers = Collections.unmodifiableMap(subscribersModifiable);
    }

    /**
     * The order of the if statements is selected based on a combination of what I expect to be the most common and/or
     * most performance if checks.
     */
    public static void post(PathmindBusEvent event) {
        EVENT_BUS.subscribers.get(event.getEventType()).stream()
                .filter(subscriber -> subscriber.filterSameUI(event) && subscriber.filterBusEvent(event) && subscriber.isAttached())
                .forEach(subscriber -> {
                    EXECUTOR_SERVICE.execute(() -> subscriber.handleBusEvent(event));
        });
    }

    /**
     * The reason for the first single EventBusSubscriber is so that we can get a compile time error in case someone forgets to add one.
     */
    public static void subscribe(Component component, EventBusSubscriber eventBusSubscriber, EventBusSubscriber... eventBusSubscribers) {
        List<EventBusSubscriber> subscribers = new ArrayList<>(Arrays.asList(eventBusSubscribers));
        subscribers.add(eventBusSubscriber);
        componentSubscribers.put(component, subscribers);
        subscribers.forEach(subscriber -> subscribe(subscriber));
    }

    private static void subscribe(EventBusSubscriber subscriber) {
        EVENT_BUS.subscribers.get(subscriber.getEventType()).add(subscriber);
    }

    public static void unsubscribe(Component component) {
        List<EventBusSubscriber> subscribers = componentSubscribers.get(component);
        componentSubscribers.remove(component);
        if(subscribers != null)
            subscribers.forEach(subscriber -> unsubscribe(subscriber));
    }

    private static void unsubscribe(EventBusSubscriber subscriber) {
        EVENT_BUS.subscribers.get(subscriber.getEventType()).remove(subscriber);
    }
}