package io.skymind.pathmind.bddtests.steps;

import io.skymind.pathmind.bddtests.page.ModelPage;
import net.thucydides.core.annotations.Step;

public class ModelPageSteps {

    private ModelPage modelPage;

    @Step
    public void clickTheModelName(String modelName) {
        modelPage.clickTheModelName(modelName);
    }

    @Step
    public void checkModelPageModelDetailsPackageNameIs(String packageName) {
        modelPage.checkModelPageModelDetailsPackageNameIs(packageName);
    }

    @Step
    public void checkModelPageModelDetailsActionsIs(String actions) {
        modelPage.checkModelPageModelDetailsActionsIs(actions);
    }

    @Step
    public void checkModelPageModelDetailsObservationsIs(String observations) {
        modelPage.checkModelPageModelDetailsObservationsIs(observations);
    }

    @Step
    public void checkModelPageModelDetailsRewardVariablesOrder() {
        modelPage.checkModelPageModelDetailsRewardVariablesOrder();
    }

    @Step
    public void checkModelPageModelDetailsRewardVariablesIs(String commaSeparatedVariableNames) {
        modelPage.checkModelPageModelDetailsRewardVariablesIs(commaSeparatedVariableNames);
    }

    @Step
    public void checkThatModelNameExistInArchivedTab(String experiment) {
        modelPage.checkThatModelNameExistInArchivedTab(experiment);
    }

    @Step
    public void checkModelPageModelDetailsRewardVariableNameIs(String variableNumber, String variableName) {
        modelPage.checkModelPageModelDetailsRewardVariableNameIs(variableNumber, variableName);
    }
}