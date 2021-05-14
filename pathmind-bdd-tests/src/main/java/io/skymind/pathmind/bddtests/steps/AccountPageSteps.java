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

    @Step
    public void checkAccountPageFooterComponents() {
        accountPage.checkAccountPageFooterComponents();
    }

    @Step
    public void clickAccountFooterBtn(String btn) {
        accountPage.clickAccountFooterBtn(btn);
    }

    @Step
    public void clickAccountPageApiCopyBtnAndPasteToTheSearchField() {
        accountPage.clickAccountPageApiCopyBtnAndPasteToTheSearchField();
    }

    @Step
    public void clickAccessTokenRotateBtnAndCheckThatTokenChanged() {
        accountPage.clickAccessTokenRotateBtnAndCheckThatTokenChanged();
    }

    @Step
    public void accountPageAccessTokenCheckTokenExpires(String expiresDays) {
        accountPage.accountPageAccessTokenCheckTokenExpires(expiresDays);
    }

    @Step
    public void checkSubscriptionPlansPage() {
        accountPage.checkSubscriptionPlansPage();
    }

    @Step
    public void checkSubscriptionPlansUpgradePage() {
        accountPage.checkSubscriptionPlansUpgradePage();
    }

    @Step
    public void fillPaymentFormWithStripeTestCard() {
        accountPage.fillPaymentFormWithStripeTestCard();
    }

    @Step
    public void paymentPageClickUpgradeBtn() {
        accountPage.paymentPageClickUpgradeBtn();
    }

    @Step
    public void checkAccountSubscriptionIs(String subscription) {
        accountPage.checkAccountSubscriptionIs(subscription);
    }

    @Step
    public void checkUpgradedToProfessionalPageIsShown() {
        accountPage.checkUpgradedToProfessionalPageIsShown();
    }

    @Step
    public void checkCancelSubscriptionPopUp() {
        accountPage.checkCancelSubscriptionPopUp();
    }

    @Step
    public void clickPopUpDialogYesCancel() {
        accountPage.clickPopUpDialogYesCancel();
    }

    @Step
    public void checkAccountSubscriptionHint() {
        accountPage.checkAccountSubscriptionHint();
    }
}
