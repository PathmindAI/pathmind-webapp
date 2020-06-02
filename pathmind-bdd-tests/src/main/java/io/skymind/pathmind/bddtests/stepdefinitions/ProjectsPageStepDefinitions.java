package io.skymind.pathmind.bddtests.stepdefinitions;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.skymind.pathmind.bddtests.steps.HomePageSteps;
import io.skymind.pathmind.bddtests.steps.ProjectsPageSteps;
import net.serenitybdd.core.Serenity;
import net.thucydides.core.annotations.Steps;

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

    @When("^Click wizard next step button$")
    public void clickPathmindHelperNextStepButton() {
        projectsPageSteps.clickPathmindHelperNextStepButton();
    }

    @When("^Upload model (.*)$")
    public void uploadModelFile(String model) {
        projectsPageSteps.uploadModelFile(model);
    }

    @When("^Input model details (.*)$")
    public void inputModelDetails(String notes) {
        projectsPageSteps.inputModelDetails(notes);
    }

    @Then("^Check that project (.*) page is opened$")
    public void checkThatProjectPageIsOpened(String projectName) {
        projectsPageSteps.checkThatProjectPageOpened(projectName + Serenity.sessionVariableCalled("randomNumber"));
    }

    @Then("^Check that experiment (.*) page is opened$")
    public void checkThatExperimentPageIsOpened(String projectName) {
        projectsPageSteps.checkThatExperimentPageOpened(projectName + Serenity.sessionVariableCalled("randomNumber"));
    }

    @Then("^Input from file reward function (.*)$")
    public void inputRewardFunctionFile(String rewardFile) throws IOException {
        projectsPageSteps.inputRewardFunctionFile(rewardFile);
    }

    @Then("^Click project start run button$")
    public void clickProjectStartDiscoveryRunButton() {
        projectsPageSteps.clickProjectStartDiscoveryRunButton();
    }

    @Then("^Check that project exist in project list (.*)$")
    public void checkThatProjectExistInProjectsList(String projectName) {
        projectsPageSteps.checkThatProjectExistInProjectsList(projectName + Serenity.sessionVariableCalled("randomNumber"));
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
        projectsPageSteps.uploadModelFile("Production_Single_Agent/FAST_CoffeeShop_Database_5Observations_4Actions.zip");
        projectsPageSteps.clickWizardModelDetailsNextBtn();
        projectsPageSteps.clickWizardRewardVariableNamesNextBtn();
        projectsPageSteps.checkThatExperimentPageOpened("AutotestProject" + Serenity.sessionVariableCalled("randomNumber"));
        projectsPageSteps.inputRewardFunctionFile("Production_Single_Agent/Production_Single_Agent_Reward.txt");
        projectsPageSteps.clickProjectSaveDraftBtn();
    }

    @When("^Create new CoffeeShop project with experiment notes$")
    public void createNewProjectWithExperimentNotes() throws IOException {
        homePageSteps.openProjectsPage();
        projectsPageSteps.clickCreateNewProjectBtn();
        Serenity.setSessionVariable("randomNumber").to(new Date().getTime());
        projectsPageSteps.inputNameOfTheNewProject("AutotestProject" + Serenity.sessionVariableCalled("randomNumber"));
        projectsPageSteps.clickProjectNameCreateBtn();
        projectsPageSteps.uploadModelFile("Production_Single_Agent/FAST_CoffeeShop_Database_5Observations_4Actions.zip");
        projectsPageSteps.inputModelDetails("");
        projectsPageSteps.clickWizardRewardVariableNamesNextBtn();
        projectsPageSteps.checkThatExperimentPageOpened("AutotestProject" + Serenity.sessionVariableCalled("randomNumber"));
        projectsPageSteps.inputRewardFunctionFile("Production_Single_Agent/Production_Single_Agent_Reward.txt");
        projectsPageSteps.inputExperimentNotes("This is the experiment notes for this Coffee Shop project fast speed model.");
        projectsPageSteps.clickProjectSaveDraftBtn();
    }

    @When("^Create new CoffeeShop project with variable names: (.*)$")
    public void createNewProjectWithVariableNames(String commaSeparatedVariableNames) throws IOException {
        homePageSteps.openProjectsPage();
        projectsPageSteps.clickCreateNewProjectBtn();
        Serenity.setSessionVariable("randomNumber").to(new Date().getTime());
        projectsPageSteps.inputNameOfTheNewProject("AutotestProject" + Serenity.sessionVariableCalled("randomNumber"));
        projectsPageSteps.clickProjectNameCreateBtn();
        projectsPageSteps.uploadModelFile("Production_Single_Agent/FAST_CoffeeShop_Database_5Observations_4Actions.zip");
        projectsPageSteps.clickWizardModelDetailsNextBtn();
        projectsPageSteps.inputVariableNames(commaSeparatedVariableNames.split(","));
        projectsPageSteps.clickWizardRewardVariableNamesNextBtn();
        projectsPageSteps.checkThatExperimentPageOpened("AutotestProject" + Serenity.sessionVariableCalled("randomNumber"));
        projectsPageSteps.inputRewardFunctionFile("Production_Single_Agent/Production_Single_Agent_Reward_Using_4Variables.txt");
    }

    @When("^Click (.*) project archive/unarchive button$")
    public void clickProjectsArchiveButton(String projectName) {
        projectsPageSteps.clickProjectsArchiveButton(projectName + Serenity.sessionVariableCalled("randomNumber"));
    }

    @When("^Click experiment archive button$")
    public void clickExperimentArchiveButton() {
        projectsPageSteps.clickExperimentArchiveButton();
    }

    @When("^Click experiment unarchive button$")
    public void clickExperimentUnArchiveButton() {
        projectsPageSteps.clickExperimentUnArchiveButton();
    }

    @When("^Confirm archive/unarchive popup$")
    public void confirmArchivePopup() {
        projectsPageSteps.confirmArchivePopup();
    }

    @When("^Open projects/model/experiment archived tab$")
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

    @Then("^Check that there are (.*) project\\(s\\) with 'Draft' tag$")
    public void checkNumberOfProjectsWithDraftTag(int numberOfProjects) {
        projectsPageSteps.checkNumberOfProjectsWithDraftTag(numberOfProjects);
    }


    @And("^Check that there are (.*) project\\(s\\) with 'Draft' tag in (.*) project page$")
    public void checkThatThereAreProjectsWithDraftTagInProjectPage(int numberOfProjects, String projectName) {
        homePageSteps.openProjectsPage();
        openProjectOnProjectsPage(projectName);
        checkNumberOfProjectsWithDraftTag(numberOfProjects);
    }


    @When("^Click the first draft model$")
    public void clickTheFirstDraftModel() {
        projectsPageSteps.clickTheFirstDraftModel();
    }


    @When("^Click upload model btn from project page$")
    public void clickUploadModelBtnFromProjectPage() {
        projectsPageSteps.clickUploadModelBtnFromProjectPage();
    }

    @Then("^Project page check that models count is (.*)$")
    public void projectPageCheckThatModelsCountIs(int modelsCount) {
        projectsPageSteps.projectPageCheckThatModelsCountIs(modelsCount);
    }

    @Then("^Check that models page opened$")
    public void checkThatModelsPageOpened() {
        projectsPageSteps.checkThatModelsPageOpened();
    }

    @Then("^Check that model/experiment name (.*) exist in archived tab$")
    public void checkThatModelExistInArchivedTab(String modelName) {
        projectsPageSteps.checkThatModelExistInArchivedTab(modelName);
    }

    @When("^Check that model/experiment NOT exist in archived tab$")
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

    @Then("^Check reward function is (.*)$")
    public void checkRewardFunctionIs(String rewardFunction) {
        projectsPageSteps.checkRewardFunctionIs(rewardFunction);
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

    @Then("^Check experiment status is (.*)$")
    public void checkExperimentModelStatusIsStarting(String status) {
        projectsPageSteps.checkExperimentModelStatusIsStarting(status);
    }

    @Then("^Check that newExperiment page opened$")
    public void checkThatNewExperimentPageOpened() {
        projectsPageSteps.checkThatNewExperimentPageOpened();
    }

    @When("^Create new empty project$")
    public void createNewEmptyProject() {
        homePageSteps.openProjectsPage();
        projectsPageSteps.clickCreateNewProjectBtn();
        Serenity.setSessionVariable("randomNumber").to(new Date().getTime());
        projectsPageSteps.inputNameOfTheNewProject("AutotestProject" + Serenity.sessionVariableCalled("randomNumber"));
        projectsPageSteps.clickProjectNameCreateBtn();
    }

    @When("^Check that experiment page of the (.*) opened$")
    public void checkThatExperimentPageOfTheProjectOpened(String projectName) {
        projectsPageSteps.checkThatExperimentPageOfTheProjectOpened(projectName + Serenity.sessionVariableCalled("randomNumber"));
    }

    @When("^Click model (.*) archive/unarchive button$")
    public void clickModelArchiveButton(String model) {
        projectsPageSteps.clickModelArchiveButton(model);
    }

    @When("^Open archives tab$")
    public void openArchivesTab() {
        projectsPageSteps.openArchivesTab();
    }

    @When("^Open models tab$")
    public void openModelsTab() {
        projectsPageSteps.openModelsTab();
    }

    @When("^Open projects tab$")
    public void openProjectsTab() {
        projectsPageSteps.openProjectsTab();
    }

    @Then("^Check that model upload page opened$")
    public void checkThatModelUploadPageOpened() {
        projectsPageSteps.checkThatModelUploadPageOpened();
    }

    @When("^Click wizard model details next btn$")
    public void clickWizardModelDetailsNextBtn() {
        projectsPageSteps.clickWizardModelDetailsNextBtn();
    }

    @When("^Click wizard reward variables next btn$")
    public void clickWizardRewardVariablesNextBtn() {
        projectsPageSteps.clickWizardRewardVariableNamesNextBtn();
    }

    @When("^Click wizard reward variables save draft btn$")
    public void clickWizardRewardVariablesSaveDraftBtn() {
        projectsPageSteps.clickWizardRewardVariablesSaveDraftBtn();
    }

    @Then("^Check variable (.*) marked (.*) times in row (.*) with index (.*)$")
    public void checkVariableMarkedCorrectlyInCodeEditor(String variableName, int occurance, int row, int variableIndex) {
        projectsPageSteps.checkCodeEditorRowHasVariableMarked(row, occurance, variableName, variableIndex);
    }

    @When("^Update variable (.*) as (.*)$")
    public void updateVariableNameWithIndex(int variableIndex, String variableName) {
        projectsPageSteps.updateVariableNameWithIndex(variableIndex, variableName);
    }

    @Then("^Check that error message in model check panel is \"(.*)\"$")
    public void checkErrorMessage(String errorMessage) {
        projectsPageSteps.checkErrorMessageInModelCheckPanel(errorMessage);
    }

    @When("^Add note (.*) to the project page$")
    public void addNoteToTheProjectPage(String note) {
        projectsPageSteps.addNoteToTheProjectPage(note);
    }

    @Then("^Check project note is (.*)$")
    public void checkProjectNoteIs(String note) {
        projectsPageSteps.checkProjectNoteIs(note);
    }

    @Then("^Check on the model page experiment (.*) notes is (.*)$")
    public void checkOnTheModelPageExperimentNotesIs(String experiment, String note) {
        projectsPageSteps.checkOnTheModelPageExperimentNotesIs(experiment, note);
    }

    @Then("^Check that we can add more info to the draft model$")
    public void checkThatWeCanAddMoreInfoToTheDraftModel() {
        projectsPageSteps.checkThatWeCanAddMoreInfoToTheDraftModel();
    }

    @Then("^Check that resumeUpload page is opened$")
    public void checkThatResumeUploadPageIsOpened() {
        projectsPageSteps.checkThatResumeUploadPageIsOpened();
    }

    @Then("^Check that the Notes field has the value \"(.*)\"$")
    public void checkThatTheNotesFieldHasTheValue(String text) {
        projectsPageSteps.checkThatTheNotesFieldHasTheValue(text);
    }

    @When("^Fill Notes field as \"(.*)\"$")
    public void fillNotesFieldAs(String notes) {
        projectsPageSteps.fillNotesFieldAs(notes);
    }

    @When("^Input project name (.*) to the edit popup$")
    public void inputProjectNameToTheEditPopup(String projectName) {
        Serenity.setSessionVariable("randomNumber").to(new Date().getTime());
        projectsPageSteps.inputProjectNameToTheEditPopup(projectName + Serenity.sessionVariableCalled("randomNumber"));
    }

    @Then("^Check that project name is (.*) on project page$")
    public void checkThatProjectNameOnProjectPage(String name) {
        projectsPageSteps.checkThatProjectNameOnProjectPage(name + Serenity.sessionVariableCalled("randomNumber"));
    }

    @When("^Input reward variable names (.*)$")
    public void inputRewardVariableNames(String commaSeparatedVariableNames) {
        projectsPageSteps.inputVariableNames(commaSeparatedVariableNames.split(","));
    }

    @And("^Check that there is a variable named (.*)$")
    public void checkThatThereIsAVariableNamed(String variableName) {
        projectsPageSteps.checkThatThereIsAVariableNamed(variableName);
    }

    @When("^Click edit (.*) project icon from projects page$")
    public void clickEditProjectIconFromProjectsPage(String projectName) {
        projectsPageSteps.clickEditProjectIconFromProjectsPage(projectName + Serenity.sessionVariableCalled("randomNumber"));
    }

    @When("^Check that model successfully uploaded$")
    public void checkThatModelSuccessfullyUploaded() {
        projectsPageSteps.checkThatModelSuccessfullyUploaded();
    }

    @Then("^Check model page model details package name is (.*)$")
    public void checkModelPageModelDetailsPackageNameIs(String packageName) {
        projectsPageSteps.checkModelPageModelDetailsPackageNameIs(packageName);
    }

    @Then("^Check model page model details actions is (.*)$")
    public void checkModelPageModelDetailsActionsIs(String actions) {
        projectsPageSteps.checkModelPageModelDetailsActionsIs(actions);
    }

    @Then("^Check model page model details observations is (.*)$")
    public void checkModelPageModelDetailsObservationsIs(String observations) {
        projectsPageSteps.checkModelPageModelDetailsObservationsIs(observations);
    }

    @Then("^Check model page model details reward variables order$")
    public void checkModelPageModelDetailsRewardVariablesOrder() {
        projectsPageSteps.checkModelPageModelDetailsRewardVariablesOrder();
    }

    @Then("^Check model page model details reward variables is (.*)$")
    public void checkModelPageModelDetailsRewardVariablesIs(String commaSeparatedVariableNames) {
        projectsPageSteps.checkModelPageModelDetailsRewardVariablesIs(commaSeparatedVariableNames);
    }

    @Then("^Check that model name (.*) exist in archived tab$")
    public void checkThatModelNameExistInArchivedTab(String experiment) {
        projectsPageSteps.checkThatModelNameExistInArchivedTab(experiment);
    }

    @Then("^Check that checkmark is shown$")
    public void checkThatCheckmarkIsShown() {
        projectsPageSteps.checkThatCheckmarkIsShown();
    }

    @Then("^Check that Notes saved! msg shown$")
    public void checkThatNotesSavedMsgShown() {
        projectsPageSteps.checkThatNotesSavedMsgShown();
    }

    @When("^Check that new project page opened$")
    public void checkThatNewProjectPageOpened() {
        projectsPageSteps.checkThatNewProjectPageOpened();
    }
}
