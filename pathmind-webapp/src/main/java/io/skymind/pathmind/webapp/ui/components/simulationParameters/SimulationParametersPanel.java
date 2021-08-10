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

    public SimulationParametersPanel() { //List<SimulationParameter> simulationParameters
        add(LabelFactory.createLabel("Simulation Parameters", BOLD_LABEL));
        setupSimulationParametersTable();
        add(simulationParametersTable);
        addClassName("simulation-parameters-panel");

        setWidthFull();
        setPadding(false);
        setSpacing(false);
    }

    private void setupSimulationParametersTable() {
        simulationParametersTable = new SimulationParametersTable();
        simulationParametersTable.setSimulationParameters();
    }

    @Override
    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
        // setSimulationParameters(experiment.getSimulationParameters());
    }

    @Override
    public void updateExperiment() {
    }
}
