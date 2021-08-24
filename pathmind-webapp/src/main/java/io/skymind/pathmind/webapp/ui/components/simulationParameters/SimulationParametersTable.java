package io.skymind.pathmind.webapp.ui.components.simulationParameters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import io.skymind.pathmind.shared.constants.ParamType;
import io.skymind.pathmind.shared.data.SimulationParameter;
import io.skymind.pathmind.webapp.ui.utils.GuiUtils;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;

@CssImport(value = "./styles/components/simulation-parameters-table.css")
public class SimulationParametersTable extends CustomField<Set<SimulationParameter>> {

    private VerticalLayout container;
    private Set<SimulationParameter> simulationParameters = new HashSet<SimulationParameter>();
    private Boolean isReadOnly = false;

    public SimulationParametersTable(Boolean isReadOnly) {
        this.isReadOnly = isReadOnly;
        container = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing();
        container.setClassName("simulation-parameters-table");

        add(container);
    }

    public void setSimulationParameters(List<SimulationParameter> simulationParameters) {
        Collections.sort(simulationParameters, Comparator.comparing(SimulationParameter::getIndex));
        this.simulationParameters = new HashSet<SimulationParameter>(simulationParameters);

        container.removeAll();

        HorizontalLayout headerRow = WrapperUtils.wrapWidthFullHorizontal(new Span("Name"), new Span("Value"));
        headerRow.addClassName("header-row");
        GuiUtils.removeMarginsPaddingAndSpacing(headerRow);
        container.add(headerRow);

        simulationParameters.forEach(simulationParam -> {
            SimulationParametersRowField row = new SimulationParametersRowField(simulationParam, isReadOnly ? true : isReadOnly(simulationParam));
            container.add(row);
        });
    }

    public void setSimulationParameters(Set<SimulationParameter> simulationParameters) {
        setSimulationParameters(new ArrayList<SimulationParameter>(simulationParameters));
    }

    @Override
    protected Set<SimulationParameter> generateModelValue() {
        return simulationParameters;
    }

    @Override
    protected void setPresentationValue(Set<SimulationParameter> newPresentationValue) {
        setSimulationParameters(newPresentationValue);
    }

    private boolean isReadOnly(SimulationParameter simulationParameter) {
        if (simulationParameter.getType().equals(ParamType.OTHERS.getValue()) ||
            simulationParameter.getType().equals(ParamType.STRING.getValue()) && simulationParameter.getValue().equals("NULL_VALUE")) {
            return true;
        }
        return false;
    }
}
