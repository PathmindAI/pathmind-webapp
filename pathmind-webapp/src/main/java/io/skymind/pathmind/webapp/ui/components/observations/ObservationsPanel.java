package io.skymind.pathmind.webapp.ui.components.observations;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.SerializableConsumer;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Observation;
import io.skymind.pathmind.shared.utils.ObservationUtils;
import io.skymind.pathmind.webapp.bus.EventBus;
import io.skymind.pathmind.webapp.bus.events.view.experiment.ExperimentNeedsSavingViewBusEvent;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.components.ExperimentComponent;
import org.springframework.util.CollectionUtils;

import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.BOLD_LABEL;

public class ObservationsPanel extends VerticalLayout implements ExperimentComponent {

    private ObservationsTable observationsTable;
    // Only used for ExperimentView and NewExperimentView and NOT ProjectView - for the changed experiment logic in the subscriber
    private Experiment experiment;
    private List<Observation> selectedObservations;

    /**
     * For ProjectView only
      */
    public ObservationsPanel(List<Observation> modelObservations) {
        this(modelObservations, true, true);
    }

    /**
     * For Experiment views only.
     */
    public ObservationsPanel(List<Observation> modelObservations, Boolean isReadOnly) {
        this(modelObservations, isReadOnly, false);
    }

    public ObservationsPanel(List<Observation> modelObservations, Boolean isReadOnly, Boolean hideCheckboxes) {

        observationsTable = new ObservationsTable(isReadOnly);

        add(LabelFactory.createLabel("Observations", BOLD_LABEL));
        
        if (hideCheckboxes) {
            add(createObservationsList(modelObservations));
        } else {
            observationsTable = new ObservationsTable(isReadOnly);
            add(getObservationsPanel(isReadOnly));
            setupObservationTable(modelObservations, selectedObservations);
        }

        setWidthFull();
        setPadding(false);
        setSpacing(false);

        addValueChangeListener(evt -> {
            if(experiment == null)
                return;
            // This is only for the experiment views. In that case we want to make sure they are different, meaning it's due to a
            // user initiated action rather than switching experiments within the view.
            List<Observation> selectedObservationsFromEvent = evt.stream().collect(Collectors.toList());
            if(!ObservationUtils.areObservationsEqual(getSelectedObservations(), selectedObservationsFromEvent)) {
                experiment.setSelectedObservations(selectedObservationsFromEvent);
                EventBus.post(new ExperimentNeedsSavingViewBusEvent(experiment));
            }
        });
    }


    private void setupObservationTable(List<Observation> modelObservations, List<Observation> selectedObservations) {
        observationsTable.setItems(new HashSet<>(modelObservations));
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

    public void setSelectedObservations(List<Observation> observations) {
        this.selectedObservations = observations;
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

    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
        setSelectedObservations(experiment.getSelectedObservations());

    }

    @Override
    public void updateExperiment() {
        experiment.setSelectedObservations(getSelectedObservations());
    }
}
