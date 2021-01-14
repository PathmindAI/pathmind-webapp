package io.skymind.pathmind.bddtests.stepdefinitions;

import cucumber.api.java.en.When;
import io.skymind.pathmind.bddtests.steps.ExperimentViewSteps;
import net.thucydides.core.annotations.Steps;

public class ExperimentViewStepDefinitions {

    @Steps
    private ExperimentViewSteps experimentViewSteps;

    @When("^Experiment page check '(.*)' experiment-header '(.*)', '(.*)', Stop Training shown '(.*)', Share with support shown '(.*)'$")
    public void experimentViewCheckExperimentHeader(String slot, String header, String status, boolean stopTrainingBtn, boolean shareWithSpBtn) {
        experimentViewSteps.experimentViewCheckExperimentHeader(slot, header, status, stopTrainingBtn, shareWithSpBtn);
    }
}