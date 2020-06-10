package io.skymind.pathmind.bddtests.stepdefinitions;

import cucumber.api.java.en.When;
import io.skymind.pathmind.bddtests.steps.GenericPageSteps;
import io.skymind.pathmind.bddtests.steps.HomePageSteps;
import io.skymind.pathmind.bddtests.steps.ProjectsPageSteps;
import net.serenitybdd.core.Serenity;
import net.thucydides.core.annotations.Steps;

import java.util.Date;

public class CreateProjectStepDefinitions {

    @Steps
    private ProjectsPageSteps projectsPageSteps;
    @Steps
    private HomePageSteps homePageSteps;
    @Steps
    private GenericPageSteps genericPageSteps;

    @When("^Create new empty project$")
    public void createNewEmptyProject() {
        homePageSteps.openProjectsPage();
        projectsPageSteps.clickCreateNewProjectBtn();
        Serenity.setSessionVariable("randomNumber").to(new Date().getTime());
        projectsPageSteps.inputNameOfTheNewProject("AutotestProject" + Serenity.sessionVariableCalled("randomNumber"));
        projectsPageSteps.clickProjectNameCreateBtn();
    }

    @When("^Create new CoffeeShop project with draft model$")
    public void createNewCoffeeShopProjectWithDraftModel() {
        createNewEmptyProject();
        projectsPageSteps.uploadModelFile("Production_Single_Agent/FAST_CoffeeShop_Database_5Observations_4Actions.zip");
        projectsPageSteps.checkThatModelSuccessfullyUploaded();
    }

    @When("^Create new CoffeeShop project with draft experiment$")
    public void createNewProjectWithModelAndDraftExperiment() {
        createNewCoffeeShopProjectWithDraftModel();
        projectsPageSteps.clickWizardModelDetailsNextBtn();
        projectsPageSteps.clickWizardRewardVariableNamesNextBtn();
        projectsPageSteps.checkThatExperimentPageOpened("AutotestProject" + Serenity.sessionVariableCalled("randomNumber"));
    }

    @When("^Create new CoffeeShop project with single reward function$")
    public void createNewProjectWithModel() {
        createNewProjectWithModelAndDraftExperiment();
        projectsPageSteps.inputRewardFunctionFile("Production_Single_Agent/Production_Single_Agent_Reward.txt");
        projectsPageSteps.clickProjectSaveDraftBtn();
    }

    @When("^Create new CoffeeShop project with experiment note '(.*)'$")
    public void createNewProjectWitExperimentNote(String note) {
        createNewProjectWithModelAndDraftExperiment();
        projectsPageSteps.inputExperimentNotes(note);
        projectsPageSteps.clickProjectSaveDraftBtn();
    }

    @When("^Create new CoffeeShop project with variable names: (.*)$")
    public void createNewProjectWithVariableNames(String commaSeparatedVariableNames) {
        createNewCoffeeShopProjectWithDraftModel();
        projectsPageSteps.clickWizardModelDetailsNextBtn();
        projectsPageSteps.inputVariableNames(commaSeparatedVariableNames.split(","));
        projectsPageSteps.clickWizardRewardVariableNamesNextBtn();
        projectsPageSteps.checkThatExperimentPageOpened("AutotestProject" + Serenity.sessionVariableCalled("randomNumber"));
        projectsPageSteps.inputRewardFunctionFile("Production_Single_Agent/Production_Single_Agent_Reward_Using_4Variables.txt");
        projectsPageSteps.clickProjectSaveDraftBtn();
    }
}
