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

    @Then("^Click project name (.*) from search page$")
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
    public void checkSearchResultPageProjectNameContainsArchivedTag() {
        homePageSteps.checkSearchResultPageProjectNameContainsArchivedTag("AutotestProject" + Serenity.sessionVariableCalled("randomNumber"));
    }

    @Then("^Check search result page project name contains tag '(.*)'$")
    public void checkSearchResultPageProjectNameContainsModelTag(String modelNumber) {
        homePageSteps.checkSearchResultPageProjectNameContainsModelTag("AutotestProject" + Serenity.sessionVariableCalled("randomNumber"), modelNumber);
    }

    @Then("^Check Search Results for value is '(.*)'$")
    public void checkSearchResultsForValueIs(String value) {
        homePageSteps.checkSearchResultsForValueIs(value);
    }

    @When("^Input '(.*)' to the notes search field with unique number$")
    public void inputSearchTestToTheNotesSearchField(String text) {
        homePageSteps.inputToTheNotesSearchField(text + Serenity.sessionVariableCalled("searchRandomNumber"));
    }

    @Then("^Check that search counter is '(\\d+)'$")
    public void checkThatSearchCounterIs(String counter) {
        homePageSteps.checkThatSearchCounterIs(counter);
    }

    @When("^Choose search option (.*)")
    public void chooseSearchOption(String option) {
        homePageSteps.chooseSearchOption(option);
    }

    @Then("^Check search result group project is '(.*)'$")
    public void checkSearchResultProjectIs(String value) {
        homePageSteps.checkSearchResultProjectIs(value);
    }

    @Then("^Check search result group model is '(.*)'$")
    public void checkSearchResultModelIs(String value) {
        homePageSteps.checkSearchResultModelIs(value);
    }

    @Then("^Check search result group experiment is '(.*)'$")
    public void checkSearchResultExperimentIs(String value) {
        homePageSteps.checkSearchResultExperimentIs(value);
    }

    @Then("^Check search result tag is '(.*)'$")
    public void checkSearchResultTagIs(String tag) {
        homePageSteps.checkSearchResultTagIs(tag);
    }

    @When("^Wait for search result page$")
    public void waitForSearchResultPage() {
        homePageSteps.waitForSearchResultPage();
    }

    @When("^Click and send enter btn to the search field$")
    public void clickAndSendEnterBtnToTheSearchField() {
        homePageSteps.clickAndSendEnterBtnToTheSearchField();
    }

    @When("^Click user menu '(.*)' btn$")
    public void clickUserMenuBtn(String btn) {
        homePageSteps.openUserDropdown();
        homePageSteps.clickUserMenuBtn(btn);
    }

    @When("^Click Request Onboarding Service btn$")
    public void clickRequestOnboardingServiceBtn() {
        homePageSteps.clickRequestOnboardingServiceBtn();
    }

    @When("^Click Request Onboarding Service back btn$")
    public void clickRequestOnboardingServiceBackBtn() {
        homePageSteps.clickRequestOnboardingServiceBackBtn();
    }

    @Then("^Check request onboarding service page$")
    public void checkRequestOnboardingServicePage() {
        homePageSteps.checkRequestOnboardingServicePage();
    }

    @Then("^Fill Request Onboarding Service payment form$")
    public void fillRequestOnboardingServicePaymentForm() {
        homePageSteps.fillRequestOnboardingServicePaymentForm();
    }

    @When("^Click Request Onboarding Service pay btn$")
    public void clickRequestOnboardingServicePayBtn() {
        homePageSteps.clickRequestOnboardingServicePayBtn();
    }

    @Then("^Check onboarding success page$")
    public void checkOnboardingSuccessPage() {
        homePageSteps.checkOnboardingSuccessPage();
    }

    @When("^Click search result '(.*)'$")
    public void clickSearchResultResult(String searchResult) {
        homePageSteps.clickSearchResultResult(searchResult);
    }
}
