package pathmind.steps;

import net.thucydides.core.annotations.Step;
import pathmind.page.ProjectsPage;
import java.io.IOException;

public class ProjectsPageSteps {

    private ProjectsPage projectsPage;
    @Step
    public void inputNameOfTheNewProject(String projectName) {
        projectsPage.inputNameOfTheNewProject(projectName);
    }
    @Step
    public void clickProjectNameCreateBtn() {
        projectsPage.clickProjectNameCreateBtn();
    }
    @Step
    public void clickCreateNewProjectBtn() {
        projectsPage.clickCreateNewProjectBtn();
    }
    @Step
    public void clickPathmindHelperNextStepButton() {
        projectsPage.clickPathmindHelperNextStepButton();
    }
    @Step
    public void uploadModelFile(String model) {
        projectsPage.uploadModelFile(model);
    }
    @Step
    public void clickCheckModelBtn() {
        projectsPage.clickCheckModelBtn();
    }
    @Step
    public void inputModelDetails(String observation, String action, String getObservationFile) throws IOException {
        projectsPage.inputModelDetailsObservation(observation);
        projectsPage.inputModelDetailsAction(action);
        projectsPage.inputModelDetailsReward(getObservationFile);
        projectsPage.clickModelDetailsNextStepButton();
    }
    @Step
    public void checkThatProjectPageOpened(String projectName) {
        projectsPage.checkThatProjectPageOpened(projectName);
    }
    @Step
    public void clickHeaderProjectsBtn() {
        projectsPage.clickHeaderProjectsBtn();
    }
    @Step
    public void checkThatProjectExistInProjectsList(String projectName) {
        projectsPage.checkThatProjectExistInProjectsList(projectName);
    }
    @Step
    public void checkThatObservationFunctionDisplayed(String getObservationFile) throws IOException {
        projectsPage.checkThatObservationFunctionDisplayed(getObservationFile);
    }
    @Step
    public void inputRewardFunctionFile(String rewardFile) throws IOException {
        projectsPage.inputRewardFunctionFile(rewardFile);
    }
    @Step
    public void clickProjectStartDiscoveryRunButton() {
        projectsPage.clickProjectStartDiscoveryRunButton();
    }
    @Step
    public void clickOkayInThePopup() {
        projectsPage.clickOkayInThePopup();
    }
    @Step
    public void checkExperimentStatusCompleted() {
        projectsPage.checkExperimentStatusCompleted();
    }
    @Step
    public void checkThatProjectsSearchFieldWorks(String projectName) {
        projectsPage.checkThatProjectsSearchFieldWorks(projectName);
    }
    @Step
    public void inputToTheProjectsSearchField(String projectName) {
        projectsPage.inputToTheProjectsSearchField(projectName);
    }
    @Step
    public void clickSearchFieldClearBtn() {
        projectsPage.clickSearchFieldClearBtn();
    }
    @Step
    public void checkThatProjectsInputFieldIsEmpty() {
        projectsPage.checkThatProjectsInputFieldIsEmpty();
    }
    @Step
    public void clickProjectName(String project) {
        projectsPage.clickProjectName(project);
    }
    @Step
    public void clickTheModelName(String modelName) {
        projectsPage.clickTheModelName(modelName);
    }
    @Step
    public void clickTheExperimentName(String experimentName) {
        projectsPage.clickTheExperimentName(experimentName);
    }
    @Step
    public void clickProjectsArchiveButton() {
        projectsPage.clickProjectsArchiveButton();
    }
    @Step
    public void confirmArchivePopup() {
        projectsPage.confirmArchivePopup();
    }
    @Step
    public void openProjectsArchivedTab() {
        projectsPage.switchProjectsTab();
    }
    @Step
    public void checkThatProjectNotExistInProjectList(String project) {
        projectsPage.checkThatProjectNotExistInProjectList(project);
    }
    @Step
    public void checkCreateANewProjectPage() {
        projectsPage.checkCreateANewProjectPage();
    }
    @Step
    public void openProjectOnProjectsPage(String projectName) {
        projectsPage.openProjectOnProjectsPage(projectName);
    }
    @Step
    public void clickUploadModelBtnFromProjectPage() {
        projectsPage.clickUploadModelBtnFromProjectPage();
    }
    @Step
    public void projectPageCheckThatModelsCountIs(int modelsCount) {
        projectsPage.projectPageCheckThatModelsCountIs(modelsCount);
    }
    @Step
    public void clickBackToProjectsBtn() {
        projectsPage.clickBackToProjectsBtn();
    }
    @Step
    public void clickBackToModelsBtn() {
        projectsPage.clickBackToModelsBtn();
    }
    @Step
    public void checkThatModelsPageOpened() {
        projectsPage.checkThatModelsPageOpened();
    }
    @Step
    public void checkThatModelExistInArchivedTab(String modelName) {
        projectsPage.checkThatModelExistInArchivedTab(modelName);
    }
    @Step
    public void checkThatModelNOTExistInArchivedTab() {
        projectsPage.checkThatModelNOTExistInArchivedTab();
    }
    @Step
    public void clickProjectPageNewExperimentButton() {
        projectsPage.clickProjectPageNewExperimentButton();
    }
    @Step
    public void inputRewardFunction(String rewardFunction) {
        projectsPage.inputRewardFunction(rewardFunction);
    }
    @Step
    public void clickProjectSaveDraftBtn() {
        projectsPage.clickProjectSaveDraftBtn();
    }
    @Step
    public void clickExperimentShowRewardFunctionBtn(String experimentName) {
        projectsPage.clickExperimentShowRewardFunctionBtn(experimentName);
    }
    @Step
    public void checkRewardFunctionIs(String rewardFunction) {
        projectsPage.checkRewardFunctionIs(rewardFunction);
    }
    @Step
    public void clickUploadModelBtn() {
        projectsPage.clickUploadModelBtn();
    }
    @Step
    public void projectWizardClickDownloadItHereBtn() {
        projectsPage.projectWizardClickDownloadItHereBtn();
    }
    @Step
    public void projectWizardForMoreDetailsSeeOurDocumentationBtn() {
        projectsPage.projectWizardForMoreDetailsSeeOurDocumentationBtn();
    }
    @Step
    public void checkTextInTheProjectPage() {
        projectsPage.checkTextInTheProjectPage();
    }
    @Step
    public void checkThatErrorShown(String error) {
        projectsPage.checkThatErrorShown(error);
    }
    @Step
    public void checkExperimentsPageElements() {
        projectsPage.checkExperimentsPageElements();
    }
    @Step
    public void clickProjectsBreadcrumbBtn(String breadcrumb) {
        projectsPage.clickProjectsBreadcrumbBtn(breadcrumb);
    }
    @Step
    public void checkThatExperimentsPageOpened() {
        projectsPage.checkThatExperimentsPageOpened();
    }
    @Step
    public void checkExperimentModelStatusIsStarting(String status) {
        projectsPage.checkExperimentModelStatusIsStarting(status);
    }
}
