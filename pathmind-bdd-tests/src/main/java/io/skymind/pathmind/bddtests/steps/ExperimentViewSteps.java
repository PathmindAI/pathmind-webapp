package io.skymind.pathmind.bddtests.steps;

import io.skymind.pathmind.bddtests.page.experimentview.ExperimentViewBottomPanel;
import io.skymind.pathmind.bddtests.page.experimentview.ExperimentViewHeader;
import io.skymind.pathmind.bddtests.page.experimentview.ExperimentViewMiddlePanel;
import net.thucydides.core.annotations.Step;

import java.io.IOException;

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

    @Step
    public void experimentPageCheckSimulationMetrics(String slot, String commaSeparatedMetrics) {
        experimentViewMiddlePanel.experimentPageCheckSimulationMetrics(slot, commaSeparatedMetrics);
    }

    @Step
    public void experimentPageCheckSimulationMetricIsChosen(String slot, String rewardVar, boolean chosen) {
        experimentViewMiddlePanel.experimentPageCheckSimulationMetricIsChosen(slot, rewardVar, chosen);
    }

    @Step
    public void experimentPageCheckObservationIsChecked(String slot, String observation, boolean checked) {
        experimentViewMiddlePanel.experimentPageCheckObservationIsChecked(slot, observation, checked);
    }

    @Step
    public void experimentPageCheckRewardFunction(String slot, String rewardFunctionFilePath) throws IOException {
        experimentViewBottomPanel.experimentPageCheckRewardFunction(slot, rewardFunctionFilePath);
    }

    @Step
    public void experimentPageSlotClickRewardVariable(String slot, String rewardVar) {
        experimentViewMiddlePanel.experimentPageSlotClickRewardVariable(slot, rewardVar);
    }

    @Step
    public void experimentPageSlotCheckRewardVariableIsChosen(String slot, String rewardVar, boolean chosen) {
        experimentViewMiddlePanel.experimentPageSlotCheckRewardVariableIsChosen(slot, rewardVar, chosen);
    }

    @Step
    public void experimentPageCheckObservationIsHighlighted(String slot, String observation, boolean highlighted) {
        experimentViewMiddlePanel.experimentPageCheckObservationIsHighlighted(slot, observation, highlighted);
    }

    @Step
    public void experimentPageCheckRewardVariableIsHighlighted(String slot, String observation, boolean highlighted) {
        experimentViewMiddlePanel.experimentPageCheckRewardVariableIsHighlighted(slot, observation, highlighted);
    }
}