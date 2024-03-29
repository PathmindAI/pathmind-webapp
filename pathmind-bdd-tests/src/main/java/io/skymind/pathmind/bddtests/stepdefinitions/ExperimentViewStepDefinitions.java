package io.skymind.pathmind.bddtests.stepdefinitions;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.skymind.pathmind.bddtests.steps.ExperimentViewSteps;
import net.serenitybdd.core.Serenity;
import net.thucydides.core.annotations.Steps;

import java.io.IOException;

public class ExperimentViewStepDefinitions {

    @Steps
    private ExperimentViewSteps experimentViewSteps;

    @When("^Experiment page Check '(.*)' experiment-header '(.*)', '(.*)', Stop Training btn shown '(.*)', Share with support btn shown '(.*)', Share with support label shown '(.*)', experiment shared '(.*)'$")
    public void experimentViewCheckExperimentHeader(String slot, String header, String status, boolean stopTrainingBtn, boolean shareWithSpBtn, boolean shareWithSpLabel, boolean experimentShared) {
        experimentViewSteps.experimentViewCheckExperimentHeader(slot, header, status, stopTrainingBtn, shareWithSpBtn, shareWithSpLabel, experimentShared);
    }

    @Then("^Experiment page Check '(.*)' middle panel$")
    public void experimentPageCheckMiddlePanel(String slot) {
        experimentViewSteps.experimentPageCheckMiddlePanel(slot);
    }

    @Then("^Experiment page Check '(.*)' bottom panel$")
    public void experimentPageCheckBottomPanel(String slot) {
        experimentViewSteps.experimentPageCheckBottomPanel(slot);
    }

    @Then("^Experiment page Check '(.*)' simulation metrics (.*)$")
    public void experimentPageCheckSimulationMetrics(String slot, String commaSeparatedMetrics) {
        experimentViewSteps.experimentPageCheckSimulationMetrics(slot, commaSeparatedMetrics);
    }

    @Then("^Experiment page Check '(.*)' simulation metric '(.*)' is chosen '(.*)'$")
    public void experimentPageCheckSimulationMetricIsChosen(String slot, String rewardVar, boolean chosen) {
        experimentViewSteps.experimentPageCheckSimulationMetricIsChosen(slot, rewardVar, chosen);
    }

    @Then("^Experiment page Check '(.*)' observation '(.*)' is checked '(.*)'$")
    public void experimentPageCheckObservationIsChecked(String slot, String observation, boolean checked) {
        experimentViewSteps.experimentPageCheckObservationIsChecked(slot, observation, checked);
    }

    @Then("^Experiment page Check '(.*)' reward function '(.*)'$")
    public void experimentPageCheckRewardFunction(String slot, String rewardFunctionFilePath) throws IOException {
        experimentViewSteps.experimentPageCheckRewardFunction(slot, rewardFunctionFilePath);
    }

    @Then("^Experiment page Check '(.*)' reward function new '(.*)'$")
    public void experimentPageCheckRewardFunctionNew(String slot, String rewardFunctionFilePath) throws IOException {
        experimentViewSteps.experimentPageCheckRewardFunctionNew(slot, rewardFunctionFilePath);
    }

    @When("^Experiment page '(.*)' slot click reward variable '(.*)'$")
    public void experimentPageSlotClickRewardVariable(String slot, String rewardVar) {
        experimentViewSteps.experimentPageSlotClickRewardVariable(slot, rewardVar);
    }

    @When("^Experiment page '(.*)' slot check reward variable '(.*)' is chosen '(.*)'$")
    public void experimentPageSlotCheckRewardVariableIsChosen(String slot, String rewardVar, boolean chosen) {
        experimentViewSteps.experimentPageSlotCheckRewardVariableIsChosen(slot, rewardVar, chosen);
    }

    @Then("^Experiment page Check '(.*)' observation '(.*)' is highlighted '(.*)'$")
    public void experimentPageCheckObservationIsHighlighted(String slot, String observation, boolean highlighted) {
        experimentViewSteps.experimentPageCheckObservationIsHighlighted(slot, observation, highlighted);
    }

    @Then("^Experiment page Check '(.*)' reward variable '(.*)' is highlighted '(.*)'$")
    public void experimentPageCheckRewardVariableIsHighlighted(String slot, String observation, boolean highlighted) {
        experimentViewSteps.experimentPageCheckRewardVariableIsHighlighted(slot, observation, highlighted);
    }

    @Then("^Check reward variable is commented '(.*)'$")
    public void checkRewardVariableIs(String rewardFunction) {
        experimentViewSteps.checkRewardVariableIs(rewardFunction);
    }

    @When("^Check export policy filename '(.*)', '(.*)'$")
    public void checkExportPolicyFilename(String projectName, String filename) {
        projectName = projectName.charAt(0) + projectName.substring(1).toLowerCase();
        experimentViewSteps.checkExportPolicyFilename(projectName + Serenity.sessionVariableCalled("randomNumber") + filename);
    }
}
