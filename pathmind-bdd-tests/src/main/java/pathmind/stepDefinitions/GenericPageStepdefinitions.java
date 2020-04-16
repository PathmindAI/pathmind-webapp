package pathmind.stepDefinitions;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.thucydides.core.annotations.Steps;
import pathmind.steps.GenericPageSteps;

public class GenericPageStepdefinitions {

    @Steps
    private GenericPageSteps genericPageSteps;

    @Then("^Check that button '(.*)' exists$")
    public void checkThatButtonExists(String buttonText) {
        genericPageSteps.checkThatButtonExists(buttonText);
    }

    @Then("^Check that button '(.*)' doesn't exist$")
    public void checkThatButtonDoesntExist(String buttonText) {
        genericPageSteps.checkThatButtonDoesntExist(buttonText);
    }

    @When("^Click in '(.*)' button$")
    public void clickInButton(String buttonText) {
        genericPageSteps.clickInButton(buttonText);
    }

    @Then("^Check that the '(.*)' confirmation dialog is shown$")
    public void checkThatTheConfirmationDialogIsShown(String confirmationDialogHeader) {
        genericPageSteps.checkThatTheConfirmationDialogIsShown(confirmationDialogHeader);
    }

    @Then("^Check that the notification '(.*)' is shown$")
    public void checkThatNotificationIsShown(String notificationText) {
        genericPageSteps.checkThatNotificationIsShown(notificationText);
    }

    @When("^In confirmation dialog click in '(.*)' button$")
    public void inConfirmationDialogClickInButton(String buttonText) {
        genericPageSteps.inConfirmationDialogClickInButton(buttonText);
    }

    @Then("^Check that no confirmation dialog is shown$")
    public void checkThatNoConfirmationDialogIsShown() {
        genericPageSteps.checkThatNoConfirmationDialogIsShown();
    }

    @Then("Wait for text \"(.*)\" to disappear")
    public void waitForTextToDisappear(String text) {
        genericPageSteps.waitForTextToDisappear(text);
    }
}
