package io.skymind.pathmind.bus;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventBus {
    private static Logger log = LogManager.getLogger(EventBus.class);

    private static final EventBus EVENT_BUS = new EventBus();

    private Map<BusEventType, List<EventBusSubscriber>> subscribers;

    private EventBus() {
        Map<BusEventType, List<EventBusSubscriber>> subscribersModifiable = new HashMap<>();
        Arrays.stream(BusEventType.values()).forEach(
                busEventType -> subscribersModifiable.put(busEventType, new CopyOnWriteArrayList<EventBusSubscriber>()));
        subscribers = Collections.unmodifiableMap(subscribersModifiable);
    }

    public static void post(PathmindBusEvent event) {
        log.info("............... POST ................." + event.getEventType().name());
        EVENT_BUS.subscribers.get(event.getEventType()).stream().forEach(subscriber -> {
            if (subscriber != null && subscriber.isFiltered(event) && subscriber.getUI().isPresent())
                new Thread(() -> subscriber.handleEvent(event)).start();
        });
    }

    public static void subscribe(EventBusSubscriber subscriber) {
        log.info("............... subscribe ................." + subscriber.getClass().getName());
        EVENT_BUS.subscribers.get(subscriber.getEventType()).add(subscriber);
    }

    public static void unsubscribe(EventBusSubscriber subscriber) {
        log.info("............... unsubscribe ................." + subscriber.getClass().getName());
        EVENT_BUS.subscribers.get(subscriber.getEventType()).remove(subscriber);
    }

}