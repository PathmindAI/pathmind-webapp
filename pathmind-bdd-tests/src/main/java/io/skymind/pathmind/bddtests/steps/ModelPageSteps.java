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

    @Step
    public void clickTheExperimentName(String experimentName) {
        modelPage.clickTheExperimentName(experimentName);
    }

    @Step
    public void clickExperimentArchiveButton() {
        modelPage.clickExperimentArchiveButton();
    }

    @Step
    public void clickExperimentUnArchiveButton() {
        modelPage.clickExperimentUnArchiveButton();
    }

    @Step
    public void checkThatModelsPageOpened() {
        modelPage.checkThatModelsPageOpened();
    }

    @Step
    public void clickProjectPageNewExperimentButton() {
        modelPage.clickProjectPageNewExperimentButton();
    }

    @Step
    public void checkModelPageElements() {
        modelPage.checkModelPageElements();
    }

    @Step
    public void checkExperimentModelStatusIsStarting(String status) {
        modelPage.checkExperimentModelStatusIsStarting(status);
    }

    @Step
    public void checkOnTheModelPageExperimentNotesIs(String experiment, String note) {
        modelPage.checkOnTheModelPageExperimentNotesIs(experiment, note);
    }

    @Step
    public void checkModelPageModelBreadcrumbPackageNameIs(String packageName) {
        modelPage.checkModelPageModelBreadcrumbPackageNameIs(packageName);
    }

    @Step
    public void clickModelPageExperimentStarButton(String experiment) {
        modelPage.clickModelPageExperimentStarButton(experiment);
    }

    @Step
    public void checkModelPageExperimentIsFavoriteTrue(String experiment, Boolean favoriteStatus) {
        modelPage.checkModelPageExperimentIsFavoriteTrue(experiment, favoriteStatus);
    }

    @Step
    public void clickModelPageExperimentArchiveBtn(String experiment) {
        modelPage.clickModelPageExperimentArchiveBtn(experiment);
    }
}
