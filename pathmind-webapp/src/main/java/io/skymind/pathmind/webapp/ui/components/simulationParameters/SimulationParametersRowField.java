package io.skymind.pathmind.webapp.ui.components.simulationParameters;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import io.skymind.pathmind.shared.data.SimulationParameter;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;

public class SimulationParametersRowField extends HorizontalLayout {
    
    private SimulationParameter simulationParameter;

    private Span nameSpan;

    public SimulationParametersRowField(SimulationParameter simulationParameter, 
                                        Boolean isReadOnly) {
        setSimulationParameter(simulationParameter);
        nameSpan = LabelFactory.createLabel(simulationParameter.getKey(), "simulation-parameter-name");
        add(nameSpan);
    }

    public SimulationParameter getSimulationParameter() {
        return simulationParameter;
    }

    public void setSimulationParameter(SimulationParameter simulationParameter) {
        this.simulationParameter = simulationParameter;
    }

}
