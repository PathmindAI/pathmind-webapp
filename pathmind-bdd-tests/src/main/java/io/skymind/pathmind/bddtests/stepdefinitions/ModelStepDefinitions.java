package io.skymind.pathmind.bddtests.stepdefinitions;

import cucumber.api.java.en.Then;
import io.skymind.pathmind.bddtests.steps.ModelPageSteps;
import net.thucydides.core.annotations.Steps;

public class ModelStepDefinitions {

    @Steps
    private ModelPageSteps modelPageSteps;

    @Then("^Click the model name (.*)$")
    public void clickTheModelName(String modelName) {
        modelPageSteps.clickTheModelName(modelName);
    }

    @Then("^Check model page model details package name is (.*)$")
    public void checkModelPageModelDetailsPackageNameIs(String packageName) {
        modelPageSteps.checkModelPageModelDetailsPackageNameIs(packageName);
    }

    @Then("^Check model page model details actions is (.*)$")
    public void checkModelPageModelDetailsActionsIs(String actions) {
        modelPageSteps.checkModelPageModelDetailsActionsIs(actions);
    }

    @Then("^Check model page model details observations is (.*)$")
    public void checkModelPageModelDetailsObservationsIs(String observations) {
        modelPageSteps.checkModelPageModelDetailsObservationsIs(observations);
    }

    @Then("^Check model page model details reward variables order$")
    public void checkModelPageModelDetailsRewardVariablesOrder() {
        modelPageSteps.checkModelPageModelDetailsRewardVariablesOrder();
    }

    @Then("^Check model page model details reward variables is (.*)$")
    public void checkModelPageModelDetailsRewardVariablesIs(String commaSeparatedVariableNames) {
        modelPageSteps.checkModelPageModelDetailsRewardVariablesIs(commaSeparatedVariableNames);
    }

    @Then("^Check that model name (.*) exist in archived tab$")
    public void checkThatModelNameExistInArchivedTab(String experiment) {
        modelPageSteps.checkThatModelNameExistInArchivedTab(experiment);
    }

    @Then("^Check model page model details reward variable (.*) name is (.*)$")
    public void checkModelPageModelDetailsRewardVariableNameIs(String variableNumber, String variableName) {
        modelPageSteps.checkModelPageModelDetailsRewardVariableNameIs(variableNumber, variableName);
    }
}