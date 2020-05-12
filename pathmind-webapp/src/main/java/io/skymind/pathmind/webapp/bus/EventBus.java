package io.skymind.pathmind.webapp.bus;

import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.shared.data.Run;
import io.skymind.pathmind.webapp.bus.events.PolicyUpdateBusEvent;
import io.skymind.pathmind.webapp.bus.events.RunUpdateBusEvent;

import java.util.*;
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

    private Map<BusEventType, List<EventBusSubscriber>> subscribers;

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();

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
        EVENT_BUS.subscribers.get(event.getEventType()).stream().forEach(subscriber -> {
            if (subscriber != null && subscriber.filterBusEvent(event) && subscriber.isAttached())
                EXECUTOR_SERVICE.execute(() -> subscriber.handleBusEvent(event));
        });
    }

    public static void subscribe(EventBusSubscriber subscriber) {
        EVENT_BUS.subscribers.get(subscriber.getEventType()).add(subscriber);
    }

    public static void unsubscribe(EventBusSubscriber subscriber) {
        EVENT_BUS.subscribers.get(subscriber.getEventType()).remove(subscriber);
    }

    public static void fireEventBusUpdates(Run run, List<Policy> policies) {
        // An event for each policy since we only need to update some of the policies in a run.
        if (!policies.isEmpty()) {
            EventBus.post(new PolicyUpdateBusEvent(policies));
        }
        // Send run updated event, meaning that all policies under the run is updated.
        // This is needed especially in dashboard, to refresh the item only once per run, instead of after all policy updates
        EventBus.post(new RunUpdateBusEvent(run));
    }

}