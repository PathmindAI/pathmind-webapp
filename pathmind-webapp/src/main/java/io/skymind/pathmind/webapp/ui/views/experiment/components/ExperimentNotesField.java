package io.skymind.pathmind.webapp.ui.views.experiment.components;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;

import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.webapp.bus.EventBus;
import io.skymind.pathmind.webapp.bus.events.view.ExperimentChangedViewBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.view.ExperimentChangedViewSubscriber;
import io.skymind.pathmind.webapp.ui.components.molecules.NotesField;
import io.skymind.pathmind.webapp.ui.utils.PushUtils;

public class ExperimentNotesField extends NotesField {

    private Supplier<Optional<UI>> getUISupplier;
    private Experiment experiment;

    public ExperimentNotesField(Supplier<Optional<UI>> getUISupplier, String title, Experiment experiment, Consumer<String> saveConsumer) {
        super(title, experiment.getUserNotes(), saveConsumer);
        this.getUISupplier = getUISupplier;
        this.experiment = experiment;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        EventBus.subscribe(this,
                new ExperimentNotesFieldExperimentChangedViewSubscriber(getUISupplier));
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        EventBus.unsubscribe(this);
    }

    class ExperimentNotesFieldExperimentChangedViewSubscriber extends ExperimentChangedViewSubscriber {

        public ExperimentNotesFieldExperimentChangedViewSubscriber(Supplier<Optional<UI>> getUISupplier) {
            super(getUISupplier);
        }

        @Override
        public void handleBusEvent(ExperimentChangedViewBusEvent event) {
            PushUtils.push(getUiSupplier(), () -> setNotesText(event.getExperiment().getUserNotes()));
        }
    }

}
