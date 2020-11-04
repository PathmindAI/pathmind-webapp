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

// TODO -> This may not be needed. Currently it is not in use. If this is needed, it should be refactored.
public class NotificationModelUpdatedSubscriber extends EventBusSubscriber<ModelUpdatedBusEvent> {
    
    private Model model;

    public NotificationModelUpdatedSubscriber(Supplier<Optional<UI>> getUISupplier, Model model) {
        super(getUISupplier);
        this.model = model;
    }

    public NotificationModelUpdatedSubscriber(Supplier<Optional<UI>> getUISupplier) {
        super(getUISupplier);
    }

    public void handleBusEvent(ModelUpdatedBusEvent event) {
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
