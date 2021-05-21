package io.skymind.pathmind.bddtests.steps;

import io.skymind.pathmind.bddtests.page.HomePage;
import net.thucydides.core.annotations.Step;

public class HomePageSteps {

    private HomePage homePage;

    @Step
    public void openProjectsPage() {
        homePage.openProjectsPage();
    }

    @Step
    public void logoutFromPathmind() {
        homePage.logoutFromPathmind();
    }

    @Step
    public void clickLearnBtn() {
        homePage.clickLearnBtn();
    }

    @Step
    public void checkThatLearnPageOpened(String learnPage) {
        homePage.checkThatLearnPageOpened(learnPage);
    }

    @Step
    public void openDashboardPage() {
        homePage.openDashboardPage();
    }

    @Step
    public void checkThatDashboardPageOpened() {
        homePage.checkThatDashboardPageOpened();
    }

    @Step
    public void checkThatProjectsPageOpened() {
        homePage.checkThatProjectsPageOpened();
    }

    @Step
    public void closeBrowserTab() {
        homePage.closeBrowserTab();
    }

    @Step
    public void openUserDropdown() {
        homePage.openUserDropdown();
    }

    @Step
    public void clickAccountBtn() {
        homePage.clickAccountBtn();
    }

    @Step
    public void clickBackButton() {
        homePage.getDriver().navigate().back();
    }

    @Step
    public void inputToTheNotesSearchField(String text) {
        homePage.inputToTheNotesSearchField(text);
    }

    @Step
    public void clickNotesSearchBtn() {
        homePage.clickNotesSearchBtn();
    }

    @Step
    public void checkSearchResultPageNotesContainsSearch(String text) {
        homePage.checkSearchResultPageNotesContainsSearch(text);
    }

    @Step
    public void clickNotesClearBtn() {
        homePage.clickNotesClearBtn();
    }

    @Step
    public void checkNotesSearchFieldIs(String text) {
        homePage.checkNotesSearchFieldIs(text);
    }

    @Step
    public void checkThatProjectsButtonHighlightIs(Boolean status) {
        homePage.checkThatProjectsButtonHighlightIs(status);
    }

    @Step
    public void checkSearchResultPageContainsProjectName(String name) {
        homePage.checkSearchResultPageContainsProjectName(name);
    }

    @Step
    public void checkSearchResultPageContainsModelName(String name) {
        homePage.checkSearchResultPageContainsModelName(name);
    }

    @Step
    public void clickAutotestProjectFromSearchPage(String name) {
        homePage.clickAutotestProjectFromSearchPage(name);
    }

    @Step
    public void clickToTheUniqueNoteOnTheSearchResultPage(String text) {
        homePage.clickToTheUniqueNoteOnTheSearchResultPage(text);
    }

    @Step
    public void checkSearchResultPageProjectNameContainsArchivedTag(String name) {
        homePage.checkSearchResultPageProjectNameContainsArchivedTag(name);
    }

    @Step
    public void checkSearchResultsForValueIs(String value) {
        homePage.checkSearchResultsForValueIs(value);
    }

    @Step
    public void checkThatSearchCounterIs(String counter) {
        homePage.checkThatSearchCounterIs(counter);
    }

    @Step
    public void chooseSearchOption(String option) {
        homePage.chooseSearchOption(option);
    }

    @Step
    public void checkSearchResultProjectIs(String value) {
        homePage.checkSearchResultProjectIs(value);
    }

    @Step
    public void checkSearchResultModelIs(String value) {
        homePage.checkSearchResultModelIs(value);
    }

    @Step
    public void checkSearchResultExperimentIs(String value) {
        homePage.checkSearchResultExperimentIs(value);
    }

    @Step
    public void checkSearchResultTagIs(String tag) {
        homePage.checkSearchResultTagIs(tag);
    }

    @Step
    public void waitForSearchResultPage() {
        homePage.waitForSearchResultPage();
    }

    @Step
    public void clickAndSendEnterBtnToTheSearchField() {
        homePage.clickAndSendEnterBtnToTheSearchField();
    }

    @Step
    public void clickUserMenuBtn(String btn) {
        homePage.clickUserMenuBtn(btn);
    }

    @Step
    public void clickRequestOnboardingServiceBtn() {
        homePage.clickRequestOnboardingServiceBtn();
    }

    @Step
    public void clickRequestOnboardingServiceBackBtn() {
        homePage.clickRequestOnboardingServiceBackBtn();
    }

    @Step
    public void checkRequestOnboardingServicePage() {
        homePage.checkRequestOnboardingServicePage();
    }

    @Step
    public void fillRequestOnboardingServicePaymentForm() {
        homePage.fillRequestOnboardingServicePaymentForm();
    }

    @Step
    public void clickRequestOnboardingServicePayBtn() {
        homePage.clickRequestOnboardingServicePayBtn();
    }

    @Step
    public void checkOnboardingSuccessPage() {
        homePage.checkOnboardingSuccessPage();
    }

    @Step
    public void checkSearchResultPageProjectNameContainsModelTag(String project, String modelNumber) {
        homePage.checkSearchResultPageProjectNameContainsModelTag(project, modelNumber);
    }
}
