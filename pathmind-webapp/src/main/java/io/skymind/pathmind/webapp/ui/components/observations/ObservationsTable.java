package io.skymind.pathmind.webapp.ui.components.observations;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.skymind.pathmind.shared.data.Observation;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;

@CssImport(value = "./styles/components/observations-table.css")
public class ObservationsTable extends CustomField<Set<Observation>> implements HasStyle {

    private Set<Observation> observationsList = new LinkedHashSet<>();
    private List<Observation> comparisonModeTheOtherSelectedObservations;
    private CheckboxGroup<Observation> checkboxGroup = new CheckboxGroup<>();
    private String highlightClassName = "highlight-label";

    public ObservationsTable(Boolean isReadOnly) {
        VerticalLayout container = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing();
        container.setClassName("observations-table");

        Checkbox checkboxSelectAll = new Checkbox("Select All");
        checkboxSelectAll.addClassName("select-all");
        checkboxGroup.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);
        checkboxGroup.addValueChangeListener(event -> {
            if (event.getValue().size() == observationsList.size()) {
                checkboxSelectAll.setValue(true);
                checkboxSelectAll.setIndeterminate(false);
            } else if (event.getValue().size() == 0) {
                checkboxSelectAll.setValue(false);
                checkboxSelectAll.setIndeterminate(false);
            } else {
                checkboxSelectAll.setIndeterminate(true);
            }
            setValue(event.getValue());
            highlightDiff();
        });
        checkboxSelectAll.addValueChangeListener(event -> {
            if (checkboxSelectAll.getValue()) {
                checkboxGroup.select(observationsList);
            } else {
                checkboxGroup.deselectAll();
            }
        });
        container.add(checkboxSelectAll, checkboxGroup);

        if (isReadOnly) {
            checkboxSelectAll.setVisible(false);
            checkboxGroup.setReadOnly(true);
        }
        add(container);
    }

    public void setItems(LinkedHashSet<Observation> observations) {
        observationsList = observations;
        checkboxGroup.setItems(observationsList);
        checkboxGroup.setItemLabelGenerator(Observation::getVariable);
    }

    @Override
    protected Set<Observation> generateModelValue() {
        return checkboxGroup.getSelectedItems();
    }

    @Override
    protected void setPresentationValue(Set<Observation> newPresentationValue) {
        checkboxGroup.setValue(newPresentationValue);
    }

    public void setComparisonModeTheOtherSelectedObservations(List<Observation> comparisonModeTheOtherSelectedObservations) {
        this.comparisonModeTheOtherSelectedObservations = comparisonModeTheOtherSelectedObservations;
        if (comparisonModeTheOtherSelectedObservations == null) {
            unhighlight();
        } else {
            highlightDiff();
        }
    }

    public void highlightDiff() {
        List<Observation> selectedObservations = new ArrayList<Observation>();
        selectedObservations.addAll(checkboxGroup.getSelectedItems());
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
            unhighlight();
            differentStatusObs.addAll(secondSelectedObsList);
            differentStatusObs.forEach(obs -> {
                checkboxGroup.getChildren()
                    .filter(checkbox -> checkbox.getElement().getText().equals(obs.getVariable()))
                    .findFirst()
                    .ifPresent(checkbox -> checkbox.getElement().getClassList().add(highlightClassName));
            });
        }
    }

    public void unhighlight() {
        checkboxGroup.getChildren().forEach(checkbox -> checkbox.getElement().getClassList().remove(highlightClassName));
    }

}
