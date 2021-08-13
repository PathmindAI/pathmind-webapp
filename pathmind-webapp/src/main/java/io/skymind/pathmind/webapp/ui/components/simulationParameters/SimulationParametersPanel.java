package io.skymind.pathmind.webapp.ui.components.simulationParameters;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.SimulationParameter;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.views.experiment.components.ExperimentComponent;

import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.BOLD_LABEL;

public class SimulationParametersPanel extends VerticalLayout implements ExperimentComponent {

    private Experiment experiment;

    private SimulationParametersTable simulationParametersTable;

    private List<SimulationParameter> simulationParameters;

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
            34972L, 32228L, 0,
            "usePolicy", "true", 0));
        simulationParameters.add(new SimulationParameter(
            34972L, 32228L, 1,
            "maxRawWaiting", "5.0", 2));
        simulationParameters.add(new SimulationParameter(
            34972L, 32228L, 2,
            "testOthersType", "something else", 4));
        simulationParameters.add(new SimulationParameter(
            34972L, 32228L, 3,
            "numberAGVs", "3", 1));
        simulationParameters.add(new SimulationParameter(
            34972L, 32228L, 4,
            "aString", "some text here", 3));
        return simulationParameters;
    }

    public List<SimulationParameter> getSimulationParameters() {
        //return new ArrayList<>(simulationParametersTable.getValue());
        return this.simulationParameters;
    }

    @Override
    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
        this.simulationParameters = experiment.getSimulationParameters();
        this.simulationParameters.forEach(param -> param.setExperimentId(this.experiment.getId()));;
        // setSimulationParameters(experiment.getSimulationParameters());
    }

    @Override
    public void updateExperiment() {
        experiment.setSimulationParameters(getSimulationParameters());
    }
}
