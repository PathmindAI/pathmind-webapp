package io.skymind.pathmind.bddtests.stepdefinitions;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.skymind.pathmind.bddtests.steps.ExperimentPageSteps;
import io.skymind.pathmind.bddtests.steps.ProjectsPageSteps;
import net.thucydides.core.annotations.Steps;

import java.io.IOException;

public class ExperimentStepDefinitions {

    @Steps
    private ExperimentPageSteps experimentPageSteps;
    @Steps
    private ProjectsPageSteps projectsPageSteps;

    @Then("^Check experiment page reward function (.*)$")
    public void checkExperimentPageRewardFunction(String rewardFnFile) throws IOException {
        experimentPageSteps.checkExperimentPageRewardFunction(rewardFnFile);
    }

    @Then("^Add note ([^\"]*) to the experiment page$")
    public void addNoteToTheExperimentPage(String note) {
        experimentPageSteps.addNoteToTheExperimentPage(note);
    }

    @Then("^Check experiment notes is (.*)$")
    public void checkExperimentNotesIs(String note) {
        experimentPageSteps.checkExperimentNotesIs(note);
    }

    @Then("^Check experiment status completed with (.*) minutes$")
    public void checkExperimentStatusCompletedWithLimitMinutes(int limit) {
        experimentPageSteps.checkExperimentStatusCompletedWithLimitMinutes(limit);
    }

    @Then("^Check that the experiment status is different from '(.*)'$")
    public void checkThatTheExperimentStatusIsDifferentFrom(String status) {
        experimentPageSteps.checkThatTheExperimentStatusIsDifferentFrom(status);
    }

    @Then("^Check that the experiment status is '(.*)'$")
    public void checkThatTheExperimentStatusIs(String status) {
        experimentPageSteps.checkThatTheExperimentStatusIs(status);
    }

    @Then("^Click side nav archive button for current experiment$")
    public void clickCurrentExperimentArchiveButton() {
        experimentPageSteps.clickCurrentExperimentArchiveButton();
    }

    @When("^Change reward variable on experiment view '(.*)' to '(.*)'$")
    public void changeRewardVariableOnExperimentView(String variableNumber, String variableName) {
        experimentPageSteps.changeRewardVariableOnExperimentView(variableNumber, variableName);
    }

    @Then("^Click side nav archive button for '(.*)'$")
    public void clickSideNavArchiveButtonFor(String experimentName) {
        experimentPageSteps.clickSideNavArchiveButtonFor(experimentName);
    }

    @Then("^Check experiment page reward variables is (.*)$")
    public void checkExperimentPageRewardVariablesIs(String commaSeparatedVariableNames) {
        experimentPageSteps.checkExperimentPageRewardVariablesIs(commaSeparatedVariableNames);
    }

    @Then("^Check running experiment page reward variables is (.*)$")
    public void checkRunningExperimentPageRewardVariablesIs(String commaSeparatedVariableNames) {
        experimentPageSteps.checkRunningExperimentPageRewardVariablesIs(commaSeparatedVariableNames);
    }

    @Then("^Check that (.*) metrics are shown for reward variables$")
    public void checkThatMetricsAreShownForRewardVariables(int metricsNumber) {
        experimentPageSteps.checkThatMetricsAreShownForRewardVariables(metricsNumber);
    }

    @Then("^Check that (.*) sparklines are shown for reward variables$")
    public void checkThatSparklinesAreShownForRewardVariables(int sparklinesNumber) {
        experimentPageSteps.checkThatSparklinesAreShownForRewardVariables(sparklinesNumber);
    }

    @Then("^Check that simulation metrics block is shown$")
    public void checkThatSimulationMetricsBlockIsShown() {
        experimentPageSteps.checkThatSimulationMetricsBlockIsShown();
    }

    @Then("^Click copy reward function btn and paste text to the notes field$")
    public void clickCopyRewardFunctionBtn() {
        experimentPageSteps.clickCopyRewardFunctionBtn();
    }

    @When("^Input experiment page note (.*)$")
    public void inputExperimentPageNote(String note) {
        projectsPageSteps.inputExperimentNotes(note);
    }

    @Then("^Check that '(.*)' exist on the experiment page$")
    public void checkThatExperimentExistOnTheExperimentPage(String experiment) {
        experimentPageSteps.checkThatExperimentExistOnTheExperimentPage(experiment);
    }

    @Then("^Check that '(.*)' NOT exist on the experiment page$")
    public void checkThatExperimentNotExistOnTheExperimentPage(String experiment) {
        experimentPageSteps.checkThatExperimentNotExistOnTheExperimentPage(experiment);
    }

    @Then("^Check that '(.*)' status icon is '(.*)'$")
    public void checkThatExperimentStatusIconIs(String experiment, String icon) {
        experimentPageSteps.checkThatExperimentStatusIconIs(experiment, icon);
    }

    @Then("^Click experiment page (.*) star button$")
    public void clickExperimentPageStarButton(String experimentName) {
        experimentPageSteps.clickExperimentPageStarButton(experimentName);
    }

    @Then("^Check experiment page side bar (.*) is favorite (.*)$")
    public void checkExperimentPageSideBarIsFavorite(String experimentName, Boolean favoriteStatus) {
        experimentPageSteps.checkExperimentPageSideBarIsFavorite(experimentName, favoriteStatus);
    }

    @Then("^Check that experiment page is opened$")
    public void checkThatExperimentPageIsOpened() {
        experimentPageSteps.checkThatExperimentPageIsOpened();
    }

    @Then("^Check experiment page simulation metrics (.*)$")
    public void checkExperimentPageSimulationMetrics(String commaSeparatedVariableNames) {
        experimentPageSteps.checkExperimentPageSimulationMetrics(commaSeparatedVariableNames);
    }

    @Then("^Check side bar experiments list (.*)$")
    public void checkSideBarExperimentsListExperiment(String commaSeparatedExperimentNames) {
        experimentPageSteps.checkSideBarExperimentsListExperiment(commaSeparatedExperimentNames);
    }

    @Then("^Check that experiment page archived tag is shown$")
    public void checkThatExperimentPageArchivedTagIsShown() {
        experimentPageSteps.checkThatExperimentPageArchivedTagIsShown();
    }

    @Then("^Check Simulation Metrics columns titles$")
    public void checkSimulationMetricsColumnsTitles() {
        experimentPageSteps.checkSimulationMetricsColumnsTitles();
    }

    @Then("^Click simulation metrics value icon$")
    public void clickSimulationMetricsValueIcon() {
        experimentPageSteps.clickSimulationMetricsValueIcon();
    }

    @Then("^Click simulation metrics overview icon$")
    public void clickSimulationMetricsOverviewIcon() {
        experimentPageSteps.clickSimulationMetricsOverviewIcon();
    }

    @When("^Click experiment page show sparkline btn for variable '(.*)'$")
    public void clickExperimentPageShowSparklineBtnForVariable(String variable) {
        experimentPageSteps.clickExperimentPageShowSparklineBtnForVariable(variable);
    }

    @Then("^Check experiment page chart pop-up is shown for variable '(.*)'$")
    public void checkExperimentPageChartPopUpIsShownForVariable(String variable) {
        experimentPageSteps.checkExperimentPageChartPopUpIsShownForVariable(variable);
    }

    @Then("^Check variable '(.*)' simulation metric value '(.*)'$")
    public void checkVariableSimulationMetricValue(String variable, String value) {
        experimentPageSteps.checkVariableSimulationMetricValue(variable, value);
    }

    @Then("^Check experiment page observations list (.*)$")
    public void checkExperimentPageObservationsList(String observation) {
        experimentPageSteps.checkExperimentPageObservationsList(observation);
    }

    @Then("^Check export policy page '(.*)'$")
    public void checkExportPolicyPage(String model) {
        experimentPageSteps.checkExportPolicyPage(model);
    }

    @Then("^Check learning progress block title '(.*)'$")
    public void checkLearningProgressTitle(String title) {
        experimentPageSteps.checkLearningProgressTitle(title);
    }

    @Then("^Check learning progress block selected tab '(.*)' name is '(.*)'$")
    public void checkLearningProgressBlockSelectedTabNameIs(String selected, String tab) {
        experimentPageSteps.checkLearningProgressBlockSelectedTabNameIs(selected, tab);
    }

    @Then("^Check learning progress block metrics hint '(.*)'$")
    public void checkLearningProgressBlockMetricsHint(String hint) {
        experimentPageSteps.checkLearningProgressBlockMetricsHint(hint);
    }

    @Then("^Check learning progress block metrics data-chart is shown$")
    public void checkLearningProgressBlockDataChartIsShown() {
        experimentPageSteps.checkLearningProgressBlockDataChartIsShown();
    }

    @Then("^Check learning progress block mean reward score data-chart is shown$")
    public void checkLearningProgressBlockMeanRewardScoreDataChartIsShown() {
        experimentPageSteps.checkLearningProgressBlockMeanRewardScoreDataChartIsShown();
    }

    @Then("^Check variable '(.*)' is chosen '(.*)'$")
    public void checkVariableGoalReachedIsChosenTrue(String variable, Boolean chosen) {
        experimentPageSteps.checkVariableGoalReachedIsChosenTrue(variable, chosen);
    }
}