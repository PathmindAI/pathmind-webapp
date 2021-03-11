package io.skymind.pathmind.webapp.ui.components.observations;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Observation;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.views.experiment.components.ExperimentComponent;

import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.BOLD_LABEL;

public class ObservationsViewOnlyPanel extends VerticalLayout implements ExperimentComponent {

    private Experiment experiment;
    private List<Observation> modelObservations;
    private List<Observation> selectedObservations;
    private List<Observation> comparisonModeTheOtherSelectedObservations;
    private List<Checkbox> checkboxList = new ArrayList<>();

    public ObservationsViewOnlyPanel(List<Observation> modelObservations) {
        this.modelObservations = modelObservations;
        add(LabelFactory.createLabel("Observations", BOLD_LABEL));
        createCheckboxes();

        setWidthFull();
        setPadding(false);
        setSpacing(false);
        addClassName("observations-panel");
    }

    private void createCheckboxes() {
        modelObservations.forEach(obs -> {
            Checkbox checkbox = new Checkbox();
            checkbox.setLabel(obs.getVariable());
            checkbox.setEnabled(false);
            checkboxList.add(checkbox);
            add(checkbox);
        });
    }

    public void setSelectedCheckboxes() {
        checkboxList.forEach(checkbox -> checkbox.setValue(false));
        selectedObservations.forEach(selectedObs -> {
            checkboxList
                .stream()
                .filter(checkbox -> checkbox.getLabel().equals(selectedObs.getVariable()))
                .findFirst()
                .ifPresent(checkbox -> checkbox.setValue(true));
        });
    }

    public void highlightDiff() {
        String highlightClassName = "highlight-label";
        if (selectedObservations != null && comparisonModeTheOtherSelectedObservations != null) {
            List<Observation> differentStatusObs;
            List<Observation> secondSelectedObsList;
            if (selectedObservations.size() >= comparisonModeTheOtherSelectedObservations.size()) {
                differentStatusObs = new ArrayList<>(selectedObservations);
                secondSelectedObsList = new ArrayList<>(comparisonModeTheOtherSelectedObservations);
                secondSelectedObsList.removeAll(differentStatusObs);
                differentStatusObs.removeAll(comparisonModeTheOtherSelectedObservations);
            } else {
                differentStatusObs = new ArrayList<>(comparisonModeTheOtherSelectedObservations);
                secondSelectedObsList = new ArrayList<>(selectedObservations);
                secondSelectedObsList.removeAll(differentStatusObs);
                differentStatusObs.removeAll(selectedObservations);
            }
            differentStatusObs.addAll(secondSelectedObsList);
            checkboxList.forEach(checkbox -> checkbox.removeClassName(highlightClassName));
            differentStatusObs.forEach(obs -> {
                checkboxList
                    .stream()
                    .filter(checkbox -> checkbox.getLabel().equals(obs.getVariable()))
                    .findFirst()
                    .ifPresent(checkbox -> checkbox.addClassName(highlightClassName));
            });
        }
    }

    public void setComparisonModeTheOtherSelectedObservations(List<Observation> comparisonModeTheOtherSelectedObservations) {
        this.comparisonModeTheOtherSelectedObservations = comparisonModeTheOtherSelectedObservations;
        highlightDiff();
    }

    @Override
    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
        this.selectedObservations = experiment.getSelectedObservations();
        setSelectedCheckboxes();
        highlightDiff();
    }

    @Override
    public void updateExperiment() {
        setSelectedCheckboxes();
    }

}
