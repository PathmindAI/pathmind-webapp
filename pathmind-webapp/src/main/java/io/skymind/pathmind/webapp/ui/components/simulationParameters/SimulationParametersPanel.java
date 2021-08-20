package io.skymind.pathmind.webapp.ui.components.simulationParameters;

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

    private List<SimulationParameter> simulationParameters;

    public SimulationParametersPanel(Boolean isReadOnly) {
        add(LabelFactory.createLabel("Simulation Parameters", BOLD_LABEL));
        setupSimulationParametersTable(isReadOnly);
        add(simulationParametersTable);
        addClassName("simulation-parameters-panel");

        addClassName("readonly");

        setWidthFull();
        setPadding(false);
        setSpacing(false);
    }

    private void setupSimulationParametersTable(Boolean isReadOnly) {
        simulationParametersTable = new SimulationParametersTable(isReadOnly);
    }

    public List<SimulationParameter> getSimulationParameters() {
        return this.simulationParameters;
    }

    @Override
    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
        this.simulationParameters = experiment.getSimulationParameters();
        simulationParametersTable.setSimulationParameters(simulationParameters);
    }

    @Override
    public void updateExperiment() {
        experiment.setSimulationParameters(getSimulationParameters());
    }
}
