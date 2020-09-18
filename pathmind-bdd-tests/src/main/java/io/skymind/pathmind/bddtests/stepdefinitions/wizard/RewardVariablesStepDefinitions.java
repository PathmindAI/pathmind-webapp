package io.skymind.pathmind.bddtests.stepdefinitions.wizard;

import cucumber.api.java.en.And;
import cucumber.api.java.en.When;
import io.skymind.pathmind.bddtests.steps.NewExperimentSteps;
import io.skymind.pathmind.bddtests.steps.wizard.RewardVariablesSteps;
import net.thucydides.core.annotations.Steps;

public class RewardVariablesStepDefinitions {

    @Steps
    private RewardVariablesSteps rewardVariablesSteps;
    @Steps
    private NewExperimentSteps newExperimentSteps;

    @When("^Click wizard reward variables next btn$")
    public void clickWizardRewardVariablesNextBtn() {
        rewardVariablesSteps.clickWizardRewardVariableNamesNextBtn();
    }

    @When("^Input reward variable names (.*)$")
    public void inputRewardVariableNames(String commaSeparatedVariableNames) {
        newExperimentSteps.inputVariableNames(commaSeparatedVariableNames.split(","));
    }

    @And("^Check that there is a variable named (.*)$")
    public void checkThatThereIsAVariableNamed(String variableName) {
        rewardVariablesSteps.checkThatThereIsAVariableNamed(variableName);
    }

    @When("^Input reward variable '(.*)' goal '(.*)' value '(.*)'$")
    public void inputRewardVariableGoalValue(String rewardVariable, String goalSign, String goal) {
        rewardVariablesSteps.inputRewardVariableGoalValue(rewardVariable, goalSign, goal);
    }
}
