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
}
