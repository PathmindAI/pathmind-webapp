package io.skymind.pathmind.webapp.ui.components.simulationParameters;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.SimulationParameter;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.views.experiment.components.ExperimentComponent;

import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.BOLD_LABEL;

public class SimulationParametersPanel extends VerticalLayout implements ExperimentComponent {

    private Experiment experiment;

    private SimulationParametersTable simulationParametersTable;

    public SimulationParametersPanel(Boolean isReadOnly) { //List<SimulationParameter> simulationParameters
        add(LabelFactory.createLabel("Simulation Parameters", BOLD_LABEL));
        setupSimulationParametersTable(isReadOnly);
        add(simulationParametersTable);
        addClassName("simulation-parameters-panel");

        if (isReadOnly) {
            addClassName("readonly");
        }

        setWidthFull();
        setPadding(false);
        setSpacing(false);
    }

    private void setupSimulationParametersTable(Boolean isReadOnly) {
        simulationParametersTable = new SimulationParametersTable(isReadOnly);
        simulationParametersTable.setSimulationParameters(getMockSimulationParameters());
    }

    private List<SimulationParameter> getMockSimulationParameters() {
        List<SimulationParameter> simulationParameters = new ArrayList<SimulationParameter>();
        simulationParameters.add(new SimulationParameter(
            34972L, 32228L, 0, 0,
            "usePolicy", "true"));
        simulationParameters.add(new SimulationParameter(
            34972L, 32228L, 1, 2,
            "maxRawWaiting", "5.0"));
        simulationParameters.add(new SimulationParameter(
            34972L, 32228L, 2, 4,
            "testOthersType", "something else"));
        simulationParameters.add(new SimulationParameter(
            34972L, 32228L, 3, 1,
            "numberAGVs", "3"));
        simulationParameters.add(new SimulationParameter(
            34972L, 32228L, 4, 3,
            "aString", "some text here"));
        return simulationParameters;
    }

    @Override
    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
        simulationParametersTable.setSimulationParameters(getMockSimulationParameters());
    }

    @Override
    public void updateExperiment() {
    }
}
