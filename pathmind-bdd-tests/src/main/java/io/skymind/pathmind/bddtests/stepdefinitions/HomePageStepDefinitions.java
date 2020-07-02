package io.skymind.pathmind.bddtests.stepdefinitions;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.skymind.pathmind.bddtests.steps.HomePageSteps;
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

    @Then("^Check that projects button highlight is (.*)$")
    public void checkThatProjectsButtonHighlightIs(Boolean status) {
        homePageSteps.checkThatProjectsButtonHighlightIs(status);
    }
}
