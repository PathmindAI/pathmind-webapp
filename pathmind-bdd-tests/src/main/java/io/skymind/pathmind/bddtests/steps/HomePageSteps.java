package io.skymind.pathmind.bddtests.steps;

import net.thucydides.core.annotations.Step;
import io.skymind.pathmind.bddtests.page.HomePage;
import io.skymind.pathmind.bddtests.page.LoginPage;

public class HomePageSteps {

    private LoginPage loginPage;
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
    public void clickGettingStartedGuideButton() {
        homePage.clickGettingStartedGuideButton();
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
    public void checkSearchResultPageProjectNameContainsDraftTag(String name) {
        homePage.checkSearchResultPageProjectNameContainsDraftTag(name);
    }

    @Step
    public void checkSearchResultsForValueIs(String value) {
        homePage.checkSearchResultsForValueIs(value);
    }
}
