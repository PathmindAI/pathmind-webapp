package io.skymind.pathmind.webapp.ui.views.model.components;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.SerializableConsumer;

import io.skymind.pathmind.db.dao.ObservationDAO;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Observation;
import io.skymind.pathmind.webapp.bus.EventBus;
import io.skymind.pathmind.webapp.bus.events.view.ExperimentChangedViewBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.view.ExperimentChangedViewSubscriber;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.utils.PushUtils;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.BOLD_LABEL;

public class ObservationsPanel extends VerticalLayout {

    private ObservationDAO observationDAO;
    private ObservationsTable observationsTable;
    private List<Observation> allObservations;

    private Supplier<Optional<UI>> getUISupplier;

    public ObservationsPanel(Supplier<Optional<UI>> getUISupplier, ObservationDAO observationDAO, List<Observation> modelObservations, List<Observation> experimentObservations) {
        this(getUISupplier, observationDAO, modelObservations, experimentObservations, false);
    }

    public ObservationsPanel(Supplier<Optional<UI>> getUISupplier, ObservationDAO observationDAO, List<Observation> modelObservations, List<Observation> experimentObservations, Boolean isReadOnly) {
        this.getUISupplier = getUISupplier;
        this.observationDAO = observationDAO;
        this.allObservations = modelObservations;
        observationsTable = new ObservationsTable(isReadOnly);

        add(LabelFactory.createLabel("Observations", BOLD_LABEL));
        add(getObservationsPanel(isReadOnly));

        setWidthFull();
        setPadding(false);
        setSpacing(false);
        setupObservationTable(experimentObservations);
    }

    public void setupObservationTable(Collection<Observation> selection) {
        observationsTable.setItems(new HashSet<>(allObservations));
        if (selection != null) {
            if (selection.isEmpty()) {
                setSelectedObservations(allObservations);
            } else {
                setSelectedObservations(selection);
            }
        }
    }

    public Collection<Observation> getSelectedObservations() {
        return observationsTable.getValue();
    }

    public void setSelectedObservations(Collection<Observation> observations) {
        observationsTable.setValue(new HashSet<>(observations));
    }

    public void addValueChangeListener(SerializableConsumer<Set<Observation>> listener) {
        observationsTable.addValueChangeListener(evt -> listener.accept(evt.getValue()));
    }

    private Component getObservationsPanel(Boolean isReadOnly) {
        VerticalLayout wrapper = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing();
        if (!isReadOnly) {
            wrapper.add(new Span("Select observations for this experiment"));
        }
        wrapper.add(observationsTable);
        wrapper.addClassName("observations-panel");
        return wrapper;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        EventBus.subscribe(this,
                new ObservationsPanelExperimentChangedViewSubscriber(getUISupplier));
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        EventBus.unsubscribe(this);
    }

    class ObservationsPanelExperimentChangedViewSubscriber extends ExperimentChangedViewSubscriber {

        public ObservationsPanelExperimentChangedViewSubscriber(Supplier<Optional<UI>> getUISupplier) {
            super(getUISupplier);
        }

        @Override
        public void handleBusEvent(ExperimentChangedViewBusEvent event) {
            Experiment newExperiment = event.getExperiment();
            PushUtils.push(getUiSupplier().get(), ui -> {
                    List<Observation> experimentObservations = observationDAO.getObservationsForExperiment(newExperiment.getId());
                    setSelectedObservations(experimentObservations);
            });
        }
    }

}
