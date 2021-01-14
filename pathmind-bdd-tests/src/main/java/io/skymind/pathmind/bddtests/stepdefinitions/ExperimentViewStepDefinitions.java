package io.skymind.pathmind.bddtests.stepdefinitions;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.skymind.pathmind.bddtests.steps.ExperimentViewSteps;
import net.thucydides.core.annotations.Steps;

public class ExperimentViewStepDefinitions {

    @Steps
    private ExperimentViewSteps experimentViewSteps;

    @When("^Experiment page Check '(.*)' experiment-header '(.*)', '(.*)', Stop Training btn shown '(.*)', Share with support btn shown '(.*)', Share with support label shown '(.*)'$")
    public void experimentViewCheckExperimentHeader(String slot, String header, String status, boolean stopTrainingBtn, boolean shareWithSpBtn, boolean shareWithSpLabel) {
        experimentViewSteps.experimentViewCheckExperimentHeader(slot, header, status, stopTrainingBtn, shareWithSpBtn, shareWithSpLabel);
    }

    @Then("^Experiment page Check '(.*)' middle panel$")
    public void experimentPageCheckMiddlePanel(String slot) {
        experimentViewSteps.experimentPageCheckMiddlePanel(slot);
    }

    @Then("^Experiment page Check '(.*)' bottom panel$")
    public void experimentPageCheckBottomPanel(String slot) {
        experimentViewSteps.experimentPageCheckBottomPanel(slot);
    }
}