package io.skymind.pathmind.bddtests.steps;

import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import io.skymind.pathmind.bddtests.page.ExperimentPage;
import net.serenitybdd.core.Serenity;
import net.thucydides.core.annotations.Step;

public class ExperimentPageSteps {

    private ExperimentPage experimentPage;

    @Step
    public void checkExperimentPageRewardFunction(String rewardFnFile) throws IOException {
        experimentPage.checkExperimentPageRewardFunction(rewardFnFile);
    }

    @Step
    public void checkExperimentPageRewardFunctionNew(String rewardFnFile) throws IOException {
        experimentPage.checkExperimentPageRewardFunctionNew(rewardFnFile);
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
    public void checkVariableSimulationMetricValue(String variable, String value) {
        experimentPage.checkVariableSimulationMetricValue(variable, value);
    }

    @Step
    public void checkExperimentPageObservationsList(String observation) {
        experimentPage.checkExperimentPageObservationsList(observation);
    }

    @Step
    public void checkLearningProgressTitle(String title) {
        experimentPage.checkLearningProgressTitle(title);
    }

    @Step
    public void checkLearningProgressBlockSelectedTabNameIs(String selected, String tab) {
        experimentPage.checkLearningProgressBlockSelectedTabNameIs(selected, tab);
    }

    @Step
    public void checkLearningProgressBlockMetricsHint(String hint) {
        experimentPage.checkLearningProgressBlockMetricsHint(hint);
    }

    @Step
    public void checkLearningProgressBlockDataChartIsShown() {
        experimentPage.checkLearningProgressBlockDataChartIsShown();
    }

    @Step
    public void checkLearningProgressBlockMeanRewardScoreDataChartIsShown() {
        experimentPage.checkLearningProgressBlockMeanRewardScoreDataChartIsShown();
    }

    @Step
    public void checkVariableGoalReachedIsChosenTrue(String variable, Boolean chosen) {
        experimentPage.checkVariableGoalReachedIsChosenTrue(variable, chosen);
    }

    @Step
    public void checkExperimentNameTagLabel(String label) {
        experimentPage.checkExperimentNameTagLabel(label);
    }

    @Step
    public void saveExperimentUrlIntoTheVariable(String variable) {
        Serenity.setSessionVariable(variable).to(experimentPage.getDriver().getCurrentUrl());
    }

    @Step
    public void checkExperimentPageObservationIsSelected(String observation, String isSelected) {
        experimentPage.checkExperimentPageObservationIsSelected(observation, isSelected);
    }

    @Step
    public void checkSideBarExperimentDateIs(String experiment, String date) {
        experimentPage.checkSideBarExperimentDateIs(experiment, date);
    }

    @Step
    public void clickSideNavButtonFromNavbarItemMenuFor(String btn, String experiment) {
        experimentPage.clickSideNavButtonFromNavbarItemMenuFor(btn, experiment);
    }

    @Step
    public void checkNumberOfTheExperimentsIsInTheLeftSidebar(int experimentsNumber) {
        experimentPage.checkNumberOfTheExperimentsIsInTheLeftSidebar(experimentsNumber);
    }

    @Step
    public void checkLearningProgressBlockTabs(String tabs) {
        experimentPage.checkLearningProgressBlockTabs(tabs);
    }

    @Step
    public void checkLearningProgressBlockHistogramSimulationMetricIs(String metric, String value) {
        experimentPage.checkLearningProgressBlockHistogramSimulationMetricIs(metric, value);
    }

    @Step
    public void clickArchiveButtonForCurrentDraftExperiment() {
        experimentPage.clickArchiveButtonForCurrentDraftExperiment();
    }

    @Step
    public void clickExperimentPageShareWithSupportBtn() {
        experimentPage.clickExperimentPageShareWithSupportBtn();
    }

    @Step
    public void clickExperimentPageActionsBtn(String btn) {
        experimentPage.clickExperimentPageActionsBtn(btn);
    }

    @Step
    public void experimentPageClickComparisonFloatingCloseBtn() {
        experimentPage.experimentPageClickComparisonFloatingCloseBtn();
    }

    @Step
    public void checkLearningProgressBlockHistogramXAxisIsShown() {
        experimentPage.checkLearningProgressBlockHistogramXAxisIsShown();
    }

    @Step
    public void checkExperimentPageStartRunBtnIsActiveTrue(Boolean shown) {
        experimentPage.checkExperimentPageStartRunBtnIsActiveTrue(shown);
    }

    @Step
    public void checkDeployingPolicyServerOverlay() {
        experimentPage.checkDeployingPolicyServerOverlay();
    }

    @Step
    public void checkPolicyServerLiveWithMinutes(int timeout) {
        experimentPage.checkPolicyServerLiveWithMinutes(timeout);
    }

    @Step
    public void checkPolicyServerLiveOverlay() throws IOException, UnsupportedFlavorException {
        experimentPage.checkPolicyServerLiveOverlay();
    }

    @Step
    public void checkShutdownPolicyServerConfirmationPopup() {
        experimentPage.checkShutdownPolicyServerConfirmationPopup();
    }

    @Step
    public void clickPopUpDialogIdCancel(String id) {
        experimentPage.clickPopUpDialogIdCancel(id);
    }

    @Step
    public void checkExperimentSharedBy(String firstName, String lastName) {
        experimentPage.checkExperimentSharedBy(firstName, lastName);
    }

    @Step
    public void checkPolicyServerOverlayTokenWithAccountPage() {
        experimentPage.checkPolicyServerOverlayTokenWithAccountPage();
    }
}