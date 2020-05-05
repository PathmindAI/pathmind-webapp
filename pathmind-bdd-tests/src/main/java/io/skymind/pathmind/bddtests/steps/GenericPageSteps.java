package io.skymind.pathmind.bddtests.steps;

import io.skymind.pathmind.bddtests.page.GenericPage;
import net.thucydides.core.annotations.Step;

public class GenericPageSteps {

    private GenericPage genericPage;

    @Step
    public void checkThatButtonExists(String buttonText) {
        genericPage.checkThatButtonExists(buttonText);
    }
    @Step
    public void checkThatButtonDoesntExist(String buttonText) {
        genericPage.checkThatButtonDoesntExist(buttonText);
    }
    @Step
    public void clickInButton(String buttonText) {
        genericPage.clickInButton(buttonText);
    }
    @Step
    public void checkThatTheConfirmationDialogIsShown(String confirmationDialogHeader) {
        genericPage.checkThatTheConfirmationDialogIsShown(confirmationDialogHeader);
    }
    @Step
    public void checkThatNotificationIsShown(String notificationText) {
        genericPage.checkThatNotificationIsShown(notificationText);
    }
    @Step
    public void inConfirmationDialogClickInButton(String buttonText) {
        genericPage.inConfirmationDialogClickInButton(buttonText);
    }
    @Step
    public void checkThatNoConfirmationDialogIsShown() {
        genericPage.checkThatNoConfirmationDialogIsShown();
    }

    @Step
    public void waitForTextToDisappear(String text) {
        genericPage.waitForTextToDisappear(text);
    }

}
