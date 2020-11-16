package io.skymind.pathmind.webapp.ui.views.model.components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.SerializableConsumer;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Observation;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import org.springframework.util.CollectionUtils;

import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.BOLD_LABEL;

public class ObservationsPanel extends VerticalLayout {

    private ObservationsTable observationsTable;
    // Only used for ExperimentView and NewExperimentView and NOT ProjectView - for the changed experiment logic in the subscriber
    private Experiment experiment;

    // For ProjectView only
    public ObservationsPanel(List<Observation> modelObservations, Boolean hideCheckboxes) {
        this(modelObservations, modelObservations, true, true);
    }

    public ObservationsPanel(Experiment experiment) {
        this(experiment.getModelObservations(), experiment.getSelectedObservations(), true, false);
        this.experiment = experiment;
    }

    public ObservationsPanel(Experiment experiment, Boolean isReadOnly) {
        this(experiment.getModelObservations(), experiment.getSelectedObservations(), isReadOnly, false);
        this.experiment = experiment;
    }

    public ObservationsPanel(List<Observation> modelObservatons, List<Observation> selectedObservations, Boolean isReadOnly, Boolean hideCheckboxes) {

        observationsTable = new ObservationsTable(isReadOnly);

        add(LabelFactory.createLabel("Observations", BOLD_LABEL));
        
        if (hideCheckboxes) {
            add(createObservationsList(modelObservatons));
        } else {
            observationsTable = new ObservationsTable(isReadOnly);
            add(getObservationsPanel(isReadOnly));
            setupObservationTable(modelObservatons, selectedObservations);
        }

        setWidthFull();
        setPadding(false);
        setSpacing(false);
    }

    private void setupObservationTable(List<Observation> modelObservations, Collection<Observation> selectedObservations) {
        observationsTable.setItems(new LinkedHashSet<>(modelObservations));
        setSelectedObservations(CollectionUtils.isEmpty(selectedObservations) ? modelObservations : selectedObservations);
    }

    private Component createObservationsList(List<Observation> modelObservations) {
        HorizontalLayout wrapper = WrapperUtils.wrapWidthFullHorizontalNoSpacingAlignCenter();
        modelObservations.forEach(observation -> {
            wrapper.add(LabelFactory.createLabel(observation.getVariable(), "observation-label"));
        });
        return wrapper;
    }

    public List<Observation> getSelectedObservations() {
        return new ArrayList<>(observationsTable.getValue());
    }

    public void setSelectedObservations(Collection<Observation> observations) {
        observationsTable.setValue(new LinkedHashSet<>(observations));
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

    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
    }

    public Experiment getExperiment() {
        return experiment;
    }
}
