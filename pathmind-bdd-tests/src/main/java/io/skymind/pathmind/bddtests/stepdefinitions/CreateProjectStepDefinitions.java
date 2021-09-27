package io.skymind.pathmind.bddtests.stepdefinitions;

import java.util.Date;

import cucumber.api.java.en.When;
import io.skymind.pathmind.bddtests.steps.*;
import io.skymind.pathmind.bddtests.steps.wizard.ModelDetailsSteps;
import io.skymind.pathmind.bddtests.steps.wizard.ModelUploadSteps;
import io.skymind.pathmind.bddtests.steps.wizard.NewProjectSteps;
import io.skymind.pathmind.bddtests.steps.wizard.RewardVariablesSteps;
import net.serenitybdd.core.Serenity;
import net.thucydides.core.annotations.Steps;

public class CreateProjectStepDefinitions {

    @Steps
    private ProjectsPageSteps projectsPageSteps;
    @Steps
    private HomePageSteps homePageSteps;
    @Steps
    private GenericPageSteps genericPageSteps;
    @Steps
    private NewProjectSteps newProjectSteps;
    @Steps
    private ModelUploadSteps modelUploadSteps;
    @Steps
    private ModelDetailsSteps modelDetailsSteps;
    @Steps
    private NewExperimentSteps newExperimentSteps;
    @Steps
    private RewardVariablesSteps rewardVariablesSteps;
    @Steps
    private NewExperimentV2Steps newExperimentV2Steps;

    @When("^Create new empty project$")
    public void createNewEmptyProject() {
        homePageSteps.openProjectsPage();
        projectsPageSteps.clickCreateNewProjectBtn();
        Serenity.setSessionVariable("randomNumber").to(new Date().getTime());
        newProjectSteps.inputNameOfTheNewProject("AutotestProject" + Serenity.sessionVariableCalled("randomNumber"));
        newProjectSteps.clickProjectNameCreateBtn();
    }

    @When("^Create new CoffeeShop project with draft model$")
    public void createNewCoffeeShopProjectWithDraftModel() {
        createNewEmptyProject();
        modelUploadSteps.uploadModelFile("CoffeeShop/CoffeeShop.zip");
        modelDetailsSteps.checkThatModelSuccessfullyUploaded();
    }

    @When("^Create new CoffeeShop project with draft experiment$")
    public void createNewProjectWithModelAndDraftExperiment() {
        createNewCoffeeShopProjectWithDraftModel();
        modelUploadSteps.clickAlpUploadStepNextBtn();
        modelDetailsSteps.clickWizardModelDetailsNextBtn();
        rewardVariablesSteps.clickWizardRewardVariableNamesNextBtn();
        newExperimentSteps.checkThatExperimentPageOpened("AutotestProject" + Serenity.sessionVariableCalled("randomNumber"));
    }

    @When("^Create new CoffeeShop project with single reward function$")
    public void createNewProjectWithModel() {
        createNewProjectWithModelAndDraftExperiment();
        newExperimentV2Steps.enableBetaFeature();
        newExperimentV2Steps.switchToRewardTermsBeta();
        newExperimentV2Steps.addRewardTerm("kitchenCleanlinessLevel", "Maximize", "1");
        newExperimentV2Steps.addRewardTerm("successfulCustomers", "Minimize", "2");
        newExperimentV2Steps.addRewardTerm("balkedCustomers", "Maximize", "3");
        newExperimentV2Steps.addRewardTerm("avgServiceTime", "Minimize", "4");
        newExperimentSteps.clickProjectSaveDraftBtn();
    }

    @When("^Create new CoffeeShop project with 4 variables reward function$")
    public void createNewProjectWithModelAnd4VariablesReward() {
        createNewProjectWithModelAndDraftExperiment();
        newExperimentSteps.inputRewardFunctionFile("CoffeeShop/CoffeeShopRewardFunction.txt");
        newExperimentSteps.clickProjectSaveDraftBtn();
    }

    @When("^Create new CoffeeShop project with experiment note '(.*)'$")
    public void createNewProjectWitExperimentNote(String note) {
        createNewProjectWithModelAndDraftExperiment();
        projectsPageSteps.inputExperimentNotes(note);
        newExperimentSteps.clickProjectSaveDraftBtn();
    }

    @When("^Create new CoffeeShop project with variable names: (.*)$")
    public void createNewProjectWithVariableNames(String commaSeparatedVariableNames) {
        createNewCoffeeShopProjectWithDraftModel();
        modelDetailsSteps.clickWizardModelDetailsNextBtn();
        newExperimentSteps.inputVariableNames(commaSeparatedVariableNames.split(","));
        rewardVariablesSteps.clickWizardRewardVariableNamesNextBtn();
        newExperimentSteps.checkThatExperimentPageOpened("AutotestProject" + Serenity.sessionVariableCalled("randomNumber"));
        newExperimentSteps.inputRewardFunctionFile("CoffeeShop/CoffeeShopRewardFunction.txt");
        newExperimentSteps.clickProjectSaveDraftBtn();
    }
}
