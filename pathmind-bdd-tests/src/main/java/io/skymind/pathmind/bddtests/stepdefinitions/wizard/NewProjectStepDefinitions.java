package io.skymind.pathmind.bddtests.stepdefinitions.wizard;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.skymind.pathmind.bddtests.steps.wizard.NewProjectSteps;
import net.serenitybdd.core.Serenity;
import net.thucydides.core.annotations.Steps;

import java.util.Date;

public class NewProjectStepDefinitions {

    @Steps
    private NewProjectSteps newProjectSteps;

    @When("^Input name of the new project (.*) and click Create project button$")
    public void inputNameOfTheNewProjectProjectName(String projectName) {
        Serenity.setSessionVariable("randomNumber").to(new Date().getTime());
        newProjectSteps.inputNameOfTheNewProject(projectName + Serenity.sessionVariableCalled("randomNumber"));
        newProjectSteps.clickProjectNameCreateBtn();
    }

    @When("^Input already exist name of the project to the project name$")
    public void inputAlreadyExistNameOfTheProjectToTheProjectName() {
        newProjectSteps.inputNameOfTheNewProject("AutotestProject" + Serenity.sessionVariableCalled("randomNumber"));
        newProjectSteps.clickProjectNameCreateBtn();
    }

    @Then("^Check Create A New Project page$")
    public void checkCreateANewProjectPage() {
        newProjectSteps.checkCreateANewProjectPage();
    }

    @Then("^Check that error shown (.*)$")
    public void checkThatErrorShown(String error) {
        newProjectSteps.checkThatErrorShown(error);
    }

    @When("^Check that new project page opened$")
    public void checkThatNewProjectPageOpened() {
        newProjectSteps.checkThatNewProjectPageOpened();
    }
}
