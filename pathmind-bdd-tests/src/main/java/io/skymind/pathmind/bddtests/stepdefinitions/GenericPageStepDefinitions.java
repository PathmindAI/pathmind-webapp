package io.skymind.pathmind.bddtests.stepdefinitions;

import java.io.IOException;
import java.util.Date;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.skymind.pathmind.bddtests.Utils;
import io.skymind.pathmind.bddtests.steps.GenericPageSteps;
import net.serenitybdd.core.Serenity;
import net.thucydides.core.annotations.Steps;

public class GenericPageStepDefinitions {

    private Utils utils;

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

    @When("^Click text contains '(.*)' link$")
    public void clickTextContainsLink(String text) {
        genericPageSteps.clickTextContainsLink(text);
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

    @When("^Rename project name to (.*)$")
    public void renameProjectNameTo(String name) {
        genericPageSteps.inputProjectNameToTheEditPopup(name + Serenity.sessionVariableCalled("searchRandomNumber"));
    }

    @Then("^Check that checkmark is shown$")
    public void checkThatCheckmarkIsShown() {
        genericPageSteps.checkThatCheckmarkIsShown();
    }

    @When("^Refresh page$")
    public void refreshPage() {
        genericPageSteps.refreshPage();
    }

    @Then("^Duplicate current tab$")
    public void duplicateCurrentTab() {
        genericPageSteps.duplicateCurrentTab();
    }

    @When("^Open tab (\\d+)$")
    public void openTab(int tab) {
        genericPageSteps.openTab(tab);
    }

    @Then("^Check page url is (.*)$")
    public void checkPageUrlIs(String url) {
        genericPageSteps.checkPageUrlIs(url);
    }

    @Then("^Check page url contains (.*)$")
    public void checkPageUrlContains(String url) {
        genericPageSteps.checkPageUrlContains(url);
    }

    @Then("^Check page title tag text is (.*)$")
    public void checkPageTitleTagTextIs(String text) {
        genericPageSteps.checkPageTitleTagTextIs(text);
    }

    @When("^Generate unique number '(.*)'$")
    public void generateUniqueNumber(String name) {
        Serenity.setSessionVariable(name).to(new Date().getTime());
    }

    @When("^Generate big model with name (.*)$")
    public void generateBigModelWithName(String name) throws IOException {
        genericPageSteps.generateBigModelWithName(name);
    }

    @When("^Wait a bit (\\d+) ms$")
    public void waitABitMs(int time) {
        genericPageSteps.waitABitMs(time);
    }

    @When("^Check that confirmation dialog is shown (.*)$")
    public void checkThatConfirmationDialogNotShown(Boolean status) {
        genericPageSteps.checkThatConfirmationDialogNotShown(status);
    }

    @Then("^Compare '(.*)' file with downloaded file$")
    public void compareALPFileWithDownloadedFile(String alpFile) {
        genericPageSteps.compareALPFileWithDownloadedFile(alpFile);
    }

    @When("^Click pop-up dialog close btn$")
    public void clickPopUpDialogCloseBtn() {
        genericPageSteps.clickPopUpDialogCloseBtn();
    }

    @When("^Check that unexpected error alert is Not shown$")
    public void checkThatUnexpectedErrorAlertIsNotShown() {
        genericPageSteps.checkThatUnexpectedErrorAlertIsNotShown();
    }

    @When("^Click in the new tab '(.*)' button$")
    public void clickInTheNewTabModelButton(String text) {
        genericPageSteps.clickInTheNewTabModelButton(text);
    }

    @Then("^Check network errors$")
    public void checkNetworkErrors() {
        genericPageSteps.checkNetworkErrors();
    }

    @When("^Open url from the variable '(.*)'$")
    public void openUrlFromTheVariable(String url) {
        genericPageSteps.openUrlFromTheVariable(url);
    }

    @When("^Wait for loading bar disappear$")
    public void waitForLoadingBarDisappear() {
        utils.waitForLoadingBar();
    }

    @When("^Click keyboard enter btn$")
    public void clickKeyboardEnterBtn() {
        genericPageSteps.clickKeyboardEnterBtn();
    }

    @When("^Click keyboard enter btn on confirmation popup$")
    public void clickKeyboardEnterBtnOnConfirmationPopup() {
        genericPageSteps.clickKeyboardEnterBtnOnConfirmationPopup();
    }

    @When("^Click pop-up dialog 'Keep My Subscription' btn$")
    public void clickPopUpDialogKeepMySubscriptionBtn() {
        genericPageSteps.clickPopUpDialogKeepMySubscriptionBtn();
    }
}
