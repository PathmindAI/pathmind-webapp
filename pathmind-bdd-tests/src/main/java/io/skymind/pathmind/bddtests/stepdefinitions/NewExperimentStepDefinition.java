package io.skymind.pathmind.bddtests.stepdefinitions;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.skymind.pathmind.bddtests.steps.NewExperimentSteps;
import io.skymind.pathmind.bddtests.steps.ProjectsPageSteps;
import net.serenitybdd.core.Serenity;
import net.thucydides.core.annotations.Steps;

import java.util.Date;

public class NewExperimentStepDefinition {

    @Steps
    private NewExperimentSteps newExperimentSteps;
    @Steps
    private ProjectsPageSteps projectsPageSteps;

    @Then("^Check that new experiment (.*) page is opened$")
    public void checkThatExperimentPageIsOpened(String projectName) {
        newExperimentSteps.checkThatExperimentPageOpened(projectName + Serenity.sessionVariableCalled("randomNumber"));
    }

    @Then("^Input from file reward function (.*)$")
    public void inputRewardFunctionFile(String rewardFile) {
        newExperimentSteps.inputRewardFunctionFile(rewardFile);
    }

    @Then("^Input reward function (.*)$")
    public void inputRewardFunction(String rewardFunction) {
        newExperimentSteps.inputRewardFunction(rewardFunction);
    }

    @Then("^Click project start run button$")
    public void clickProjectStartDiscoveryRunButton() {
        newExperimentSteps.clickProjectStartDiscoveryRunButton();
    }

    @When("^Click project save draft btn$")
    public void clickProjectSaveDraftBtn() {
        newExperimentSteps.clickProjectSaveDraftBtn();
    }

    @Then("^Check reward function is (.*)$")
    public void checkRewardFunctionIs(String rewardFunction) {
        newExperimentSteps.checkRewardFunctionIs(rewardFunction);
    }

    @Then("^Check variable (.*) marked (.*) times in row (.*) with index (.*)$")
    public void checkVariableMarkedCorrectlyInCodeEditor(String variableName, int occurance, int row, int variableIndex) {
        newExperimentSteps.checkCodeEditorRowHasVariableMarked(row, occurance, variableName, variableIndex);
    }

    @When("^Update variable (.*) as (.*)$")
    public void updateVariableNameWithIndex(int variableIndex, String variableName) {
        newExperimentSteps.updateVariableNameWithIndex(variableIndex, variableName);
    }

    @Then("^Check Reward Function default value <(.*)>$")
    public void checkRewardFunctionDefaultValue(String reward) {
        newExperimentSteps.checkRewardFunctionDefaultValue(reward);
    }

    @Then("^Check that Notes saved! msg shown$")
    public void checkThatNotesSavedMsgShown() {
        newExperimentSteps.checkThatNotesSavedMsgShown();
    }

    @When("^Input unique experiment page note$")
    public void inputUniqueExperimentPageNoteNote() {
        Serenity.setSessionVariable("noteRandomNumber").to(new Date().getTime());
        projectsPageSteps.inputExperimentNotes("Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo" + Serenity.sessionVariableCalled("noteRandomNumber"));
    }

    @Then("^Check newExperiment page reward variable error is shown (.*)$")
    public void checkNewExperimentPageRewardVariableErrorIsShown(String error) {
        projectsPageSteps.checkNewExperimentPageRewardVariableErrorIsShown(error);
    }

    @Then("^Check that Before you leave\\.\\.\\.\\. pop-up is shown with error (.*)$")
    public void checkThatBeforeYouLeavePopUpIsShownWithError(String error) {
        newExperimentSteps.checkThatBeforeYouLeavePopUpIsShownWithError(error);
    }
}
