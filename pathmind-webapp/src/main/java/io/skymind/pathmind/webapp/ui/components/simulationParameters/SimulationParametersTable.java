package io.skymind.pathmind.webapp.ui.components.simulationParameters;

import java.util.List;
import java.util.Set;

import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.SimulationParameter;
import io.skymind.pathmind.webapp.ui.utils.GuiUtils;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;

public class SimulationParametersTable extends CustomField<Set<SimulationParameter>> {

    private VerticalLayout container;

    public SimulationParametersTable() {
        container = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing();
        container.setClassName("simulation-parameters-table");

        add(container);
    }

    public void setSimulationParameters() { // List<SimulationParameter> simulationParameters
        container.removeAll();

        HorizontalLayout headerRow = WrapperUtils.wrapWidthFullHorizontal(new Span("Name"), new Span("Value"));
        headerRow.addClassName("header-row");
        GuiUtils.removeMarginsPaddingAndSpacing(headerRow);
        container.add(headerRow);
    }

    @Override
    protected Set<SimulationParameter> generateModelValue() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void setPresentationValue(Set<SimulationParameter> arg0) {
        // TODO Auto-generated method stub
        
    }

}
