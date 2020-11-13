package io.skymind.pathmind.webapp.ui.views.model.components;

import java.util.HashSet;
import java.util.Set;

import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.skymind.pathmind.shared.data.Observation;

@CssImport(value = "./styles/components/observations-table.css")
public class ObservationsTable extends CustomField<Set<Observation>> implements HasStyle {

    private Set<Observation> observationsList = new HashSet<>();
    private CheckboxGroup<Observation> checkboxGroup = new CheckboxGroup<>();

    public ObservationsTable(Boolean isReadOnly) {
        VerticalLayout container = new VerticalLayout();
        container.setPadding(false);
        container.setSpacing(false);
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

    public void setItems(Set<Observation> observations) {
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

}
