package io.skymind.pathmind.webapp.bus;

import com.vaadin.flow.component.UI;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * Any View or Component that wants to subscribe to events needs to extend this class. However it is strongly
 * recommended to create an intermediate interface for the Subscriber such as PolicyUpdateSubscriber, etc. so that
 * we can easily differentiate what there are no errors or mixups with the EventType.
 * <p>
 * Then on the onAttach and onDetach methods you need to call EventBus.subscribe(this) and EventBus.unsubscribe(this) where
 * this is this EventBusSubscriber.
 * <p>
 * The filterBusEvent method is meant to pre-filter the events BEFORE they are fired to save from having to create
 * threads for each event that then filter whether or not to listen to the event.
 * <p>
 * I did try to add an attachToEventBus() method which would then getUI().ifPresent() where it would do
 * ui.addAttachListener(attach -> EventBus.subscriber(this)) and the same for detach but that didn't work
 * because the UI was always null at that point (creation), and if I did it in the onAttach() method then
 * there was no benefit.
 *
 * @param <T> Is the type of PathmindBusEvent.
 */
public abstract class EventBusSubscriber<T extends PathmindBusEvent> {

    private Supplier<Optional<UI>> uiSupplier;
    private boolean isListenForEventOnSameUI;

    public abstract BusEventType getEventType();

    public abstract void handleBusEvent(T event);

    /**
     * This is already supplied by the component or view so this method should NOT be implemented. That being said it's
     * needed by the EventBus so that we can filter on whether the component has UI. This is important because a screen
     * may no longer be viewable, etc. and as a result we do not want to fire events to views and components that are still
     * subscribed but that do not have a UI.
     */
    public boolean isAttached() {
        return getUiSupplier().get().isPresent();
    }

    public EventBusSubscriber(Supplier<Optional<UI>> uiSupplier) {
        this(uiSupplier, false);
    }

    public EventBusSubscriber(Supplier<Optional<UI>> uiSupplier, boolean isListenForEventOnSameUI) {
        this.uiSupplier = uiSupplier;
        this.isListenForEventOnSameUI = isListenForEventOnSameUI;
    }

    /**
     * This is used to filter events BEFORE they are fired. By default nothing is filtered but in most cases you will
     * want to filter the events to prevent events that are not interesting from being fired and hence creating un-needed
     * threads. For example this could be a PolicyUpdateEvent where you filter on the Experiment (for the Experiment View)
     * or on the Policy for the PolicyStatusDetailsPanel (which is on the Experiment view).
     */
    public boolean filterBusEvent(T event) {
        return true;
    }

    public boolean isSourceSameUI(T event) {
        if(event.getSourceId() < 0 || uiSupplier.get().isEmpty())
            return false;
        return event.getSourceId() == uiSupplier.get().get().getUIId();
    }

    public Supplier<Optional<UI>> getUiSupplier() {
        return uiSupplier;
    }

    public boolean filterSameUI(T event) {
        if(!isListenForEventOnSameUI)
            return true;
        return isSourceSameUI(event);
    }
}