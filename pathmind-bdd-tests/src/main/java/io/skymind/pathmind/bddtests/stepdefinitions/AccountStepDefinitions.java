package io.skymind.pathmind.bddtests.stepdefinitions;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.skymind.pathmind.bddtests.EmailApi;
import io.skymind.pathmind.bddtests.steps.AccountPageSteps;
import io.skymind.pathmind.bddtests.steps.LoginPageSteps;
import net.serenitybdd.core.Serenity;
import net.thucydides.core.annotations.Steps;

import java.util.Date;

public class AccountStepDefinitions {

    @Steps
    private AccountPageSteps accountPageSteps;
    @Steps
    private EmailApi emailApi;
    @Steps
    private LoginPageSteps loginPageSteps;

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

    @Then("^Save account page api key to the environment variable$")
    public void saveAccountPageApiKeyToTheEnvironmentVariable() {
        accountPageSteps.saveAccountPageApiKeyToTheEnvironmentVariable();
    }

    @When("^Input account page first name '(.*)'$")
    public void inputAccountPageFirstName(String firstName) {
        Serenity.setSessionVariable("firstNameRandomNumber").to(new Date().getTime());
        accountPageSteps.inputAccountPageFirstName(firstName + Serenity.sessionVariableCalled("firstNameRandomNumber"));
    }

    @When("^Input account page last name '(.*)'$")
    public void inputAccountPageLastName(String lastName) {
        Serenity.setSessionVariable("lastNameRandomNumber").to(new Date().getTime());
        accountPageSteps.inputAccountPageLastName(lastName + Serenity.sessionVariableCalled("lastNameRandomNumber"));
    }

    @Then("^Check that user name changed to '(.*)' '(.*)'$")
    public void checkThatUserNameChangedTo(String firstName, String lastName) {
        loginPageSteps.checkHeaderUsername(firstName + Serenity.sessionVariableCalled("firstNameRandomNumber") + " " + lastName + Serenity.sessionVariableCalled("lastNameRandomNumber"));
    }

    @Then("^Check account page footer components$")
    public void checkAccountPageFooterComponents() {
        accountPageSteps.checkAccountPageFooterComponents();
    }

    @When("^Click account footer '(.*)' btn$")
    public void clickAccountFooterBtn(String btn) {
        accountPageSteps.clickAccountFooterBtn(btn);
    }

    @When("^Click account page api copy btn and paste to the search field$")
    public void clickAccountPageApiCopyBtnAndPasteToTheSearchField() {
        accountPageSteps.clickAccountPageApiCopyBtnAndPasteToTheSearchField();
    }

    @Then("^Click access token rotate btn and check that token changed$")
    public void clickAccessTokenRotateBtnAndCheckThatTokenChanged() {
        accountPageSteps.clickAccessTokenRotateBtnAndCheckThatTokenChanged();
    }

    @Then("^Account page access token check token expires '(.*)'$")
    public void accountPageAccessTokenCheckTokenExpires(String expiresDays) {
        accountPageSteps.accountPageAccessTokenCheckTokenExpires(expiresDays);
    }

    @Then("^Check subscription plans page$")
    public void checkSubscriptionPlansPage() {
        accountPageSteps.checkSubscriptionPlansPage();
    }

    @Then("^Check subscription plans upgrade page$")
    public void checkSubscriptionPlansUpgradePage() {
        accountPageSteps.checkSubscriptionPlansUpgradePage();
    }

    @When("^Fill payment form with stripe test card$")
    public void fillPaymentFormWithStripeTestCard() {
        accountPageSteps.fillPaymentFormWithStripeTestCard();
    }

    @When("^Payment page click Upgrade btn$")
    public void paymentPageClickUpgradeBtn() {
        accountPageSteps.paymentPageClickUpgradeBtn();
    }

    @When("^Check account subscription is (.*)")
    public void checkAccountSubscriptionIs(String subscription) {
        accountPageSteps.checkAccountSubscriptionIs(subscription);
    }

    @When("^Check Upgraded to Professional page is shown$")
    public void checkUpgradedToProfessionalPageIsShown() {
        accountPageSteps.checkUpgradedToProfessionalPageIsShown();
    }
}
