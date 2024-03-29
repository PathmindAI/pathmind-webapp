package io.skymind.pathmind.bddtests.steps;

import io.skymind.pathmind.bddtests.page.NewExperimentPage;
import net.thucydides.core.annotations.Step;

public class NewExperimentSteps {

    private NewExperimentPage newExperimentPage;

    @Step
    public void checkThatExperimentPageOpened(String projectName) {
        newExperimentPage.checkThatExperimentPageOpened(projectName);
    }

    @Step
    public void inputRewardFunctionFile(String rewardFile) {
        newExperimentPage.inputRewardFunctionFile(rewardFile);
    }

    @Step
    public void inputRewardFunction(String rewardFunction) {
        newExperimentPage.inputRewardFunction(rewardFunction);
    }

    @Step
    public void clickProjectStartDiscoveryRunButton() {
        newExperimentPage.clickProjectStartDiscoveryRunButton();
    }

    @Step
    public void clickProjectSaveDraftBtn() {
        newExperimentPage.clickProjectSaveDraftBtn();
    }

    @Step
    public void checkRewardFunctionIs(String rewardFunction) {
        newExperimentPage.checkRewardFunctionIs(rewardFunction);
    }

    @Step
    public void checkCodeEditorRowHasVariableMarked(int row, int expectedSize, String variableName, int variableIndex) {
        newExperimentPage.checkCodeEditorRowHasVariableMarked(row, expectedSize, variableName, variableIndex);
    }

    @Step
    public void inputVariableNames(String[] variableNames) {
        for (int i = 0; i < variableNames.length; i++) {
            newExperimentPage.inputVariableName(variableNames[i], i);
        }
    }

    @Step
    public void updateVariableNameWithIndex(int variableIndex, String variableName) {
        newExperimentPage.inputVariableName(variableName, variableIndex);
    }

    @Step
    public void checkRewardFunctionDefaultValue(String reward) {
        newExperimentPage.checkRewardFunctionDefaultValue(reward);
    }

    @Step
    public void checkThatNotesSavedMsgShown() {
        newExperimentPage.checkThatNotesSavedMsgShown();
    }

    @Step
    public void clickSideBarExperiment(String experimentName) {
        newExperimentPage.clickSideBarExperiment(experimentName);
    }

    @Step
    public void clickObservationsCheckbox(String checkbox) {
        newExperimentPage.clickObservationsCheckbox(checkbox);
    }

    @Step
    public void checkThatNewExperimentRewardVariableGoalAndValue(String rewardVariable, String goalSign) {
        newExperimentPage.checkThatNewExperimentRewardVariableGoalAndValue(rewardVariable, goalSign);
    }

    @Step
    public void checkThatExperimentPageTitleIs(String experiment) {
        newExperimentPage.checkThatExperimentPageTitleIs(experiment);
    }

    @Step
    public void checkNewExperimentPageModelALPBtn(String filename) {
        newExperimentPage.checkNewExperimentPageModelALPBtn(filename);
    }

    public void checkSideBarStarBtnTooltipIsFavorite(String tooltip) {
        newExperimentPage.checkSideBarStarBtnTooltipIsFavorite(tooltip);
    }

    @Step
    public void checkNewExperimentPageTrainPolicyBtn(Boolean btnStatus) {
        newExperimentPage.checkNewExperimentPageTrainPolicyBtn(btnStatus);
    }

    @Step
    public void cleanNewExperimentRewardFunctionField() {
        newExperimentPage.cleanNewExperimentRewardFunctionField();
    }

    @Step
    public void openExperimentFromSidebarInTheNewTab(String experiment) {
        newExperimentPage.openExperimentFromSidebarInTheNewTab(experiment);
    }

    @Step
    public void clickSideBarNewExperimentBtn() {
        newExperimentPage.clickSideBarNewExperimentBtn();
    }

    @Step
    public void clickNewExperimentPageObservationCheckbox(String observation) {
        newExperimentPage.clickNewExperimentPageObservationCheckbox(observation);
    }

    @Step
    public void checkNewExperimentObservationsListContains(String observations) {
        newExperimentPage.checkNewExperimentObservationsListContains(observations);
    }

    @Step
    public void checkNewExperimentRewardFunctionCommentedTextNotAutocompleted(String reward, Boolean shown) {
        newExperimentPage.checkNewExperimentRewardFunctionCommentedTextNotAutocompleted(reward, shown);
    }

    @Step
    public void changeSimulationParameterIntegerToOnTheNewExperimentPage(String simParameter, String value) {
        newExperimentPage.changeSimulationParameterIntegerToOnTheNewExperimentPage(simParameter, value);
    }

    @Step
    public void checkSimulationParameterHighlightedOnTheNewExperimentPage(String simParameter, Boolean highlighted) {
        newExperimentPage.checkSimulationParameterHighlightedOnTheNewExperimentPage(simParameter, highlighted);
    }

    @Step
    public void changeSimulationParameterNumberMaxVzToOnTheNewExperimentPage(String simParameter, String value) {
        newExperimentPage.changeSimulationParameterNumberMaxVzToOnTheNewExperimentPage(simParameter, value);
    }

    @Step
    public void changeHighlightDifferenceFromDropdownToOnTheNewExperimentPage(String experiment) {
        newExperimentPage.changeHighlightDifferenceFromDropdownToOnTheNewExperimentPage(experiment);
    }

    @Step
    public void changeSimulationParameterBooleanToOnTheNewExperimentPage(String simParameter, String value) {
        newExperimentPage.changeSimulationParameterBooleanToOnTheNewExperimentPage(simParameter, value);
    }

    @Step
    public void checkObservationHighlightedOnTheNewExperimentPage(String observation, Boolean highlighted) {
        newExperimentPage.checkObservationHighlightedOnTheNewExperimentPage(observation, highlighted);
    }
}
