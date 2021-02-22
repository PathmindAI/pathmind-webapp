package io.skymind.pathmind.bddtests.steps.wizard;

import io.skymind.pathmind.bddtests.page.wizard.ModelDetailsPage;
import net.thucydides.core.annotations.Step;

public class ModelDetailsSteps {

    private ModelDetailsPage modelDetailsPage;

    @Step
    public void inputModelDetails(String notes) {
        modelDetailsPage.inputModelDetailsNotes(notes);
    }

    @Step
    public void clickWizardModelDetailsNextBtn() {
        modelDetailsPage.clickWizardModelDetailsNextBtn();
    }

    @Step
    public void checkThatModelDetailsPageIsOpened() {
        modelDetailsPage.checkThatModelDetailsPageIsOpened();
    }

    @Step
    public void checkThatModelSuccessfullyUploaded() {
        modelDetailsPage.checkThatModelSuccessfullyUploaded();
    }

    @Step
    public void checkObservationsListContains(String experiment, String[] observations) {
        for (int i = 0; i < observations.length; i++) {
            modelDetailsPage.checkObservationsListContains(experiment, observations[i]);
        }
    }

    @Step
    public void clickModelsPageMetricsDropdown() {
        modelDetailsPage.clickModelsPageMetricsDropdown();
    }

    @Step
    public void checkModelPageMetricsVariables(String metrics) {
        modelDetailsPage.checkMetrics(metrics);
    }

    @Step
    public void modelPageChooseMetricFromDropdown(String metric) {
        modelDetailsPage.modelPageChooseMetricFromDropdown(metric);
    }

    @Step
    public void modelPageCheckExperimentColumnValueIs(String experiment, String column, String value) {
        modelDetailsPage.modelPageCheckExperimentColumnValueIs(experiment, column, value);
    }

    @Step
    public void checkModelPageColumnsMultiselect(String columns) {
        modelDetailsPage.checkModelPageColumnsMultiselect(columns);
    }

    @Step
    public void modelPageDisableFavoriteColumn(String column) {
        modelDetailsPage.modelPageDisableFavoriteColumn(column);
    }

    @Step
    public void clickModelsPageColumnsDropdown() {
        modelDetailsPage.clickModelsPageColumnsDropdown();
    }
}
