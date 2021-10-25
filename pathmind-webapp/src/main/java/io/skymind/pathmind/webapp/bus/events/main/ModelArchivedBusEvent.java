package io.skymind.pathmind.webapp.bus.events.main;

import io.skymind.pathmind.shared.data.Model;
import io.skymind.pathmind.webapp.bus.BusEventType;
import io.skymind.pathmind.webapp.bus.PathmindBusEvent;

public class ModelArchivedBusEvent implements PathmindBusEvent {

    private Model model;

    public ModelArchivedBusEvent(Model model) {
        this.model = model;
    }

    @Override
    public BusEventType getEventType() {
        return BusEventType.ModelArchived;
    }

    public Model getModel() {
        return model;
    }

    @Override
    public ModelArchivedBusEvent cloneForEventBus() {
        return new ModelArchivedBusEvent(model.deepClone());
    }
}
