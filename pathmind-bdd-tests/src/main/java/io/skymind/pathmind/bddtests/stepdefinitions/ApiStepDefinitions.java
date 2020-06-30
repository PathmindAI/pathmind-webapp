package io.skymind.pathmind.bddtests.stepdefinitions;

import cucumber.api.java.en.When;
import io.skymind.pathmind.bddtests.steps.ApiSteps;
import net.thucydides.core.annotations.Steps;

public class ApiStepDefinitions {

    @Steps
    private ApiSteps apiSteps;

    @When("^Trigger API new version notification$")
    public void triggerApiNewVersionNotification() {
        apiSteps.triggerApiNewVersionNotification();
    }
}
