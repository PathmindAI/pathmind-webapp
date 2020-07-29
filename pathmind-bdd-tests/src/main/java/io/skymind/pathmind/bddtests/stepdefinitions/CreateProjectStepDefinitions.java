package io.skymind.pathmind.bddtests.stepdefinitions;

import cucumber.api.java.en.When;
import io.skymind.pathmind.bddtests.steps.GenericPageSteps;
import io.skymind.pathmind.bddtests.steps.HomePageSteps;
import io.skymind.pathmind.bddtests.steps.NewExperimentSteps;
import io.skymind.pathmind.bddtests.steps.wizard.ModelDetailsSteps;
import io.skymind.pathmind.bddtests.steps.wizard.ModelUploadSteps;
import io.skymind.pathmind.bddtests.steps.wizard.NewProjectSteps;
import io.skymind.pathmind.bddtests.steps.ProjectsPageSteps;
import io.skymind.pathmind.bddtests.steps.wizard.RewardVariablesSteps;
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
        modelUploadSteps.uploadModelFile("tuple_models/CoffeeShopTuple.zip");
        modelDetailsSteps.checkThatModelSuccessfullyUploaded();
    }

    @When("^Create new CoffeeShop project with draft experiment$")
    public void createNewProjectWithModelAndDraftExperiment() {
        createNewCoffeeShopProjectWithDraftModel();
        modelDetailsSteps.clickWizardModelDetailsNextBtn();
        rewardVariablesSteps.clickWizardRewardVariableNamesNextBtn();
        newExperimentSteps.checkThatExperimentPageOpened("AutotestProject" + Serenity.sessionVariableCalled("randomNumber"));
    }

    @When("^Create new CoffeeShop project with single reward function$")
    public void createNewProjectWithModel() {
        createNewProjectWithModelAndDraftExperiment();
        newExperimentSteps.inputRewardFunctionFile("Production_Single_Agent/Production_Single_Agent_Reward.txt");
        newExperimentSteps.clickProjectSaveDraftBtn();
    }

    @When("^Create new CoffeeShop project with 4 variables reward function$")
    public void createNewProjectWithModelAnd4VariablesReward() {
        createNewProjectWithModelAndDraftExperiment();
        newExperimentSteps.inputRewardFunctionFile("Production_Single_Agent/Production_Single_Agent_Reward_Using_4Variables.txt");
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
        newExperimentSteps.inputRewardFunctionFile("Production_Single_Agent/Production_Single_Agent_Reward_Using_4Variables.txt");
        newExperimentSteps.clickProjectSaveDraftBtn();
    }
}
