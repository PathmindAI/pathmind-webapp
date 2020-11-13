package io.skymind.pathmind.bddtests.stepdefinitions;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.skymind.pathmind.bddtests.steps.DashboardPageSteps;
import io.skymind.pathmind.bddtests.steps.GenericPageSteps;
import io.skymind.pathmind.bddtests.steps.HomePageSteps;
import io.skymind.pathmind.bddtests.steps.ProjectsPageSteps;
import net.serenitybdd.core.Serenity;
import net.thucydides.core.annotations.Steps;

public class DashboardPageStepDefinitions {

    @Steps
    private DashboardPageSteps dashboardPageSteps;
    @Steps
    private ProjectsPageSteps projectsPageSteps;
    @Steps
    private HomePageSteps homePageSteps;
    @Steps
    private GenericPageSteps genericPageSteps;


    @When("^Click project (.*) from dashboard$")
    public void clickProjectFromDashboard(String projectName) {
        dashboardPageSteps.clickProjectFromDashboard(projectName + Serenity.sessionVariableCalled("randomNumber"));
    }

    @When("^Click model breadcrumb (.*) from dashboard$")
    public void clickModelBreadcrumbFromDashboard(String projectName) {
        dashboardPageSteps.clickModelBreadcrumbFromDashboard(projectName + Serenity.sessionVariableCalled("randomNumber"));
    }

    @When("^Click experiment breadcrumb (.*) from dashboard$")
    public void clickExperimentBreadcrumbFromDashboard(String projectName) {
        dashboardPageSteps.clickExperimentBreadcrumbFromDashboard(projectName + Serenity.sessionVariableCalled("randomNumber"));
    }

    @Then("^Check (.*) stage (.*) is (.*)$")
    public void checkStageStatus(String projectName, String stage, String stageStatus) {
        dashboardPageSteps.checkStageStatus(projectName + Serenity.sessionVariableCalled("randomNumber"), stage, stageStatus);
    }

    @Then("^Check that (.*) experiment notes does not exist$")
    public void checkExperimentNotesNotExist(String projectName) {
        dashboardPageSteps.checkExperimentNotesNotExist(projectName + Serenity.sessionVariableCalled("randomNumber"));
    }

    @Then("^Check dashboard (.*) experiment notes is (.*)$")
    public void checkExperimentNotes(String projectName, String experimentNotes) {
        dashboardPageSteps.checkExperimentNotes(projectName + Serenity.sessionVariableCalled("randomNumber"), experimentNotes);
    }

    @When("^Click (.*) dashboard item$")
    public void clickInNavigationIcon(String projectName) {
        dashboardPageSteps.clickDashboardItem(projectName + Serenity.sessionVariableCalled("randomNumber"));
    }

    @When("^Click in (.*) stage breadcrumb$")
    public void clickInAutotestProjectStageBreadcrumb(String projectName) {
        dashboardPageSteps.clickInAutotestProjectStageBreadcrumb(projectName + Serenity.sessionVariableCalled("randomNumber"));
    }

    @When("^Click archive btn from dashboard$")
    public void clickArchiveBtnFromDashboard() {
        dashboardPageSteps.clickArchiveBtnFromDashboard("AutotestProject" + Serenity.sessionVariableCalled("randomNumber"));
    }

    @When("^Check pathmind begin screen elements$")
    public void checkDashboardBeginScreenElements() {
        dashboardPageSteps.checkDashboardBeginScreenElements();
    }

    @When("^Click pathmind create your first project btn$")
    public void clickDashboardCreateYourFirstProjectBtn() {
        dashboardPageSteps.clickDashboardCreateYourFirstProjectBtn();
    }

    @Then("^Check that dashboard page opened with the getting started message$")
    public void checkThatDashboardPageOpenedWithTheGettingStartedMessage() {
        homePageSteps.checkThatDashboardPageOpened();
        dashboardPageSteps.checkDashboardBeginScreenElements();
    }

    @When("^Click stage write reward function (.*) from dashboard$")
    public void clickStageWriteRewardFunctionFromDashboard(String projectName) {
        dashboardPageSteps.clickStageWriteRewardFunctionFromDashboard(projectName + Serenity.sessionVariableCalled("randomNumber"));
    }

    @Then("^Check dashboard (.*) model breadcrumb (.*)$")
    public void checkDashboardModelBreadcrumb(String projectName, String packageName) {
        dashboardPageSteps.checkDashboardModelBreadcrumb(projectName + Serenity.sessionVariableCalled("randomNumber"), packageName);
    }

    @Then("^Check dashboard page '(.*)' '(.*)' is favorite (.*)$")
    public void checkDashboardPageIsFavoriteTrue(String projectName, String experimentName, Boolean favoriteStatus) {
        dashboardPageSteps.checkDashboardPageProjectIsFavoriteTrue(projectName + Serenity.sessionVariableCalled("randomNumber"), experimentName, favoriteStatus);
    }

    @When("^Click dashboard page '(.*)' '(.*)' favorite button$")
    public void clickDashboardPageFavoriteButton(String projectName, String experimentName) {
        dashboardPageSteps.clickDashboardPageFavoriteButton(projectName + Serenity.sessionVariableCalled("randomNumber"), experimentName);
    }
}
