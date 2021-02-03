package io.skymind.pathmind.webapp.ui.layouts.components;

import io.skymind.pathmind.shared.constants.RunStatus;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Run;
import io.skymind.pathmind.webapp.ui.components.atoms.StatusIcon;
import io.skymind.pathmind.webapp.ui.karibu.KaribuUtils;
import io.skymind.pathmind.webapp.ui.mocks.ExperimentMock;
import io.skymind.pathmind.webapp.ui.mocks.RunMock;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StatusIconTest {

    private Experiment mockExperiment = new ExperimentMock();
    private Run mockRun = new RunMock();

    @Before
    public void setUp() {
        KaribuUtils.setup();
    }

    @After
    public void tearDown() {
        KaribuUtils.tearDown();
    }

    @Test
    public void StatusIconNotStartedTest() {
        StatusIcon statusIcon = new StatusIcon(mockExperiment);
        assertEquals("pencil", statusIcon.getStatusIconType());
    }

    @Test
    public void StatusIconStartingTest() {
        mockExperiment.addRun(mockRun);
        mockExperiment.setTrainingStatusEnum(RunStatus.Starting);
        StatusIcon statusIcon = new StatusIcon(mockExperiment);
        assertEquals("loading", statusIcon.getStatusIconType());
    }

    @Test
    public void StatusIconRunningTest() {
        mockExperiment.addRun(mockRun);
        mockExperiment.setTrainingStatusEnum(RunStatus.Running);
        StatusIcon statusIcon = new StatusIcon(mockExperiment);
        assertEquals("loading", statusIcon.getStatusIconType());
    }
    
    @Test
    public void StatusIconRestartingTest() {
        mockExperiment.addRun(mockRun);
        mockExperiment.setTrainingStatusEnum(RunStatus.Restarting);
        StatusIcon statusIcon = new StatusIcon(mockExperiment);
        assertEquals("loading", statusIcon.getStatusIconType());
    }
    
    @Test
    public void StatusIconCompletingTest() {
        mockExperiment.addRun(mockRun);
        mockExperiment.setTrainingStatusEnum(RunStatus.Completing);
        StatusIcon statusIcon = new StatusIcon(mockExperiment);
        assertEquals("loading", statusIcon.getStatusIconType());
    }
    
    @Test
    public void StatusIconCompletedTest() {
        mockExperiment.addRun(mockRun);
        mockExperiment.setTrainingStatusEnum(RunStatus.Completed);
        StatusIcon statusIcon = new StatusIcon(mockExperiment);
        assertEquals("check", statusIcon.getStatusIconType());
    }
    
    @Test
    public void StatusIconKilledTest() {
        mockExperiment.addRun(mockRun);
        mockExperiment.setTrainingStatusEnum(RunStatus.Killed);
        StatusIcon statusIcon = new StatusIcon(mockExperiment);
        assertEquals("stopped", statusIcon.getStatusIconType());
    }
    
    @Test
    public void StatusIconStoppingTest() {
        mockExperiment.addRun(mockRun);
        mockExperiment.setTrainingStatusEnum(RunStatus.Stopping);
        StatusIcon statusIcon = new StatusIcon(mockExperiment);
        assertEquals("stopped", statusIcon.getStatusIconType());
    }
    
    @Test
    public void StatusIconErrorTest() {
        mockExperiment.addRun(mockRun);
        mockExperiment.setTrainingStatusEnum(RunStatus.Error);
        StatusIcon statusIcon = new StatusIcon(mockExperiment);
        assertEquals("exclamation", statusIcon.getStatusIconType());
    }

}