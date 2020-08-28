package io.skymind.pathmind.webapp.ui.views.model.components;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import io.skymind.pathmind.shared.data.Observation;

@CssImport(value = "./styles/components/observations-table.css")
public class ObservationsTable extends VerticalLayout {

	private List<Observation> observationsList = new ArrayList<>();
    private CheckboxGroup<Observation> checkboxGroup = new CheckboxGroup<>();
    private Set<Observation> checkboxGroupItems;

	public ObservationsTable() {
        setPadding(false);
        setSpacing(false);
	    setClassName("observations-table");
        
        Checkbox checkboxSelectAll = new Checkbox("Select All");
        checkboxGroup.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);
        checkboxGroup.addValueChangeListener(event -> {
            if (event.getValue().size() == observationsList.size()) {
                checkboxSelectAll.setValue(true);
                checkboxSelectAll.setIndeterminate(false);
            } else if (event.getValue().size() == 0) {
                checkboxSelectAll.setValue(false);
                checkboxSelectAll.setIndeterminate(false);
            } else
                checkboxSelectAll.setIndeterminate(true);

        });
        checkboxSelectAll.addValueChangeListener(event -> {
            if (checkboxSelectAll.getValue()) {
                checkboxGroup.setValue(checkboxGroupItems);
            } else {
                checkboxGroup.deselectAll();
            }
        });
        add(checkboxSelectAll, checkboxGroup);
    }
    
    public void setObservations(List<Observation> observations) {
        observationsList = observations;
        checkboxGroupItems = new LinkedHashSet<>(observationsList);
        checkboxGroup.setItems(checkboxGroupItems);
        checkboxGroup.setItemLabelGenerator(Observation::getVariable);
    }

    public List<Observation> getObservations() {
        return observationsList;
    }
}
