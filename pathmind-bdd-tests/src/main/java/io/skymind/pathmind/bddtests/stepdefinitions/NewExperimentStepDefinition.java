package io.skymind.pathmind.bddtests.stepdefinitions;

import java.util.Date;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.skymind.pathmind.bddtests.Utils;
import io.skymind.pathmind.bddtests.steps.ExperimentPageSteps;
import io.skymind.pathmind.bddtests.steps.NewExperimentSteps;
import io.skymind.pathmind.bddtests.steps.ProjectsPageSteps;
import net.serenitybdd.core.Serenity;
import net.thucydides.core.annotations.Steps;

public class NewExperimentStepDefinition {

    private Utils utils;

    @Steps
    private NewExperimentSteps newExperimentSteps;
    @Steps
    private ProjectsPageSteps projectsPageSteps;
    @Steps
    private ExperimentPageSteps experimentPageSteps;

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
        experimentPageSteps.addNoteToTheExperimentPage("Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo" + Serenity.sessionVariableCalled("noteRandomNumber"));
    }

    @Then("^Check newExperiment page reward variable error is shown (.*)$")
    public void checkNewExperimentPageRewardVariableErrorIsShown(String error) {
        projectsPageSteps.checkNewExperimentPageRewardVariableErrorIsShown(error);
    }

    @When("^Click side bar experiment (.*)$")
    public void clickSideBarExperiment(String experimentName) {
        newExperimentSteps.clickSideBarExperiment(experimentName);
        utils.waitForLoadingBar();
    }

    @Then("^Check that new experiment reward variable '(.*)' goal is '(.*)'$")
    public void checkThatNewExperimentRewardVariableGoalAndValue(String rewardVariable, String goalSign) {
        newExperimentSteps.checkThatNewExperimentRewardVariableGoalAndValue(rewardVariable, goalSign);
    }

    @Then("^Check that experiment page title is '(.*)'$")
    public void checkThatExperimentPageTitleIs(String experiment) {
        newExperimentSteps.checkThatExperimentPageTitleIs(experiment);
    }

    @Then("^Check new experiment page model ALP btn (.*)$")
    public void checkNewExperimentPageModelALPBtn(String filename) {
        newExperimentSteps.checkNewExperimentPageModelALPBtn(filename);
    }

    @When("^Click new experiment observation btn '(.*)'$")
    public void clickNewExperimentObservationBtn(String checkbox) {
        newExperimentSteps.clickObservationsCheckbox(checkbox);
    }

    @Then("^Check side bar current experiment star btn tooltip is '(.*)'$")
    public void checkSideBarStarBtnTooltipIsFavorite(String tooltip) {
        newExperimentSteps.checkSideBarStarBtnTooltipIsFavorite(tooltip);
    }

    @When("^Check new experiment page train policy btn enabled '(.*)'$")
    public void checkNewExperimentPageTrainPolicyBtn(Boolean btnStatus) {
        newExperimentSteps.checkNewExperimentPageTrainPolicyBtn(btnStatus);
    }

    @When("^Clean new experiment reward function field$")
    public void cleanNewExperimentRewardFunctionField() {
        newExperimentSteps.cleanNewExperimentRewardFunctionField();
    }

    @When("^Open experiment '(.*)' from sidebar in the new tab$")
    public void openExperimentFromSidebarInTheNewTab(String experiment) {
        newExperimentSteps.openExperimentFromSidebarInTheNewTab(experiment);
    }

    @When("^Click side bar new experiment btn$")
    public void clickSideBarNewExperimentBtn() {
        newExperimentSteps.clickSideBarNewExperimentBtn();
        utils.waitForLoadingBar();
    }

    @When("^Click new experiment page observation checkbox '(.*)'$")
    public void clickNewExperimentPageObservationCheckbox(String observation) {
        newExperimentSteps.clickNewExperimentPageObservationCheckbox(observation);
    }

    @Then("^Check new experiment observations list contains '(.*)'$")
    public void checkNewExperimentObservationsListContains(String observations) {
        newExperimentSteps.checkNewExperimentObservationsListContains(observations);
    }

    @When("^Check new experiment reward function '(.*)' autocomplete is shown '(.*)'$")
    public void checkNewExperimentRewardFunctionCommentedTextNotAutocompleted(String reward, Boolean shown) {
        newExperimentSteps.checkNewExperimentRewardFunctionCommentedTextNotAutocompleted(reward, shown);
    }

    @When("^Change simulation parameter integer '(.*)' to '(.*)' on the new experiment page$")
    public void changeSimulationParameterIntegerToOnTheNewExperimentPage(String simParameter, String value) {
        newExperimentSteps.changeSimulationParameterIntegerToOnTheNewExperimentPage(simParameter, value);
    }

    @Then("^Check simulation parameter '(.*)' highlighted '(.*)' on the new experiment page$")
    public void checkSimulationParameterHighlightedOnTheNewExperimentPage(String simParameter, Boolean highlighted) {
        newExperimentSteps.checkSimulationParameterHighlightedOnTheNewExperimentPage(simParameter, highlighted);
    }

    @When("^Change simulation parameter number '(.*)' to '(.*)' on the new experiment page$")
    public void changeSimulationParameterNumberMaxVzToOnTheNewExperimentPage(String simParameter, String value) {
        newExperimentSteps.changeSimulationParameterNumberMaxVzToOnTheNewExperimentPage(simParameter, value);
    }

    @When("^Change highlight difference from dropdown to '(.*)' on the new experiment page$")
    public void changeHighlightDifferenceFromDropdownToOnTheNewExperimentPage(String experiment) {
        newExperimentSteps.changeHighlightDifferenceFromDropdownToOnTheNewExperimentPage(experiment);

    }

    @When("^Change simulation parameter boolean '(.*)' to '(.*)' on the new experiment page$")
    public void changeSimulationParameterBooleanToOnTheNewExperimentPage(String simParameter, String value) {
        newExperimentSteps.changeSimulationParameterBooleanToOnTheNewExperimentPage(simParameter, value);
    }

    @Then("^Check observation '(.*)' highlighted '(.*)' on the new experiment page$")
    public void checkObservationHighlightedOnTheNewExperimentPage(String observation, Boolean highlighted) {
        newExperimentSteps.checkObservationHighlightedOnTheNewExperimentPage(observation, highlighted);
    }
}
