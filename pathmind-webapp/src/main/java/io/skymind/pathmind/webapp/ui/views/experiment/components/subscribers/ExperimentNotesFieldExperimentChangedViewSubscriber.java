package io.skymind.pathmind.webapp.ui.views.experiment.components.subscribers;

import java.util.Optional;
import java.util.function.Supplier;

import com.vaadin.flow.component.UI;

import io.skymind.pathmind.webapp.bus.events.view.ExperimentChangedViewBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.view.ExperimentChangedViewSubscriber;
import io.skymind.pathmind.webapp.ui.utils.PushUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.components.ExperimentNotesField;

public class ExperimentNotesFieldExperimentChangedViewSubscriber extends ExperimentChangedViewSubscriber {

    private ExperimentNotesField experimentNotesField;

    public ExperimentNotesFieldExperimentChangedViewSubscriber(Supplier<Optional<UI>> getUISupplier, ExperimentNotesField experimentNotesField) {
        super(getUISupplier);
        this.experimentNotesField = experimentNotesField;
    }

    @Override
    public void handleBusEvent(ExperimentChangedViewBusEvent event) {
        PushUtils.push(getUiSupplier(), () -> experimentNotesField.setNotesText(event.getExperiment().getUserNotes()));
    }

}