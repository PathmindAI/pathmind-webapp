package io.skymind.pathmind.bddtests.steps;

import net.thucydides.core.annotations.Step;
import io.skymind.pathmind.bddtests.page.AccountPage;

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
}
