package io.skymind.pathmind.bddtests.stepdefinitions.wizard;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.skymind.pathmind.bddtests.steps.wizard.ModelDetailsSteps;
import net.thucydides.core.annotations.Steps;

public class ModelDetailsStepDefinitions {

    @Steps
    private ModelDetailsSteps modelDetailsSteps;

    @When("^Input wizard model details (.*)$")
    public void inputModelDetails(String notes) {
        modelDetailsSteps.inputModelDetails(notes);
    }

    @When("^Click wizard model details next btn$")
    public void clickWizardModelDetailsNextBtn() {
        modelDetailsSteps.clickWizardModelDetailsNextBtn();
    }

    @Then("^Check that model details page is opened$")
    public void checkThatModelDetailsPageIsOpened() {
        modelDetailsSteps.checkThatModelDetailsPageIsOpened();
    }

    @When("^Check that model successfully uploaded$")
    public void checkThatModelSuccessfullyUploaded() {
        modelDetailsSteps.checkThatModelSuccessfullyUploaded();
    }

    @Then("^Check experiment '(.*)' observations list contains (.*)$")
    public void checkObservationsListContains(String experiment, String commaSeparatedObservations) {
        modelDetailsSteps.checkObservationsListContains(experiment, commaSeparatedObservations.split(","));
    }

    @Then("^Click models page metrics dropdown$")
    public void clickModelsPageMetricsDropdown() {
        modelDetailsSteps.clickModelsPageMetricsDropdown();
    }

    @Then("^Check model page metrics (.*)$")
    public void checkModelPageMetricsVariables(String commaSeparatedMetrics) {
        modelDetailsSteps.checkModelPageMetricsVariables(commaSeparatedMetrics);
    }

    @When("^Model page choose metric '(.*)' from dropdown$")
    public void modelPageChooseMetricFromDropdown(String metric) {
        modelDetailsSteps.modelPageChooseMetricFromDropdown(metric);
    }

    @When("^Model page check experiment '(.*)' column '(.*)' value is '(.*)'$")
    public void modelPageCheckExperimentColumnValueIs(String experiment, String column, String value) {
        modelDetailsSteps.modelPageCheckExperimentColumnValueIs(experiment, column, value);
    }

    @Then("^Check model page columns multiselect '(.*)'$")
    public void checkModelPageColumnsMultiselect(String columns) {
        modelDetailsSteps.checkModelPageColumnsMultiselect(columns);
    }

    @When("^Model page disable '(.*)' column$")
    public void modelPageDisableFavoriteColumn(String column) {
        modelDetailsSteps.modelPageDisableFavoriteColumn(column);
    }

    @When("^Click models page columns dropdown$")
    public void clickModelsPageColumnsDropdown() {
        modelDetailsSteps.clickModelsPageColumnsDropdown();
    }
}
