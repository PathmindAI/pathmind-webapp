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
    public void checkThatBeforeYouLeavePopUpIsShownWithError(String error) {
        newExperimentPage.checkThatBeforeYouLeavePopUpIsShownWithError(error);
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
    public void checkThatNewExperimentRewardVariableGoalAndValue(String rewardVariable, String goalSign, String goal) {
        newExperimentPage.checkThatNewExperimentRewardVariableGoalAndValue(rewardVariable, goalSign, goal);
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
    public void checkSideBarCurrentExperimentArchiveBtnTooltipIs(String tooltip) {
        newExperimentPage.checkSideBarCurrentExperimentArchiveBtnTooltipIs(tooltip);
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
}
