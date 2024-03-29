package io.skymind.pathmind.bddtests.stepdefinitions;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.skymind.pathmind.bddtests.steps.HomePageSteps;
import io.skymind.pathmind.bddtests.steps.ProjectsPageSteps;
import net.serenitybdd.core.Serenity;
import net.thucydides.core.annotations.Steps;

public class ProjectsPageStepDefinitions {

    @Steps
    private ProjectsPageSteps projectsPageSteps;
    @Steps
    private HomePageSteps homePageSteps;

    @When("^Click create new project button$")
    public void clickCreateNewProjectBtn() {
        projectsPageSteps.clickCreateNewProjectBtn();
    }

    @Then("^Check that project exist in project list (.*)$")
    public void checkThatProjectExistInProjectsList(String projectName) {
        projectsPageSteps.checkThatProjectExistInProjectsList(projectName + Serenity.sessionVariableCalled("randomNumber"));
    }

    @When("^Click (.*) project archive/unarchive button$")
    public void clickProjectsArchiveButton(String projectName) {
        projectsPageSteps.clickProjectsArchiveButton(projectName + Serenity.sessionVariableCalled("randomNumber"));
    }

    @Then("^Check that project not exist in project list (.*)$")
    public void checkThatProjectNotExistInProjectList(String project) {
        projectsPageSteps.checkThatProjectNotExistInProjectList(project);
    }

    @When("^Open project (.*) on projects page$")
    public void openProjectOnProjectsPage(String projectName) {
        projectsPageSteps.openProjectOnProjectsPage(projectName + Serenity.sessionVariableCalled("randomNumber"));
    }

    @When("^Open projects tab$")
    public void openProjectsTab() {
        projectsPageSteps.openProjectsTab();
    }

    @Then("^Check page title is (.*)$")
    public void checkPageTitleIsProjects(String title) {
        projectsPageSteps.checkPageTitleIsProjects(title);
    }

    @Then("^Check project page model ALP btn (.*)$")
    public void checkProjectPageModelALPBtn(String filename) {
        projectsPageSteps.checkProjectPageModelALPBtn(filename);
    }

    @Then("^Check projects page project '(.*)' created is '(.*)'$")
    public void checkProjectsPageProjectCreatedIs(String project, String date) {
        projectsPageSteps.checkProjectsPageProjectCreatedIs(project + Serenity.sessionVariableCalled("randomNumber"), date);
    }

    @When("^Projects page check project '(.*)' models number '(.*)'$")
    public void projectsPageCheckProjectModelsNumber(String projectName, String modelsNumber) {
        projectsPageSteps.projectsPageCheckProjectModelsNumber(projectName + Serenity.sessionVariableCalled("randomNumber"), modelsNumber);
    }

    @When("^Open first project contains '(.*)' on projects page$")
    public void openFirstProjectContainsOnProjectsPage(String project) {
        projectsPageSteps.openFirstProjectContainsOnProjectsPage(project);
    }
}
