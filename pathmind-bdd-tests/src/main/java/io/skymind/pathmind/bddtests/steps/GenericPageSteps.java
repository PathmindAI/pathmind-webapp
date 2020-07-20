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

    @Step
    public void openProjectsArchivedTab() {
        genericPage.switchProjectsTab();
    }

    @Step
    public void checkThatModelExistInArchivedTab(String modelName) {
        genericPage.checkThatModelExistInArchivedTab(modelName);
    }

    @Step
    public void checkThatModelNotExistInArchivedTab(String modelName) {
        genericPage.checkThatModelNotExistInArchivedTab(modelName);
    }

    @Step
    public void clickBreadcrumbBtn(String breadcrumb) {
        genericPage.clickBreadcrumbBtn(breadcrumb);
    }

    @Step
    public void addNoteToTheProjectPage(String note) {
        genericPage.addNoteToTheProjectPage(note);
        genericPage.projectPageClickSaveBtn();
    }

    @Step
    public void checkProjectNoteIs(String note) {
        genericPage.checkProjectNoteIs(note);
    }

    @Step
    public void inputProjectNameToTheEditPopup(String projectName) {
        genericPage.inputProjectNameToTheEditPopup(projectName);
    }

    @Step
    public void checkThatCheckmarkIsShown() {
        genericPage.checkThatCheckmarkIsShown();
    }

    @Step
    public void refreshPage() {
        genericPage.getDriver().navigate().refresh();
    }

    @Step
    public void duplicateCurrentTab() {
        genericPage.duplicateCurrentTab();
    }

    @Step
    public void openTab(int tab) {
        genericPage.openTab(tab);
    }

    @Step
    public void checkPageUrlIs(String url) {
        genericPage.checkPageUrlIs(url);
    }

    @Step
    public void checkPageTitleTagTextIs(String text) {
        genericPage.checkPageTitleTagTextIs(text);
    }
}
