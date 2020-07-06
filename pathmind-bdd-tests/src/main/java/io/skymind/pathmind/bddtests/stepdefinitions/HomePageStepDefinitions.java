package io.skymind.pathmind.bddtests.stepdefinitions;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.skymind.pathmind.bddtests.steps.HomePageSteps;
import net.serenitybdd.core.Serenity;
import net.thucydides.core.annotations.Steps;

public class HomePageStepDefinitions {

    @Steps
    private HomePageSteps homePageSteps;

    @When("^Open projects page$")
    public void openProjectsPage() {
        homePageSteps.openProjectsPage();
    }

    @Then("^Logout from pathmind$")
    public void logoutFromPathmind() {
        homePageSteps.logoutFromPathmind();
    }

    @When("^Click learn btn$")
    public void clickLearnBtn() {
        homePageSteps.clickLearnBtn();
    }

    @Then("^Check that learn page (.*) opened$")
    public void checkThatLearnPageOpened(String learnPage) {
        homePageSteps.checkThatLearnPageOpened(learnPage);
    }

    @When("^Open dashboard page$")
    public void openDashboardPage() {
        homePageSteps.openDashboardPage();
    }

    @Then("^Check that dashboard page opened$")
    public void checkThatDashboardPageOpened() {
        homePageSteps.checkThatDashboardPageOpened();
    }

    @Then("^Check that projects page opened$")
    public void checkThatProjectsPageOpened() {
        homePageSteps.checkThatProjectsPageOpened();
    }

    @Then("^Close browser tab$")
    public void closeBrowserTab() {
        homePageSteps.closeBrowserTab();
    }

    @When("^Open user account page$")
    public void openUserAccountPage() {
        homePageSteps.openUserDropdown();
        homePageSteps.clickAccountBtn();
    }

    @When("^Click back button$")
    public void clickBackButton() {
        homePageSteps.clickBackButton();
    }

    @When("^Click Getting Started Guide button$")
    public void clickGettingStartedGuideButton() {
        homePageSteps.clickGettingStartedGuideButton();
    }

    @When("^Input '(.*)' to the notes search field$")
    public void inputToTheNotesSearchField(String text) {
        homePageSteps.inputToTheNotesSearchField(text);
    }

    @When("^Click notes search btn$")
    public void clickNotesSearchBtn() {
        homePageSteps.clickNotesSearchBtn();
    }

    @When("^Check search result page notes contains '(.*)'$")
    public void checkSearchResultPageNotesContainsSearch(String text) {
        homePageSteps.checkSearchResultPageNotesContainsSearch(text);
    }

    @When("^Click notes clear btn$")
    public void clickNotesClearBtn() {
        homePageSteps.clickNotesClearBtn();
    }

    @Then("^Check notes search field text is '(.*)'$")
    public void checkNotesSearchFieldIs(String text) {
        homePageSteps.checkNotesSearchFieldIs(text);
    }

    @Then("^Check that projects button highlight is (.*)$")
    public void checkThatProjectsButtonHighlightIs(Boolean status) {
        homePageSteps.checkThatProjectsButtonHighlightIs(status);
    }

    @When("^Input project name to the notes search field$")
    public void inputProjectNameToTheNotesSearchField() {
        homePageSteps.inputToTheNotesSearchField("AutotestProject" + Serenity.sessionVariableCalled("randomNumber"));
    }

    @When("^Check search result page contains project name$")
    public void checkSearchResultPageContainsProjectName() {
        homePageSteps.checkSearchResultPageContainsProjectName("AutotestProject" + Serenity.sessionVariableCalled("randomNumber"));
    }

    @When("^Check search result page contains model name '(.*)'$")
    public void checkSearchResultPageContainsModelName(String name) {
        homePageSteps.checkSearchResultPageContainsModelName(name);
    }

    @Then("^Click (.*) from search page$")
    public void clickAutotestProjectFromSearchPage(String projectName) {
        homePageSteps.clickAutotestProjectFromSearchPage(projectName + Serenity.sessionVariableCalled("randomNumber"));
    }

    @When("^Input unique note to the notes search field$")
    public void inputUniqueNoteToTheNotesSearchField() {
        homePageSteps.inputToTheNotesSearchField("Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo" + Serenity.sessionVariableCalled("noteRandomNumber"));
    }

    @When("^Check search result page notes contains unique note$")
    public void checkSearchResultPageNotesContainsUniqueNote() {
        homePageSteps.checkSearchResultPageNotesContainsSearch("Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo" + Serenity.sessionVariableCalled("noteRandomNumber"));
    }

    @When("^Click to the unique note on the search result page$")
    public void clickToTheUniqueNoteOnTheSearchResultPage() {
        homePageSteps.clickToTheUniqueNoteOnTheSearchResultPage("Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo" + Serenity.sessionVariableCalled("noteRandomNumber"));
    }

    @Then("^Check search result page project name contains archived tag$")
    public void checkSearchResultPageProjectNameContainsDraftTag() {
        homePageSteps.checkSearchResultPageProjectNameContainsDraftTag("AutotestProject" + Serenity.sessionVariableCalled("randomNumber"));
    }

    @Then("^Check Search Results for value is '(.*)'$")
    public void checkSearchResultsForValueIs(String value) {
        homePageSteps.checkSearchResultsForValueIs(value);
    }
}
