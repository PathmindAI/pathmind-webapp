package io.skymind.pathmind.bddtests.stepdefinitions;

import cucumber.api.java.en.Then;
import io.skymind.pathmind.bddtests.steps.AccountPageSteps;
import net.thucydides.core.annotations.Steps;

public class AccountStepDefinitions {

    @Steps
    private AccountPageSteps accountPageSteps;

    @Then("^Check that user account page opened$")
    public void checkThatUserAccountPageOpened() {
        accountPageSteps.checkThatAccountPageOpened();
    }
}
