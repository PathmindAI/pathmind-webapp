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

    @Then("^Check experiment status completed with (.*) hours$")
    public void checkExperimentStatusCompletedWithLimitHours(int limit) {
        experimentPageSteps.checkExperimentStatusCompletedWithLimitHours(limit);
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

    @When("^Change reward variable on experiment view (.*) to (.*)$")
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
}