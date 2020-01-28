package pathmind.stepDefinitions;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.serenitybdd.core.Serenity;
import net.thucydides.core.annotations.Steps;
import pathmind.steps.HomePageSteps;
import pathmind.steps.ProjectsPageSteps;

import java.io.IOException;
import java.util.Date;

public class ProjectsPageStepDefinitions {

    @Steps
    private ProjectsPageSteps projectsPageSteps;
    @Steps
    private HomePageSteps homePageSteps;

    @When("^Click create new project button$")
    public void clickCreateNewProjectBtn() {
        projectsPageSteps.clickCreateNewProjectBtn();
    }

    @When("^Input name of the new project (.*) and click Create project button$")
    public void inputNameOfTheNewProjectProjectName(String projectName) {
        Serenity.setSessionVariable("randomNumber").to(new Date().getTime());
        projectsPageSteps.inputNameOfTheNewProject(projectName + Serenity.sessionVariableCalled("randomNumber"));
        projectsPageSteps.clickProjectNameCreateBtn();
    }

    @When("^Click pathmind helper next step button$")
    public void clickPathmindHelperNextStepButton() {
        projectsPageSteps.clickPathmindHelperNextStepButton();
    }

    @When("^Upload model (.*)$")
    public void uploadModelFile(String model) {
        projectsPageSteps.uploadModelFile(model);
//        File Check deprecated
//        projectsPageSteps.clickCheckModelBtn();
    }

    @When("^Input model details (.*), (.*), (.*)$")
    public void inputModelDetails(String observation, String action, String getObservationFile) throws IOException {
        projectsPageSteps.inputModelDetails(observation, action, getObservationFile);
    }

    @Then("^Check that project (.*) page is opened$")
    public void checkThatProjectPageIsOpened(String projectName) {
        projectsPageSteps.checkThatProjectPageOpened(projectName + Serenity.sessionVariableCalled("randomNumber"));
    }

    @Then("^Check that observation function displayed (.*)$")
    public void checkThatObservationFunctionDisplayed(String getObservationFile) throws IOException {
        projectsPageSteps.checkThatObservationFunctionDisplayed(getObservationFile);
    }

    @Then("^Input from file reward function (.*)$")
    public void inputRewardFunctionFile(String rewardFile) throws IOException {
        projectsPageSteps.inputRewardFunctionFile(rewardFile);
    }

    @Then("^Click project start discovery run button$")
    public void clickProjectStartDiscoveryRunButton() {
        projectsPageSteps.clickProjectStartDiscoveryRunButton();
    }

    @Then("^Click Okay in the \"Starting the training...\" popup$")
    public void clickOkayInThePopup() {
        projectsPageSteps.clickOkayInThePopup();
    }

    @Then("^Check experiment status completed$")
    public void checkExperimentStatusCompleted() {
        projectsPageSteps.checkExperimentStatusCompleted();
    }

    @Then("^Input project name to the search field (.*)$")
    public void inputToTheProjectsSearchField(String projectName) {
        projectsPageSteps.inputToTheProjectsSearchField(projectName + Serenity.sessionVariableCalled("randomNumber"));
    }

    @Then("^Check that project exist in project list (.*)$")
    public void checkThatProjectExistInProjectsList(String projectName){
        projectsPageSteps.checkThatProjectExistInProjectsList(projectName + Serenity.sessionVariableCalled("randomNumber"));
    }

    @Then("^Check that project search field works (.*)$")
    public void checkThatProjectsSearchFieldWorks(String projectName){
        projectsPageSteps.checkThatProjectsSearchFieldWorks(projectName + Serenity.sessionVariableCalled("randomNumber"));
    }

    @When("^Input to the projects search field value (.*)$")
    public void inputToTheProjectsSearchFieldValue(String text) {
        projectsPageSteps.inputToTheProjectsSearchField(text);
    }

    @When("^Click search field clear button$")
    public void clickSearchFieldClearButton() {
        projectsPageSteps.clickSearchFieldClearBtn();
    }

    @Then("^Check that search field is empty$")
    public void checkThatSearchFieldIsEmpty() {
        projectsPageSteps.checkThatProjectsInputFieldIsEmpty();
    }

    @Then("^Click project name (.*)$")
    public void clickProjectName(String project) {
        projectsPageSteps.clickProjectName(project);
    }

    @Then("^Click the model name (.*)$")
    public void clickTheModelName(String modelName) {
        projectsPageSteps.clickTheModelName(modelName);
    }

    @Then("^Click the experiment name (.*)$")
    public void clickTheExperimentName(String experimentName) {
        projectsPageSteps.clickTheExperimentName(experimentName);
    }

    @When("^Create new CoffeeShop project$")
    public void createNewProject() throws IOException {
        homePageSteps.openProjectsPage();
        projectsPageSteps.clickCreateNewProjectBtn();
        Serenity.setSessionVariable("randomNumber").to(new Date().getTime());
        projectsPageSteps.inputNameOfTheNewProject("AutotestProject" + Serenity.sessionVariableCalled("randomNumber"));
        projectsPageSteps.clickProjectNameCreateBtn();
        projectsPageSteps.clickUploadModelBtn();
        projectsPageSteps.uploadModelFile("\\CoffeeShopExportedModel");
        projectsPageSteps.inputModelDetails("5", "4", "CoffeeShopExportedModelGetObservation.txt");
        projectsPageSteps.checkThatProjectPageOpened("AutotestProject" + Serenity.sessionVariableCalled("randomNumber"));
        projectsPageSteps.inputRewardFunction("reward -= after[1] - before[1];");
        projectsPageSteps.clickProjectSaveDraftBtn();
    }

    @When("^Click project archive button$")
    public void clickProjectsArchiveButton() {
        projectsPageSteps.clickProjectsArchiveButton();
    }

    @When("^Confirm archive popup$")
    public void confirmArchivePopup() {
        projectsPageSteps.confirmArchivePopup();
    }

    @When("^Open projects archived tab$")
    public void openProjectsArchivedTab() {
        projectsPageSteps.openProjectsArchivedTab();
    }

    @Then("^Check that project not exist in project list (.*)$")
    public void checkThatProjectNotExistInProjectList(String project) {
        projectsPageSteps.checkThatProjectNotExistInProjectList(project);
    }

    @Then("^Check Create A New Project page$")
    public void checkCreateANewProjectPage() {
        projectsPageSteps.checkCreateANewProjectPage();
    }

    @When("^Open project (.*) on projects page$")
    public void openProjectOnProjectsPage(String projectName) {
        projectsPageSteps.openProjectOnProjectsPage(projectName + Serenity.sessionVariableCalled("randomNumber"));
    }

    @When("^Click upload model btn from project page$")
    public void clickUploadModelBtnFromProjectPage() {
        projectsPageSteps.clickUploadModelBtnFromProjectPage();
    }

    @Then("^Project page check that models count is (.*)$")
    public void projectPageCheckThatModelsCountIs(int modelsCount) {
        projectsPageSteps.projectPageCheckThatModelsCountIs(modelsCount);
    }

    @When("^Click back to projects btn$")
    public void clickBackToProjectsBtn() {
        projectsPageSteps.clickBackToProjectsBtn();
    }

    @When("^Click back to models btn$")
    public void clickBackToModelsBtn() {
        projectsPageSteps.clickBackToModelsBtn();
    }

    @Then("^Check that models page opened$")
    public void checkThatModelsPageOpened() {
        projectsPageSteps.checkThatModelsPageOpened();
    }

    @Then("^Check that model name (.*) exist in archived tab$")
    public void checkThatModelExistInArchivedTab(String modelName) {
        projectsPageSteps.checkThatModelExistInArchivedTab(modelName);
    }

    @When("^Check that model NOT exist in archived tab$")
    public void checkThatModelNOTExistInArchivedTab() {
        projectsPageSteps.checkThatModelNOTExistInArchivedTab();
    }

    @When("^Click project page new experiment button$")
    public void clickProjectPageNewExperimentButton() {
        projectsPageSteps.clickProjectPageNewExperimentButton();
    }

    @Then("^Input reward function (.*)$")
    public void inputRewardFunction(String rewardFunction) {
        projectsPageSteps.inputRewardFunction(rewardFunction);
    }

    @When("^Click project save draft btn$")
    public void clickProjectSaveDraftBtn() {
        projectsPageSteps.clickProjectSaveDraftBtn();
    }

    @Then("^Click (.*) experiment show reward function btn$")
    public void clickExperimentShowRewardFunctionBtn(String experimentName) {
        projectsPageSteps.clickExperimentShowRewardFunctionBtn(experimentName);
    }

    @Then("^Check reward function is (.*)$")
    public void checkRewardFunctionIs(String rewardFunction) {
        projectsPageSteps.checkRewardFunctionIs(rewardFunction);
    }

    @When("^New project wizard click upload model button$")
    public void newProjectWizardClickUploadModelButton() {
        projectsPageSteps.clickUploadModelBtn();
    }

    @Then("^Project wizard click download it here btn$")
    public void projectWizardClickDownloadItHereBtn() {
        projectsPageSteps.projectWizardClickDownloadItHereBtn();
    }

    @When("^Project wizard click For more details, see our documentation btn$")
    public void projectWizardForMoreDetailsSeeOurDocumentationBtn() {
        projectsPageSteps.projectWizardForMoreDetailsSeeOurDocumentationBtn();
    }

    @Then("^Check text in the project page$")
    public void checkTextInTheProjectPage() {
        projectsPageSteps.checkTextInTheProjectPage();
    }

    @When("^Input already exist name of the project to the project name$")
    public void inputAlreadyExistNameOfTheProjectToTheProjectName() {
        projectsPageSteps.inputNameOfTheNewProject("AutotestProject" + Serenity.sessionVariableCalled("randomNumber"));
        projectsPageSteps.clickProjectNameCreateBtn();
    }

    @Then("^Check that error shown (.*)$")
    public void checkThatErrorShown(String error) {
        projectsPageSteps.checkThatErrorShown(error);
    }

    @Then("^Check experiments page elements$")
    public void checkExperimentsPageElements() {
        projectsPageSteps.checkExperimentsPageElements();
    }

    @When("^Click (.*) breadcrumb btn$")
    public void clickProjectsBreadcrumbBtn(String breadcrumb) {
        projectsPageSteps.clickProjectsBreadcrumbBtn(breadcrumb);
    }

    @Then("^Check that experiments page opened$")
    public void checkThatExperimentsPageOpened() {
        projectsPageSteps.checkThatExperimentsPageOpened();
    }

    @Then("^Check experiment model status is (.*)$")
    public void checkExperimentModelStatusIsStarting(String status) {
        projectsPageSteps.checkExperimentModelStatusIsStarting(status);
    }
}
