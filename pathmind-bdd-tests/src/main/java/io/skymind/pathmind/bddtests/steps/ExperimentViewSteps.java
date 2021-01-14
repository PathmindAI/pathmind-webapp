package io.skymind.pathmind.bddtests.steps;

import io.skymind.pathmind.bddtests.page.experimentview.ExperimentViewBottomPanel;
import io.skymind.pathmind.bddtests.page.experimentview.ExperimentViewHeader;
import io.skymind.pathmind.bddtests.page.experimentview.ExperimentViewMiddlePanel;
import net.thucydides.core.annotations.Step;

public class ExperimentViewSteps {

    private ExperimentViewHeader experimentViewHeader;
    private ExperimentViewMiddlePanel experimentViewMiddlePanel;
    private ExperimentViewBottomPanel experimentViewBottomPanel;

    @Step
    public void experimentViewCheckExperimentHeader(String slot, String header, String status, boolean stopTrainingBtn, boolean shareWithSpBtn, boolean shareWithSpLabel) {
        experimentViewHeader.experimentViewCheckExperimentHeader(slot, header, status, stopTrainingBtn, shareWithSpBtn, shareWithSpLabel);
    }

    @Step
    public void experimentPageCheckMiddlePanel(String slot) {
        experimentViewMiddlePanel.experimentPageCheckMiddlePanel(slot);
    }

    @Step
    public void experimentPageCheckBottomPanel(String slot) {
        experimentViewBottomPanel.experimentPageCheckBottomPanel(slot);
    }
}