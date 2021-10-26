package io.skymind.pathmind.bddtests.steps;

import io.skymind.pathmind.bddtests.page.ProjectPage;
import net.thucydides.core.annotations.Step;

public class ProjectPageSteps {

    private ProjectPage projectPage;

    @Step
    public void checkThatProjectPageOpened(String projectName) {
        projectPage.checkThatProjectPageOpened(projectName);
    }

    @Step
    public void checkNumberOfModelsWithDraftTag(int numberOfProjects) {
        projectPage.checkNumberOfModelsWithDraftTag(numberOfProjects);
    }

    @Step
    public void clickUploadModelBtnFromProjectPage() {
        projectPage.clickUploadModelBtnFromProjectPage();
    }

    @Step
    public void projectPageCheckThatModelsCountIs(int modelsCount) {
        projectPage.projectPageCheckThatModelsCountIs(modelsCount);
    }

    @Step
    public void clickModelArchiveButton(String model) {
        projectPage.clickModelArchiveButton(model);
    }

    @Step
    public void openModelsTab() {
        projectPage.clickModelsTab();
    }

    @Step
    public void checkThatProjectNameOnProjectPage(String name) {
        projectPage.checkThatProjectNameDetailsOnProjectPage(name);
        projectPage.checkThatProjectNameBreadcrumbOnProjectPage(name);
    }

    @Step
    public void checkProjectPageModelPackageNameIs(String modelId, String packageName) {
        projectPage.checkProjectPageModelPackageNameIs(modelId, packageName);
    }

    @Step
    public void checkThatProjectPageIsOpened() {
        projectPage.checkThatProjectPageIsOpened();
    }

    @Step
    public void archiveModelWithPackageNameFromLeftSidebar(String modelId, String packageName) {
        projectPage.archiveModelWithPackageNameFromLeftSidebar(modelId, packageName);
    }

    @Step
    public void changeModelsSidebarListTo(String modelsList) {
        projectPage.changeModelsSidebarListTo(modelsList);
    }

    @Step
    public void checkProjectPageModelNotExistInList(String model) {
        projectPage.checkProjectPageModelNotExistInList(model);
    }

    @Step
    public void checkThatModelsSidebarModelContainsDraftTagFalse(String model, Boolean draft) {
        projectPage.checkThatModelsSidebarModelContainsDraftTagFalse(model, draft);
    }

    @Step
    public void checkProjectTitleLabelTagIsArchived(String tag) {
        projectPage.checkProjectTitleLabelTagIsArchived(tag);
    }

    @Step
    public void checkThatProjectPageTitleIs(String title) {
        projectPage.checkThatProjectPageTitleIs(title);
    }

    @Step
    public void clickProjectPageMetricDropdown(String dropdown, String value) {
        projectPage.clickProjectPageMetricDropdown(dropdown, value);
    }

    @Step
    public void checkProjectPageDropdown(String dropdown, String value) {
        projectPage.checkProjectPageDropdown(dropdown, value);
    }

    @Step
    public void clickProjectPageArchiveModelBtn() {
        projectPage.clickProjectPageArchiveModelBtn();
    }
}
