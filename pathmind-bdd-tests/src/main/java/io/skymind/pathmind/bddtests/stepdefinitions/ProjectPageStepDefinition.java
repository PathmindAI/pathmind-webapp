package io.skymind.pathmind.bddtests.stepdefinitions;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.skymind.pathmind.bddtests.steps.HomePageSteps;
import io.skymind.pathmind.bddtests.steps.ProjectPageSteps;
import net.serenitybdd.core.Serenity;
import net.thucydides.core.annotations.Steps;

public class ProjectPageStepDefinition {

    @Steps
    private ProjectPageSteps projectPageSteps;
    @Steps
    private HomePageSteps homePageSteps;

    @Then("^Check that project (.*) page is opened$")
    public void checkThatProjectPageIsOpened(String projectName) {
        projectPageSteps.checkThatProjectPageOpened(projectName + Serenity.sessionVariableCalled("randomNumber"));
    }

    @And("^Check that there are (.*) model\\(s\\) with 'Draft' tag in project page$")
    public void checkThatThereAreProjectsWithDraftTagInProjectPage(int numberOfProjects) {
        projectPageSteps.checkNumberOfModelsWithDraftTag(numberOfProjects);
    }

    @When("^Click upload model btn from project page$")
    public void clickUploadModelBtnFromProjectPage() {
        projectPageSteps.clickUploadModelBtnFromProjectPage();
    }

    @Then("^Project page check that models count is (.*)$")
    public void projectPageCheckThatModelsCountIs(int modelsCount) {
        projectPageSteps.projectPageCheckThatModelsCountIs(modelsCount);
    }

    @When("^Click model (.*) archive/unarchive button$")
    public void clickModelArchiveButton(String model) {
        projectPageSteps.clickModelArchiveButton(model);
    }

    @When("^Open models tab$")
    public void openModelsTab() {
        projectPageSteps.openModelsTab();
    }

    @Then("^Check that project name is (.*) on project page$")
    public void checkThatProjectNameOnProjectPage(String name) {
        projectPageSteps.checkThatProjectNameOnProjectPage(name + Serenity.sessionVariableCalled("randomNumber"));
    }

    @Then("^Check project page model '(.*)' package name is (.*)$")
    public void checkProjectPageModelPackageNameIs(String modelId, String packageName) {
        projectPageSteps.checkProjectPageModelPackageNameIs(modelId, packageName);
    }

    @Then("^Check that project page is opened$")
    public void checkThatProjectPageIsOpened() {
        projectPageSteps.checkThatProjectPageIsOpened();
    }
}
