package io.skymind.pathmind.bddtests.stepdefinitions;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
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

//    @Then("^Check that model name (.*) exist in archived tab$")
//    public void checkThatModelNameExistInArchivedTab(String experiment) {
//        modelPageSteps.checkThatModelNameExistInArchivedTab(experiment);
//    }

    @Then("^Check model page model details reward variable (.*) name is (.*)$")
    public void checkModelPageModelDetailsRewardVariableNameIs(String variableNumber, String variableName) {
        modelPageSteps.checkModelPageModelDetailsRewardVariableNameIs(variableNumber, variableName);
    }

    @Then("^Click the experiment name (.*)$")
    public void clickTheExperimentName(String experimentName) {
        modelPageSteps.clickTheExperimentName(experimentName);
    }

    @When("^Click experiment archive button$")
    public void clickExperimentArchiveButton() {
        modelPageSteps.clickExperimentArchiveButton();
    }

    @When("^Click experiment unarchive button$")
    public void clickExperimentUnArchiveButton() {
        modelPageSteps.clickExperimentUnArchiveButton();
    }

    @Then("^Check that models page opened$")
    public void checkThatModelsPageOpened() {
        modelPageSteps.checkThatModelsPageOpened();
    }

    @When("^Click project page new experiment button$")
    public void clickProjectPageNewExperimentButton() {
        modelPageSteps.clickProjectPageNewExperimentButton();
    }

    @Then("^Check model page elements$")
    public void checkModelPageElements() {
        modelPageSteps.checkModelPageElements();
    }

    @Then("^Check experiment status is (.*)$")
    public void checkExperimentModelStatusIsStarting(String status) {
        modelPageSteps.checkExperimentModelStatusIsStarting(status);
    }

    @Then("^Check on the model page experiment (.*) notes is (.*)$")
    public void checkOnTheModelPageExperimentNotesIs(String experiment, String note) {
        modelPageSteps.checkOnTheModelPageExperimentNotesIs(experiment, note);
    }
}