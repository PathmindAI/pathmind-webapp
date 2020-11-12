package io.skymind.pathmind.webapp.ui.views.model.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.SerializableConsumer;
import io.skymind.pathmind.shared.data.Observation;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.BOLD_LABEL;

public class ObservationsPanel extends VerticalLayout {

    private ObservationsTable observationsTable;
    private List<Observation> allObservations;

    public ObservationsPanel(List<Observation> observations, Boolean hideCheckboxes) {
        this(observations, null, true, true);
    }

    public ObservationsPanel(List<Observation> observations, List<Observation> selectedObservations, Boolean isReadOnly, Boolean hideCheckboxes) {
        this.allObservations = observations;
        
        add(LabelFactory.createLabel("Observations", BOLD_LABEL));
        
        if (hideCheckboxes) {
            add(createObservationsList());
        } else {
            observationsTable = new ObservationsTable(isReadOnly);
            add(getObservationsPanel(isReadOnly));
            setupObservationTable(selectedObservations);
        }

        setWidthFull();
        setPadding(false);
        setSpacing(false);
    }

    private void setupObservationTable(Collection<Observation> selection) {
        observationsTable.setItems(new HashSet<>(allObservations));
        if (selection == null || selection.isEmpty()) {
            setSelectedObservations(allObservations);
        } else {
            setSelectedObservations(selection);
        }
    }

    private Component createObservationsList() {
        HorizontalLayout wrapper = WrapperUtils.wrapWidthFullHorizontalNoSpacingAlignCenter();
        allObservations.forEach(observation -> {
            wrapper.add(LabelFactory.createLabel(observation.getVariable(), "observation-label"));
        });
        return wrapper;
    }

    public List<Observation> getSelectedObservations() {
        return new ArrayList<>(observationsTable.getValue());
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
}
