package io.skymind.pathmind.bddtests.steps;

import io.skymind.pathmind.bddtests.page.ExperimentPage;
import net.thucydides.core.annotations.Step;

import java.io.IOException;

public class ExperimentPageSteps {

    private ExperimentPage experimentPage;

    @Step
    public void checkExperimentPageRewardFunction(String rewardFnFile) throws IOException {
        experimentPage.checkExperimentPageRewardFunction(rewardFnFile);
    }

    @Step
    public void addNoteToTheExperimentPage(String note) {
        experimentPage.addNoteToTheExperimentPage(note);
    }

    @Step
    public void checkExperimentNotesIs(String note) {
        experimentPage.checkExperimentNotesIs(note);
    }

    @Step
    public void checkExperimentStatusCompletedWithLimitHours(int limit) {
        experimentPage.checkExperimentStatusCompletedWithLimitHours(limit);
    }

    @Step
    public void checkThatTheExperimentStatusIsDifferentFrom(String status) {
        experimentPage.checkThatTheExperimentStatusIsDifferentFrom(status);
    }

    @Step
    public void checkThatTheExperimentStatusIs(String status) {
        experimentPage.checkThatTheExperimentStatusIs(status);
    }

    @Step
    public void clickCurrentExperimentArchiveButton() {
        experimentPage.clickCurrentExperimentArchiveButton();
    }

    @Step
    public void changeRewardVariableOnExperimentView(String variableNumber, String variableName) {
        experimentPage.changeRewardVariableOnExperimentView(variableNumber, variableName);
    }

    @Step
    public void clickSideNavArchiveButtonFor(String experimentName) {
        experimentPage.clickSideNavArchiveButtonFor(experimentName);
    }

    @Step
    public void checkExperimentPageRewardVariablesIs(String commaSeparatedVariableNames) {
        experimentPage.checkExperimentPageRewardVariablesIs(commaSeparatedVariableNames);
    }

    @Step
    public void checkThatMetricsAreShownForRewardVariables(int metricsNumber) {
        experimentPage.checkThatMetricsAreShownForRewardVariables(metricsNumber);
    }

    @Step
    public void checkThatSparklinesAreShownForRewardVariables(int sparklinesNumber) {
        experimentPage.checkThatSparklinesAreShownForRewardVariables(sparklinesNumber);
    }

    @Step
    public void checkThatSimulationMetricsBlockIsShown() {
        experimentPage.checkThatSimulationMetricsBlockIsShown();
    }

    @Step
    public void clickCopyRewardFunctionBtn() {
        experimentPage.clickCopyRewardFunctionBtn();
    }

    @Step
    public void checkThatExperimentExistOnTheExperimentPage(String experiment) {
        experimentPage.checkThatExperimentExistOnTheExperimentPage(experiment);
    }

    @Step
    public void checkThatExperimentNotExistOnTheExperimentPage(String experiment) {
        experimentPage.checkThatExperimentNotExistOnTheExperimentPage(experiment);
    }

    @Step
    public void checkThatExperimentStatusIconIs(String experiment, String icon) {
        experimentPage.checkThatExperimentStatusIconIs(experiment, icon);
    }

    @Step
    public void clickExperimentPageStarButton(String experimentName) {
        experimentPage.clickExperimentPageStarButton(experimentName);
    }

    @Step
    public void checkExperimentPageSideBarIsFavorite(String experimentName, Boolean favoriteStatus) {
        experimentPage.checkExperimentPageSideBarIsFavorite(experimentName, favoriteStatus);
    }

    @Step
    public void checkThatExperimentPageIsOpened() {
        experimentPage.checkThatExperimentPageIsOpened();
    }
}