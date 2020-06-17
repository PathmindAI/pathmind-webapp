package io.skymind.pathmind.bddtests.stepdefinitions;

import cucumber.api.java.en.Then;
import io.skymind.pathmind.bddtests.steps.NotFoundPageSteps;
import net.thucydides.core.annotations.Steps;

public class NotFoundPageStepDefinitions {

    @Steps
    private NotFoundPageSteps notFoundPageSteps;

    @Then("^Check that '404' page opened$")
    public void check404PageOpened() {
        notFoundPageSteps.check404PageOpened();
    }

    @Then("^Check that Oops page opened$")
    public void checkThatOopsPageOpened() {
        notFoundPageSteps.checkThatOopsPageOpened();
    }

    @Then("^Check that Invalid data error page opened$")
    public void checkThatInvalidDataErrorPageOpened() {
        notFoundPageSteps.checkThatInvalidDataErrorPageOpened();
    }

    @Then("^Check that pathmind status page opened (.*)$")
    public void checkThatStatusPageOpened(String url) {
        notFoundPageSteps.checkThatStatusPageOpened(url);
    }
}
