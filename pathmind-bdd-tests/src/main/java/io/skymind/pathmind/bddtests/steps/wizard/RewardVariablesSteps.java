package io.skymind.pathmind.bddtests.steps.wizard;

import io.skymind.pathmind.bddtests.page.wizard.RewardVariablesPage;
import net.thucydides.core.annotations.Step;

public class RewardVariablesSteps {

    private RewardVariablesPage rewardVariablesPage;

    @Step
    public void clickWizardRewardVariableNamesNextBtn() {
        rewardVariablesPage.clickWizardRewardVariableNamesNextBtn();
    }

    @Step
    public void checkThatThereIsAVariableNamed(String variableName) {
        rewardVariablesPage.checkThatThereIsAVariableNamed(variableName);
    }

    @Step
    public void inputRewardVariableGoalValue(String rewardVariable, String goalSign, String goal) {
        rewardVariablesPage.inputRewardVariableGoalValue(rewardVariable, goalSign, goal);
    }

    @Step
    public void checkWizardRewardVariableErrorIsShown(String variable, String error) {
        rewardVariablesPage.checkWizardRewardVariableErrorIsShown(variable, error);
    }

    @Step
    public void checkWizardNextButtonIsDisabled() {
        rewardVariablesPage.checkWizardNextButtonIsDisabled();
    }
}
