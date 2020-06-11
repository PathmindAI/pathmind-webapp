package io.skymind.pathmind.bddtests.stepdefinitions;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.serenitybdd.core.Serenity;
import net.thucydides.core.annotations.Steps;
import io.skymind.pathmind.bddtests.steps.GenericPageSteps;

import java.util.Date;

public class GenericPageStepDefinitions {

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

    @When("^Confirm archive/unarchive popup$")
    public void confirmArchivePopup() {
        genericPageSteps.confirmArchivePopup();
    }

    @When("^Open projects/model/experiment archived tab$")
    public void openProjectsArchivedTab() {
        genericPageSteps.openProjectsArchivedTab();
    }

    @Then("^Check that model/experiment name '(.*)' exist in archived/not archived tab$")
    public void checkThatModelExistInArchivedTab(String modelName) {
        genericPageSteps.checkThatModelExistInArchivedTab(modelName);
    }

    @Then("^Check that model/experiment name '(.*)' NOT exist in archived/not archived tab$")
    public void checkThatModelNotExistInArchivedTab(String modelName) {
        genericPageSteps.checkThatModelNotExistInArchivedTab(modelName);
    }

    @When("^Click (.*) breadcrumb btn$")
    public void clickBreadcrumbBtn(String breadcrumb) {
        genericPageSteps.clickBreadcrumbBtn(breadcrumb);
    }

    @When("^Add note (.*) to the project page$")
    public void addNoteToTheProjectPage(String note) {
        genericPageSteps.addNoteToTheProjectPage(note);
    }

    @Then("^Check project note is (.*)$")
    public void checkProjectNoteIs(String note) {
        genericPageSteps.checkProjectNoteIs(note);
    }

    @When("^Input project name (.*) to the edit popup$")
    public void inputProjectNameToTheEditPopup(String projectName) {
        Serenity.setSessionVariable("randomNumber").to(new Date().getTime());
        genericPageSteps.inputProjectNameToTheEditPopup(projectName + Serenity.sessionVariableCalled("randomNumber"));
    }

    @Then("^Check that checkmark is shown$")
    public void checkThatCheckmarkIsShown() {
        genericPageSteps.checkThatCheckmarkIsShown();
    }
}
