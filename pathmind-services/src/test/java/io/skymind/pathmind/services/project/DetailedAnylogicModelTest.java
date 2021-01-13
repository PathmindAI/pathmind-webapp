package io.skymind.pathmind.services.project;

import java.io.File;

import io.skymind.pathmind.services.project.AnyLogicModelInfo.ExperimentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static junit.framework.TestCase.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class DetailedAnylogicModelTest {
    @InjectMocks
    AnylogicFileChecker anylogicFileChecker;
    private StatusUpdater statusUpdater = new MockObjectStatusUpdater();

    @Test
    public void testNormal() {
        // This model is from the tutorial https://help.pathmind.com/en/articles/4004788-simple-stochastic
        File modelFile = new File("./src/test/resources/model/1.simple_normal.zip");
        AnylogicFileCheckResult fileCheckResult = (AnylogicFileCheckResult) anylogicFileChecker.performFileCheck(statusUpdater, modelFile);
        assertEquals(fileCheckResult.isFileCheckSuccessful(), true);
        assertEquals(fileCheckResult.getModelInfos().size(), 1);
        assertEquals(fileCheckResult.getPriorityModelInfo().getExperimentClass(), "simple_stochastic_model/Simulation");
        assertEquals(fileCheckResult.getPriorityModelInfo().getExperimentType(), ExperimentType.Simulation);
        assertEquals(fileCheckResult.getPriorityModelInfo().getMainAgentClass(), "simple_stochastic_model.Main");
        assertEquals(fileCheckResult.getDefinedHelpers().size(), 1);
        assertEquals(fileCheckResult.getDefinedHelpers().get(0), "simple_stochastic_model/Main##pathmindHelper");
    }

    @Test
    public void testSimulationName() {
        // this model has user-defined Simulation name:Simulation1
        File modelFile = new File("./src/test/resources/model/2.simple_simulation_name.zip");
        AnylogicFileCheckResult fileCheckResult = (AnylogicFileCheckResult) anylogicFileChecker.performFileCheck(statusUpdater, modelFile);
        assertEquals(fileCheckResult.isFileCheckSuccessful(), true);
        assertEquals(fileCheckResult.getModelInfos().size(), 1);
        assertEquals(fileCheckResult.getPriorityModelInfo().getExperimentClass(), "simple_stochastic_model/Simulation1");
        assertEquals(fileCheckResult.getPriorityModelInfo().getExperimentType(), ExperimentType.Simulation);
        assertEquals(fileCheckResult.getPriorityModelInfo().getMainAgentClass(), "simple_stochastic_model.Main");
        assertEquals(fileCheckResult.getDefinedHelpers().size(), 1);
        assertEquals(fileCheckResult.getDefinedHelpers().get(0), "simple_stochastic_model/Main##pathmindHelper");
    }

    @Test
    public void testPathmindHelperName() {
        // this model has user-defined Pathmind Helper name:pm
        File modelFile = new File("./src/test/resources/model/3.simple_helper_name.zip");
        AnylogicFileCheckResult fileCheckResult = (AnylogicFileCheckResult) anylogicFileChecker.performFileCheck(statusUpdater, modelFile);
        assertEquals(fileCheckResult.isFileCheckSuccessful(), true);
        assertEquals(fileCheckResult.getModelInfos().size(), 1);
        assertEquals(fileCheckResult.getPriorityModelInfo().getExperimentClass(), "simple_stochastic_model/Simulation");
        assertEquals(fileCheckResult.getPriorityModelInfo().getExperimentType(), ExperimentType.Simulation);
        assertEquals(fileCheckResult.getPriorityModelInfo().getMainAgentClass(), "simple_stochastic_model.Main");
        assertEquals(fileCheckResult.getDefinedHelpers().size(), 1);
        assertEquals(fileCheckResult.getDefinedHelpers().get(0), "simple_stochastic_model/Main##pm");
    }

    @Test
    public void testMainAgentName() {
        // this model has user-defined Main Agent name:Main2
        File modelFile = new File("./src/test/resources/model/4.simple_Main_name.zip");
        AnylogicFileCheckResult fileCheckResult = (AnylogicFileCheckResult) anylogicFileChecker.performFileCheck(statusUpdater, modelFile);
        assertEquals(fileCheckResult.isFileCheckSuccessful(), true);
        assertEquals(fileCheckResult.getModelInfos().size(), 1);
        assertEquals(fileCheckResult.getPriorityModelInfo().getExperimentClass(), "simple_stochastic_model/Simulation");
        assertEquals(fileCheckResult.getPriorityModelInfo().getExperimentType(), ExperimentType.Simulation);
        assertEquals(fileCheckResult.getPriorityModelInfo().getMainAgentClass(), "simple_stochastic_model.Main2");
        assertEquals(fileCheckResult.getDefinedHelpers().size(), 1);
        assertEquals(fileCheckResult.getDefinedHelpers().get(0), "simple_stochastic_model/Main2##pathmindHelper");
    }

    @Test
    public void testTwoPathmindHelpers() {
        // this model has two Pathmind Helpers:pathmindHelper, pathmindHelper1
        File modelFile = new File("./src/test/resources/model/5.simple_two_helpers.zip");
        AnylogicFileCheckResult fileCheckResult = (AnylogicFileCheckResult) anylogicFileChecker.performFileCheck(statusUpdater, modelFile);
        assertEquals(fileCheckResult.isFileCheckSuccessful(), false);
        assertEquals(fileCheckResult.getModelInfos().size(), 1);
        assertEquals(fileCheckResult.getPriorityModelInfo().getExperimentClass(), "simple_stochastic_model/Simulation");
        assertEquals(fileCheckResult.getPriorityModelInfo().getExperimentType(), ExperimentType.Simulation);
        assertEquals(fileCheckResult.getPriorityModelInfo().getMainAgentClass(), "simple_stochastic_model.Main");
        assertEquals(fileCheckResult.getDefinedHelpers().size(), 2);
        assertEquals(fileCheckResult.getDefinedHelpers().get(0), "simple_stochastic_model/Main##pathmindHelper");
        assertEquals(fileCheckResult.getDefinedHelpers().get(1), "simple_stochastic_model/Main##pathmindHelper1");
    }

    @Test
    public void testRLExperimentExportedFromNormal() {
        // this model has RLExperiment from "export to standard java app"
        File modelFile = new File("./src/test/resources/model/6.simple_RLExperiment_exported_from_simulation.zip");
        AnylogicFileCheckResult fileCheckResult = (AnylogicFileCheckResult) anylogicFileChecker.performFileCheck(statusUpdater, modelFile);
        assertEquals(fileCheckResult.isFileCheckSuccessful(), true);
        assertEquals(fileCheckResult.getModelInfos().size(), 2);
        assertEquals(fileCheckResult.getPriorityModelInfo().getExperimentClass(), "simple_stochastic_model/Simulation");
        assertEquals(fileCheckResult.getPriorityModelInfo().getExperimentType(), ExperimentType.Simulation);
        assertEquals(fileCheckResult.getPriorityModelInfo().getMainAgentClass(), "simple_stochastic_model.Main");
        assertEquals(fileCheckResult.getDefinedHelpers().size(), 1);
        assertEquals(fileCheckResult.getDefinedHelpers().get(0), "simple_stochastic_model/Main##pathmindHelper");
    }

    @Test
    public void testRLExperimentExportedFromRLExperiment() {
        // this model has RLExperiment from "export to pathmind"
        File modelFile = new File("./src/test/resources/model/7.simple_RLExperiment_exported_from_RLExperiment.zip");
        AnylogicFileCheckResult fileCheckResult = (AnylogicFileCheckResult) anylogicFileChecker.performFileCheck(statusUpdater, modelFile);
        assertEquals(fileCheckResult.isFileCheckSuccessful(), true);
        assertEquals(fileCheckResult.getModelInfos().size(), 1);
        assertEquals(fileCheckResult.getPriorityModelInfo().getExperimentClass(), "simple_stochastic_model/RLExperiment");
        assertEquals(fileCheckResult.getPriorityModelInfo().getExperimentType(), ExperimentType.RLExperiment);
        assertEquals(fileCheckResult.getPriorityModelInfo().getMainAgentClass(), "simple_stochastic_model.Main");
        assertEquals(fileCheckResult.getDefinedHelpers().size(), 1);
        assertEquals(fileCheckResult.getDefinedHelpers().get(0), "simple_stochastic_model/Main##pathmindHelper");
    }

    @Test
    public void testRLExperimentNameExportedFromRLExperiment() {
        // this model has user-defined RLExperiment name from "export to pathmind":Simulation1
        File modelFile = new File("./src/test/resources/model/8.simple_RLExperiment_name_exported_from_RLExperiment.zip");
        AnylogicFileCheckResult fileCheckResult = (AnylogicFileCheckResult) anylogicFileChecker.performFileCheck(statusUpdater, modelFile);
        assertEquals(fileCheckResult.isFileCheckSuccessful(), true);
        assertEquals(fileCheckResult.getModelInfos().size(), 1);
        assertEquals(fileCheckResult.getPriorityModelInfo().getExperimentClass(), "simple_stochastic_model/Simulation1");
        assertEquals(fileCheckResult.getPriorityModelInfo().getExperimentType(), ExperimentType.RLExperiment);
        assertEquals(fileCheckResult.getPriorityModelInfo().getMainAgentClass(), "simple_stochastic_model.Main");
        assertEquals(fileCheckResult.getDefinedHelpers().size(), 1);
        assertEquals(fileCheckResult.getDefinedHelpers().get(0), "simple_stochastic_model/Main##pathmindHelper");
    }

    @Test
    public void testRLExperimentExportedFromRLExperimentBonsai() {
        // this model has RLExperiment from "export to bonsai"
        File modelFile = new File("./src/test/resources/model/9.simple_RLExperiment_exported_RLExperiment_bonsai.zip");
        AnylogicFileCheckResult fileCheckResult = (AnylogicFileCheckResult) anylogicFileChecker.performFileCheck(statusUpdater, modelFile);
        assertEquals(fileCheckResult.isFileCheckSuccessful(), false);
        assertEquals(fileCheckResult.getModelInfos().size(), 1);
        assertEquals(fileCheckResult.getPriorityModelInfo().getExperimentClass(), "simple_stochastic_model/RLExperiment");
        assertEquals(fileCheckResult.getPriorityModelInfo().getExperimentType(), ExperimentType.RLExperiment);
        assertEquals(fileCheckResult.getPriorityModelInfo().getMainAgentClass(), "simple_stochastic_model.Main");
        assertEquals(fileCheckResult.getDefinedHelpers().size(), 1);
        assertEquals(fileCheckResult.getDefinedHelpers().get(0), "simple_stochastic_model/Main##pathmindHelper");
    }

    @Test
    public void testTwoAgents() {
        // this model has two agents:Main, DummyAgent
        // The top level agent of Simulation is set to Main
        File modelFile = new File("./src/test/resources/model/10.simple_two_agents.zip");
        AnylogicFileCheckResult fileCheckResult = (AnylogicFileCheckResult) anylogicFileChecker.performFileCheck(statusUpdater, modelFile);
        assertEquals(fileCheckResult.isFileCheckSuccessful(), true);
        assertEquals(fileCheckResult.getModelInfos().size(), 1);
        assertEquals(fileCheckResult.getPriorityModelInfo().getExperimentClass(), "simple_stochastic_model/Simulation");
        assertEquals(fileCheckResult.getPriorityModelInfo().getExperimentType(), ExperimentType.Simulation);
        assertEquals(fileCheckResult.getPriorityModelInfo().getMainAgentClass(), "simple_stochastic_model.Main");
        assertEquals(fileCheckResult.getDefinedHelpers().size(), 1);
        assertEquals(fileCheckResult.getDefinedHelpers().get(0), "simple_stochastic_model/Main##pathmindHelper");
    }

    @Test
    public void testTwoSimulations() {
        // this model has two Simulations:Simulation, AnotherSimulation
        File modelFile = new File("./src/test/resources/model/11.simple_two_simulations.zip");
        AnylogicFileCheckResult fileCheckResult = (AnylogicFileCheckResult) anylogicFileChecker.performFileCheck(statusUpdater, modelFile);
        assertEquals(fileCheckResult.isFileCheckSuccessful(), true);
        assertEquals(fileCheckResult.getModelInfos().size(), 2);
        assertEquals(fileCheckResult.getPriorityModelInfo().getExperimentClass(), "simple_stochastic_model/Simulation");
        assertEquals(fileCheckResult.getPriorityModelInfo().getExperimentType(), ExperimentType.Simulation);
        assertEquals(fileCheckResult.getPriorityModelInfo().getMainAgentClass(), "simple_stochastic_model.Main");
        assertEquals(fileCheckResult.getDefinedHelpers().size(), 1);
        assertEquals(fileCheckResult.getDefinedHelpers().get(0), "simple_stochastic_model/Main##pathmindHelper");
    }

    @Test
    public void testTwoSimulationsNoValidSimulation() {
        // this model has two Simulations:Simulation1, AnotherSimulation
        File modelFile = new File("./src/test/resources/model/12.simple_two_simulations_invalid_name.zip");
        AnylogicFileCheckResult fileCheckResult = (AnylogicFileCheckResult) anylogicFileChecker.performFileCheck(statusUpdater, modelFile);
        assertEquals(fileCheckResult.isFileCheckSuccessful(), false);
        assertEquals(fileCheckResult.getModelInfos().size(), 2);
        assertEquals(fileCheckResult.getPriorityModelInfo(), null);
        assertEquals(fileCheckResult.getDefinedHelpers().size(), 1);
        assertEquals(fileCheckResult.getDefinedHelpers().get(0), "simple_stochastic_model/Main##pathmindHelper");
    }
}
