package io.skymind.pathmind.webapp.bus.subscribers.main;

import io.skymind.pathmind.webapp.bus.BusEventType;
import io.skymind.pathmind.webapp.bus.EventBusSubscriber;
import io.skymind.pathmind.webapp.bus.events.main.ExperimentFavoriteBusEvent;

public abstract class ExperimentFavoriteSubscriber extends EventBusSubscriber<ExperimentFavoriteBusEvent> {

    public ExperimentFavoriteSubscriber() {
        super(true);
    }

    @Override
    public BusEventType getEventType() {
        return BusEventType.ExperimentFavorite;
    }
}
