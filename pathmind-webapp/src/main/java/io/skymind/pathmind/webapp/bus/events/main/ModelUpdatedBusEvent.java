package io.skymind.pathmind.webapp.bus.events.main;

import io.skymind.pathmind.shared.data.Model;
import io.skymind.pathmind.webapp.bus.BusEventType;
import io.skymind.pathmind.webapp.bus.PathmindBusEvent;

public class ModelUpdatedBusEvent implements PathmindBusEvent {

    public enum ModelUpdateType {
        ModelDataUpdate,
        Archive
    }

    private Model model;
    private ModelUpdateType modelUpdateType;

    public ModelUpdatedBusEvent(Model model) {
        this(model, ModelUpdateType.ModelDataUpdate);
    }

    public ModelUpdatedBusEvent(Model model, ModelUpdateType modelUpdateType) {
        this.model = model;
        this.modelUpdateType = modelUpdateType;
    }

    @Override
    public BusEventType getEventType() {
        return BusEventType.ModelUpdate;
    }

    public Model getModel() {
        return model;
    }

    public long getProjectId() {
        return model.getProjectId();
    }

    public boolean isArchiveEventType() {
        return ModelUpdateType.Archive.equals(modelUpdateType);
    }

    public ModelUpdateType getModelUpdateType() {
        return modelUpdateType;
    }

    @Override
    public ModelUpdatedBusEvent cloneForEventBus() {
        return new ModelUpdatedBusEvent(model.deepClone(), modelUpdateType);
    }
}
