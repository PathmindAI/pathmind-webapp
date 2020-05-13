package io.skymind.pathmind.bddtests.stepdefinitions;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.skymind.pathmind.bddtests.steps.NotFoundPageSteps;
import net.thucydides.core.annotations.Steps;

public class NotFoundPageStepDefinitions {
    @Steps
    private NotFoundPageSteps notFoundPageSteps;

    @Then("^Check that 404 page opened$")
    public void check404PageOpened(){
        notFoundPageSteps.check404PageOpened();
    }
}
