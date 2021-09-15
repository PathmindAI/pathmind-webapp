package io.skymind.pathmind.webapp.ui.components.simulationParameters;

import java.util.List;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.PathmindUser;
import io.skymind.pathmind.shared.data.SimulationParameter;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.views.experiment.AbstractExperimentView;
import io.skymind.pathmind.webapp.ui.views.experiment.components.ExperimentComponent;

import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.BOLD_LABEL;

public class SimulationParametersPanel extends VerticalLayout implements ExperimentComponent {

    private Experiment experiment;

    private SimulationParametersTable simulationParametersTable;

    private List<SimulationParameter> simulationParameters;

    private AbstractExperimentView abstractExperimentView;

    private SegmentIntegrator segmentIntegrator;

    public SimulationParametersPanel(AbstractExperimentView abstractExperimentView, boolean isReadOnly, 
            PathmindUser currentUser, SegmentIntegrator segmentIntegrator) {
        this.abstractExperimentView = abstractExperimentView;
        this.segmentIntegrator = segmentIntegrator;
        add(LabelFactory.createLabel("Simulation Parameters", BOLD_LABEL));
        setupSimulationParametersTable(isReadOnly, currentUser);
        add(simulationParametersTable);
        addClassName("simulation-parameters-panel");

        if (isReadOnly) {
            addClassName("readonly");
        }

        setWidthFull();
        setPadding(false);
        setSpacing(false);
    }

    private void setupSimulationParametersTable(boolean isReadOnly, PathmindUser currentUser) {
        simulationParametersTable = new SimulationParametersTable(isReadOnly, currentUser, segmentIntegrator);
    }

    public List<SimulationParameter> getSimulationParameters() {
        return this.simulationParameters;
    }

    public void setComparisonModeTheOtherParameters(List<SimulationParameter> comparisonSimulationParameters) {
        simulationParametersTable.setComparisonParameters(comparisonSimulationParameters);
    }

    @Override
    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
        this.simulationParameters = experiment.getSimulationParameters();
        simulationParametersTable.setSimulationParameters(simulationParameters, abstractExperimentView.getModelSimulationParameters());
    }

    @Override
    public void updateExperiment() {
        experiment.setSimulationParameters(getSimulationParameters());
    }
}
