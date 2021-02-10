package io.skymind.pathmind.webapp.bus.events.main;

import io.skymind.pathmind.webapp.bus.BusEventType;
import io.skymind.pathmind.webapp.bus.PathmindBusEvent;

/**
 * Event used to indicate whether or not an experiment has been starred or not. We don't
 * send the whole experiment because we only need to know the state.
 */
public class ExperimentFavoriteBusEvent implements PathmindBusEvent {

    private long experimentId;
    private boolean isFavorite;

    public ExperimentFavoriteBusEvent(long experimentId, boolean isFavorite) {
        this.experimentId = experimentId;
        this.isFavorite = isFavorite;
    }

    @Override
    public BusEventType getEventType() {
        return BusEventType.ExperimentFavorite;
    }

    public long getExperimentId() {
        return experimentId;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    @Override
    public ExperimentFavoriteBusEvent cloneForEventBus() {
        // Creating new instances just as an added safety in case anyone where to modify the values.
        return new ExperimentFavoriteBusEvent(experimentId, isFavorite);
    }
}
