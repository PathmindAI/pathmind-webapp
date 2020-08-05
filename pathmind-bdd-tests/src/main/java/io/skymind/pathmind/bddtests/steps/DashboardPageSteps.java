package io.skymind.pathmind.bddtests.steps;

import net.thucydides.core.annotations.Step;
import io.skymind.pathmind.bddtests.page.DashboardPage;

public class DashboardPageSteps {

    private DashboardPage dashboardPage;

    @Step
    public void clickProjectFromDashboard(String randomNumber) {
        dashboardPage.clickProjectFromDashboard(randomNumber);
    }

    @Step
    public void clickModelBreadcrumbFromDashboard(String projectName) {
        dashboardPage.clickModelBreadcrumbFromDashboard(projectName);
    }

    @Step
    public void clickExperimentBreadcrumbFromDashboard(String projectName) {
        dashboardPage.clickExperimentBreadcrumbFromDashboard(projectName);
    }

    @Step
    public void checkStageStatus(String projectName, String stage, String stageStatus) {
        dashboardPage.checkStageStatus(projectName, stage, stageStatus);
    }

    @Step
    public void checkExperimentNotesNotExist(String projectName) {
        dashboardPage.checkExperimentNotesNotExist(projectName);
    }

    @Step
    public void checkExperimentNotes(String projectName, String experimentNotes) {
        dashboardPage.checkExperimentNotes(projectName, experimentNotes);
    }

    @Step
    public void clickInNavigationIcon(String projectName) {
        dashboardPage.clickInNavigationIcon(projectName);
    }

    @Step
    public void clickInAutotestProjectStageBreadcrumb(String projectName) {
        dashboardPage.clickInAutotestProjectStageBreadcrumb(projectName);
    }

    @Step
    public void clickArchiveBtnFromDashboard(String projectName) {
        dashboardPage.clickDashItemIcons(projectName);
        dashboardPage.clickArchiveBtn();
    }

    @Step
    public void checkDashboardBeginScreenElements() {
        dashboardPage.checkDashboardBeginScreenElements();
    }

    @Step
    public void clickDashboardCreateYourFirstProjectBtn() {
        dashboardPage.clickDashboardCreateYourFirstProjectBtn();
    }

    @Step
    public void clickStageWriteRewardFunctionFromDashboard(String projectName) {
        dashboardPage.clickStageWriteRewardFunctionFromDashboard(projectName);
    }

    @Step
    public void checkDashboardModelBreadcrumb(String projectName, String packageName) {
        dashboardPage.checkDashboardModelBreadcrumb(projectName, packageName);
    }

    @Step
    public void checkDashboardPageProjectIsFavoriteTrue(String projectName, String experimentName, Boolean favoriteStatus) {
        dashboardPage.checkDashboardPageProjectIsFavoriteTrue(projectName, experimentName, favoriteStatus);
    }

    @Step
    public void clickDashboardPageFavoriteButton(String projectName, String experimentName) {
        dashboardPage.clickDashboardPageFavoriteButton(projectName, experimentName);
    }
}
