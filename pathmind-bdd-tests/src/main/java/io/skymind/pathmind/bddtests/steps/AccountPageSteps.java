package io.skymind.pathmind.bddtests.steps;

import io.skymind.pathmind.bddtests.page.AccountPage;
import net.thucydides.core.annotations.Step;

public class AccountPageSteps {

    private AccountPage accountPage;

    @Step
    public void checkThatAccountPageOpened() {
        accountPage.checkThatAccountPageOpened();
    }

    @Step
    public void clickAccountEditBtn() {
        accountPage.clickAccountEditBtn();
    }

    @Step
    public void inputNewEmail(String email) {
        accountPage.inputNewEmail(email);
    }

    @Step
    public void clickAccountEditUpdateBtn() {
        accountPage.clickAccountEditUpdateBtn();
    }

    @Step
    public void checkUserEmailIsCorrect(String email) {
        accountPage.checkUserEmailIsCorrect(email);
    }

    @Step
    public void saveAccountPageApiKeyToTheEnvironmentVariable() {
        accountPage.saveAccountPageApiKeyToTheEnvironmentVariable();
    }

    @Step
    public void inputAccountPageFirstName(String firstName) {
        accountPage.inputAccountPageFirstName(firstName);
    }

    @Step
    public void inputAccountPageLastName(String lastName) {
        accountPage.inputAccountPageLastName(lastName);
    }
}
