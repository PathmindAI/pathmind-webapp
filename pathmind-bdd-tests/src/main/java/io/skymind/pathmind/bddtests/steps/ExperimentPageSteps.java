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
    public void checkExperimentStatusCompletedWithLimitMinutes(int limit) {
        experimentPage.checkExperimentStatusCompletedWithLimitMinutes(limit);
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
    public void checkRunningExperimentPageRewardVariablesIs(String commaSeparatedVariableNames) {
        experimentPage.checkRunningExperimentPageRewardVariablesIs(commaSeparatedVariableNames);
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

    @Step
    public void checkExperimentPageSimulationMetrics(String commaSeparatedVariableNames) {
        experimentPage.checkExperimentPageSimulationMetrics(commaSeparatedVariableNames);
    }

    @Step
    public void checkSideBarExperimentsListExperiment(String commaSeparatedExperimentNames) {
        experimentPage.checkSideBarExperimentsListExperiment(commaSeparatedExperimentNames);
    }

    @Step
    public void checkThatExperimentPageArchivedTagIsShown() {
        experimentPage.checkThatExperimentPageArchivedTagIsShown();
    }

    @Step
    public void checkSimulationMetricsColumnsTitles() {
        experimentPage.checkSimulationMetricsColumnsTitles();
    }

    @Step
    public void clickSimulationMetricsValueIcon() {
        experimentPage.clickSimulationMetricsValueIcon();
    }

    @Step
    public void clickSimulationMetricsOverviewIcon() {
        experimentPage.clickSimulationMetricsOverviewIcon();
    }

    @Step
    public void clickExperimentPageShowSparklineBtnForVariable(String variable) {
        experimentPage.clickExperimentPageShowSparklineBtnForVariable(variable);
    }

    @Step
    public void checkExperimentPageChartPopUpIsShownForVariable(String variable) {
        experimentPage.checkExperimentPageChartPopUpIsShownForVariable(variable);
    }

    @Step
    public void checkVariableSimulationMetricValue(String variable, String value) {
        experimentPage.checkVariableSimulationMetricValue(variable, value);
    }

    @Step
    public void checkExperimentPageObservationsList(String observation) {
        experimentPage.checkExperimentPageObservationsList(observation);
    }

    @Step
    public void checkExportPolicyPage(String model) {
        experimentPage.checkExportPolicyPage(model);
    }
}