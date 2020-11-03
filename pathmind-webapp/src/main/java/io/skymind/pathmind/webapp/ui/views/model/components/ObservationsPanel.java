package io.skymind.pathmind.webapp.ui.views.model.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.SerializableConsumer;
import io.skymind.pathmind.shared.data.Observation;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.BOLD_LABEL;

public class ObservationsPanel extends VerticalLayout {
    private ObservationsTable observationsTable;

    public ObservationsPanel() {
        this(false);
    }

    public ObservationsPanel(Boolean isReadOnly) {
        observationsTable = new ObservationsTable(isReadOnly);

        add(LabelFactory.createLabel("Observations", BOLD_LABEL));
        add(getObservationsPanel(isReadOnly));

        setWidthFull();
        setPadding(false);
        setSpacing(false);
    }

    public void setupObservationTable(Collection<Observation> allObservations, Collection<Observation> selection) {
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

}
