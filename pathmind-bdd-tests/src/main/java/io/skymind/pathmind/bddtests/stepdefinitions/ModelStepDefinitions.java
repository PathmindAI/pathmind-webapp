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

    @Then("^Check model page model title package name is (.*)$")
    public void checkModelPageModelTitlePackageNameIs(String packageName) {
        modelPageSteps.checkModelPageModelTitlePackageNameIs(packageName);
    }

    @Then("^Check model page model details actions is (.*)$")
    public void checkModelPageModelDetailsActionsIs(String actions) {
        modelPageSteps.checkModelPageModelDetailsActionsIs(actions);
    }

    @Then("^Check model page model details observations is (.*)$")
    public void checkModelPageModelDetailsObservationsIs(int observations) {
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

    @Then("^Check experiment '(.*)' status is '(.*)'$")
    public void checkExperimentModelStatusIsStarting(String experiment, String status) {
        modelPageSteps.checkExperimentModelStatusIsStarting(experiment, status);
    }

    @Then("^Check on the model page experiment (.*) notes is (.*)$")
    public void checkOnTheModelPageExperimentNotesIs(String experiment, String note) {
        modelPageSteps.checkOnTheModelPageExperimentNotesIs(experiment, note);
    }

    @Then("^Check model page model breadcrumb package name is (.*)$")
    public void checkModelPageModelBreadcrumbPackageNameIs(String packageName) {
        modelPageSteps.checkModelPageModelBreadcrumbPackageNameIs(packageName);
    }

    @When("^Click model page experiment '(.*)' star button$")
    public void clickModelPageExperimentStarButton(String experiment) {
        modelPageSteps.clickModelPageExperimentStarButton(experiment);
    }

    @Then("^Check model page experiment '(.*)' is favorite (.*)$")
    public void checkModelPageExperimentIsFavoriteTrue(String experiment, Boolean favoriteStatus) {
        modelPageSteps.checkModelPageExperimentIsFavoriteTrue(experiment, favoriteStatus);
    }

    @When("^Click model page experiment '(.*)' btn '(.*)'$")
    public void clickModelPageExperimentArchiveBtn(String experiment, String archive) {
        modelPageSteps.clickModelPageExperimentArchiveBtn(experiment, archive);
    }

    @When("^Click model page model archive/unarchive button$")
    public void clickModelPageModelArchiveButton() {
        modelPageSteps.clickModelPageModelArchiveButton();
    }

    @Then("^Check model page model archived tag is shown (.*)$")
    public void checkModelPageModelArchivedTagIsShown(Boolean archived) {
        modelPageSteps.checkModelPageModelArchivedTagIsShown(archived);
    }

    @Then("^Check model title label tag is (.*)$")
    public void checkModelTitleLabelTagIsArchived(String tag) {
        modelPageSteps.checkModelTitleLabelTagIsArchived(tag);
    }

    @Then("^Check side bar models '(.*)' date is '(.*)'$")
    public void checkSideBarModelsDateIs(String model, String date) {
        modelPageSteps.checkSideBarModelsDateIs(model, date);
    }

    @Then("^Check model page model '(.*)' created is '(.*)'$")
    public void checkModelPageCreatedIs(String model, String date) {
        modelPageSteps.checkModelPageCreatedIs(model, date);
    }

    @When("^Add model note (.*) to the project page$")
    public void addModelNoteToTheProjectPage(String note) {
        modelPageSteps.addModelNoteToTheProjectPage(note);
    }

    @Then("^Check model note (.*) on the project page$")
    public void checkModelNoteOnTheProjectPage(String note) {
        modelPageSteps.checkModelNoteOnTheProjectPage(note);
    }

    @Then("^Check model page experiment name '(.*)' selected observations is '(.*)'$")
    public void checkModelPageExperimentNameSelectedObservationsIs(String experiment, String observations) {
        modelPageSteps.checkModelPageExperimentNameSelectedObservationsIs(experiment, observations);
    }
}