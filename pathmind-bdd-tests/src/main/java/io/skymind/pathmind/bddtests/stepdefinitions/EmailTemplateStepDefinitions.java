package io.skymind.pathmind.bddtests.stepdefinitions;

import cucumber.api.java.en.When;
import io.skymind.pathmind.bddtests.EmailApi;

public class EmailTemplateStepDefinitions {

    private EmailApi emailApi;

    @When("^Check user verification email$")
    public void clickInNavigationIcon() {
        emailApi.checkUserVerificationEmail();
    }
}
