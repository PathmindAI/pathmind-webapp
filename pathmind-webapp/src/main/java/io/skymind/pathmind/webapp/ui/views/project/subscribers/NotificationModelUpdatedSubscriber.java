package io.skymind.pathmind.webapp.ui.views.project.subscribers;

import com.vaadin.flow.component.UI;

import io.skymind.pathmind.shared.data.Model;
import io.skymind.pathmind.webapp.bus.BusEventType;
import io.skymind.pathmind.webapp.bus.EventBusSubscriber;
import io.skymind.pathmind.webapp.bus.events.main.ModelUpdatedBusEvent;
import io.skymind.pathmind.webapp.data.utils.ModelUtils;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class NotificationModelUpdatedSubscriber extends EventBusSubscriber<ModelUpdatedBusEvent> {
    
    private List<Model> models;
    private Model model;

    public NotificationModelUpdatedSubscriber(Supplier<Optional<UI>> getUISupplier, List<Model> models, Model model) {
        super(getUISupplier);
        this.models = models;
        this.model = model;
    }

    public NotificationModelUpdatedSubscriber(Supplier<Optional<UI>> getUISupplier, boolean isListenForEventOnSameUI) {
        super(getUISupplier, isListenForEventOnSameUI);
    }

    public void handleBusEvent(ModelUpdatedBusEvent event) {
        // We need to update the internal models list for the navigation logic.
        ModelUtils.updateModelInModelsList(models, event.getModel());
    }

    @Override
    public BusEventType getEventType() {
        return BusEventType.ModelUpdate;
    }

    @Override
    public boolean filterBusEvent(ModelUpdatedBusEvent event) {
        return ModelUtils.isSameModel(event.getModel(), model.getId()) && event.isArchiveEventType();
    }
}
