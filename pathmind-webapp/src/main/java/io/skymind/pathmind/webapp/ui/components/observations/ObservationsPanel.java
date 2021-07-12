package io.skymind.pathmind.webapp.ui.components.observations;

import java.util.ArrayList;
import java.util.LinkedHashSet;
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
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.NewExperimentView;
import io.skymind.pathmind.webapp.ui.views.experiment.actions.newExperiment.NeedsSavingAction;
import io.skymind.pathmind.webapp.ui.views.experiment.components.ExperimentComponent;
import org.springframework.util.CollectionUtils;

import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.BOLD_LABEL;

public class ObservationsPanel extends VerticalLayout implements ExperimentComponent {

    private ObservationsTable observationsTable;
    // Only used for ExperimentView and NewExperimentView and NOT ProjectView - for the changed experiment logic in the subscriber
    private Experiment experiment;
    private List<Observation> selectedObservations;

    private NewExperimentView newExperimentView;
    private boolean isEnableValueChangeListener = true;

    /**
     * For ProjectView only
      */
    public ObservationsPanel(List<Observation> modelObservations) {
        this(modelObservations, true, true, null);
    }

    /**
     * For ExperimentView only.
     */
    public ObservationsPanel(List<Observation> modelObservations, Boolean isReadOnly) {
        this(modelObservations, isReadOnly, false, null);
    }

    /**
     * For NewExperimentView only.
     */
    public ObservationsPanel(List<Observation> modelObservations, Boolean isReadOnly, NewExperimentView newExperimentView) {
        this(modelObservations, isReadOnly, false, newExperimentView);
    }
    public ObservationsPanel(List<Observation> modelObservations, Boolean isReadOnly, Boolean hideCheckboxes, NewExperimentView newExperimentView) {

        this.newExperimentView = newExperimentView;

        observationsTable = new ObservationsTable(isReadOnly);

        add(LabelFactory.createLabel("Observations", BOLD_LABEL));

        modelObservations = modelObservations.stream()
            .filter(obs -> !obs.getVariable().equals(Observation.ACTION_MASKING))
            .collect(Collectors.toList());
        
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

        addObservationsTableValueChangeListener();
    }

    private void addObservationsTableValueChangeListener() {
        // This is only used for the NewExperimentView. The check needs to be here because we add it again on setSelectedObservations().
        if(newExperimentView != null) {
            addValueChangeListener(evt -> NeedsSavingAction.setNeedsSaving(newExperimentView));
        }
    }

    private void setupObservationTable(List<Observation> modelObservations, List<Observation> selectedObservations) {
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

    public void setSelectedObservations(List<Observation> observations) {
        this.selectedObservations = observations;
        // REFACTOR -> I'm not sure how the value listener is all fired but ideally we'd want something like either
        // isUserOriginated() on the event which is possible in Vaadin or we need to confirm that the observations value are different
        // then the selected observations. That being said I'm not sure where the magic happens because I couldn't find a binder, or locate
        // where the selectedObservations list is automatically updated, but if we can adjust that it would be easier. Either way we
        // should look at the different options. Although the current solution is hacky in my opinion it works for now. Otherwise what happens is that
        // on the set method the value change listener is fired which in turn causes.
        isEnableValueChangeListener = false;
        observationsTable.setValue(new LinkedHashSet<>(observations));
        isEnableValueChangeListener = true;
    }

    public void addValueChangeListener(SerializableConsumer<Set<Observation>> listener) {
        observationsTable.addValueChangeListener(evt -> {
            if(isEnableValueChangeListener) {
                listener.accept(evt.getValue());
            }
        });
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
    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
        setSelectedObservations(experiment.getSelectedObservations());
    }

    @Override
    public void updateExperiment() {
        experiment.setSelectedObservations(getSelectedObservations());
    }
}
