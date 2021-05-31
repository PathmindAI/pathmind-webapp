package io.skymind.pathmind.bddtests.steps;

import io.skymind.pathmind.bddtests.page.ProjectsPage;
import net.thucydides.core.annotations.Step;

public class ProjectsPageSteps {

    private ProjectsPage projectsPage;

    @Step
    public void clickCreateNewProjectBtn() {
        projectsPage.clickCreateNewProjectBtn();
    }

    @Step
    public void checkThatProjectExistInProjectsList(String projectName) {
        projectsPage.checkThatProjectExistInProjectsList(projectName);
    }

    @Step
    public void inputExperimentNotes(String notes) {
        projectsPage.inputExperimentNotes(notes);
    }

    @Step
    public void clickProjectsArchiveButton(String projectName) {
        projectsPage.clickProjectsArchiveButton(projectName);
    }

    @Step
    public void checkThatProjectNotExistInProjectList(String project) {
        projectsPage.checkThatProjectNotExistInProjectList(project);
    }

    @Step
    public void openProjectOnProjectsPage(String projectName) {
        projectsPage.openProjectOnProjectsPage(projectName);
    }

    @Step
    public void openProjectsTab() {
        projectsPage.clickProjectsTab();
    }

    @Step
    public void checkPageTitleIsProjects(String title) {
        projectsPage.checkPageTitleIsProjects(title);
    }

    @Step
    public void checkNewExperimentPageRewardVariableErrorIsShown(String error) {
        projectsPage.checkNewExperimentPageRewardVariableErrorIsShown(error);
    }

    @Step
    public void checkProjectPageModelALPBtn(String filename) {
        projectsPage.checkProjectPageModelALPBtn(filename);
    }

    @Step
    public void checkProjectsPageProjectCreatedIs(String projectName, String date) {
        projectsPage.checkProjectsPageProjectCreatedIs(projectName, date);
    }

    @Step
    public void projectsPageCheckProjectModelsNumber(String projectName, String modelsNumber) {
        projectsPage.projectsPageCheckProjectModelsNumber(projectName, modelsNumber);
    }
}
