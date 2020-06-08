package io.skymind.pathmind.bddtests.stepdefinitions;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.skymind.pathmind.bddtests.EmailApi;
import io.skymind.pathmind.bddtests.steps.AccountPageSteps;
import net.serenitybdd.core.Serenity;
import net.thucydides.core.annotations.Steps;

public class AccountStepDefinitions {

    @Steps
    private AccountPageSteps accountPageSteps;
    @Steps
    private EmailApi emailApi;

    @Then("^Check that user account page opened$")
    public void checkThatUserAccountPageOpened() {
        accountPageSteps.checkThatAccountPageOpened();
    }

    @When("^Click account edit btn$")
    public void clickAccountEditBtn() {
        accountPageSteps.clickAccountEditBtn();
    }

    @When("^Input new email (.*)$")
    public void inputNewEmail(String email) {
        accountPageSteps.inputNewEmail(email);
    }

    @When("^Click account edit update btn$")
    public void clickAccountEditUpdateBtn() {
        accountPageSteps.clickAccountEditUpdateBtn();
    }

    @When("^Input account page new temp email$")
    public void inputAccountPageNewEmail() {
        Serenity.setSessionVariable("oldEmail").to(Serenity.sessionVariableCalled("email"));
        accountPageSteps.inputNewEmail(emailApi.getEmail());
    }

    @Then("^Check user email is correct$")
    public void checkUserEmailIsCorrect() {
        accountPageSteps.checkUserEmailIsCorrect(Serenity.sessionVariableCalled("email"));
    }
}
