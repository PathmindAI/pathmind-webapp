package io.skymind.pathmind.bddtests.steps;

import net.thucydides.core.annotations.Step;
import io.skymind.pathmind.bddtests.page.ProjectsPage;
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
    public void inputModelDetails(String notes) {
        projectsPage.inputModelDetailsNotes(notes);
        projectsPage.clickWizardModelDetailsNextBtn();
    }
    @Step
    public void checkThatProjectPageOpened(String projectName) {
        projectsPage.checkThatProjectPageOpened(projectName);
    }
	@Step
	public void checkThatExperimentPageOpened(String projectName) {
		projectsPage.checkThatExperimentPageOpened(projectName);
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
    public void inputExperimentNotes(String notes) {
        projectsPage.inputExperimentNotes(notes);
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
    public void clickTheModelName(String modelName) {
        projectsPage.clickTheModelName(modelName);
    }
    @Step
    public void clickTheExperimentName(String experimentName) {
        projectsPage.clickTheExperimentName(experimentName);
    }
    @Step
    public void clickProjectsArchiveButton(String projectName) {
        projectsPage.clickProjectsArchiveButton(projectName);
    }
	@Step
	public void clickExperimentArchiveButton() {
		projectsPage.clickExperimentArchiveButton();
	}
	@Step
	public void clickExperimentUnArchiveButton() {
		projectsPage.clickExperimentUnArchiveButton();
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
	@Step
	public void checkThatNewExperimentPageOpened() {
		projectsPage.checkThatNewExperimentPageOpened();
	}

	@Step
	public void checkThatExperimentPageOfTheProjectOpened(String projectName) {
		projectsPage.checkThatExperimentPageOfTheProjectOpened(projectName);
	}
	@Step
	public void clickModelArchiveButton(String model) {
		projectsPage.clickModelArchiveButton(model);
	}
	@Step
	public void openArchivesTab() {
		projectsPage.clickArchivesTab();
	}
	@Step
	public void openModelsTab() {
		projectsPage.clickModelsTab();
	}
	@Step
	public void openProjectsTab() {
		projectsPage.clickProjectsTab();
	}
	@Step
	public void checkThatModelUploadPageOpened() {
    	projectsPage.checkThatModelUploadPageOpened();
	}
	@Step
	public void clickWizardModelDetailsNextBtn() {
		projectsPage.clickWizardModelDetailsNextBtn();
	}
	@Step
	public void checkExperimentStatusCompletedWithLimitHours(int limit) {
		projectsPage.checkExperimentStatusCompletedWithLimitHours(limit);
    }
	@Step
	public void inputVariableNames(String[] variableNames) {
		for (int i = 0; i < variableNames.length; i++) {
			projectsPage.inputVariableName(variableNames[i], i);
		}
	}
	@Step
	public void updateVariableNameWithIndex(int variableIndex, String variableName) {
		projectsPage.inputVariableName(variableName, variableIndex);
	}
	@Step
	public void checkCodeEditorRowHasVariableMarked(int row, int expectedSize, String variableName, int variableIndex) {
		projectsPage.checkCodeEditorRowHasVariableMarked(row, expectedSize, variableName, variableIndex);
	}
	@Step
    public void checkErrorMessageInModelCheckPanel(String errorMessage) {
        projectsPage.checkErrorMessageInModelCheckPanel(errorMessage);
    }

	@Step
	public void addNoteToTheProjectPage(String note) {
		projectsPage.addNoteToTheProjectPage(note);
		projectsPage.projectPageClickSaveBtn();
	}
	@Step
	public void checkProjectNoteIs(String note) {
		projectsPage.checkProjectNoteIs(note);
	}
	@Step
	public void addNoteToTheExperimentPage(String note) {
		projectsPage.addNoteToTheExperimentPage(note);
		projectsPage.projectPageClickSaveBtn();
	}
	@Step
	public void checkExperimentNotesIs(String note) {
		projectsPage.checkExperimentNotesIs(note);
	}
	@Step
	public void checkOnTheModelPageExperimentNotesIs(String experiment, String note) {
		projectsPage.checkOnTheModelPageExperimentNotesIs(experiment, note);
	}
    @Step
    public void checkThatTheExperimentStatusIsDifferentFrom(String status) {
        projectsPage.checkThatTheExperimentStatusIsDifferentFrom(status);
    }
    @Step
    public void checkThatTheExperimentStatusIs(String status) {
        projectsPage.checkThatTheExperimentStatusIs(status);
    }
	@Step
	public void clickWizardRewardVariableNamesNextBtn() {
		projectsPage.clickWizardRewardVariableNamesNextBtn();
	}
    @Step
    public void checkNumberOfProjectsWithDraftTag(int numberOfProjects) {
        projectsPage.checkNumberOfProjectsWithDraftTag(numberOfProjects);
    }
    @Step
    public void clickTheFirstDraftModel() {
        projectsPage.clickTheFirstDraftModel();
    }
    @Step
    public void checkThatWeCanAddMoreInfoToTheDraftModel() {
        projectsPage.inputModelDetailsNotes("random note");
    }

    @Step
    public void checkThatResumeUploadPageIsOpened() {
        projectsPage.checkThatResumeUploadPageIsOpened();
    }

    @Step
    public void checkThatTheNotesFieldHasTheValue(String text) {
        projectsPage.checkThatTheNotesFieldHasTheValue(text);
    }
    @Step
    public void checkThatRewardVariablesPageOpened() {
        projectsPage.checkThatRewardVariablesPageOpened();
    }
    @Step
    public void fillNotesFieldAs(String notes) {
        projectsPage.inputModelDetailsNotes(notes);
    }
	@Step
	public void inputProjectNameToTheEditPopup(String projectName) {
		projectsPage.inputProjectNameToTheEditPopup(projectName);
	}
	@Step
	public void checkThatProjectNameOnProjectPage(String name) {
		projectsPage.checkThatProjectNameDetailsOnProjectPage(name);
		projectsPage.checkThatProjectNameBreadcrumbOnProjectPage(name);
	}
    @Step
    public void clickWizardRewardVariablesSaveDraftBtn() {
        projectsPage.clickWizardRewardVariablesSaveDraftBtn();
    }
    @Step
    public void checkThatThereIsAVariableNamed(String variableName) {
        projectsPage.checkThatThereIsAVariableNamed(variableName);
    }
    @Step
    public void clickEditProjectIconFromProjectsPage(String projectName) {
        projectsPage.clickEditProjectIconFromProjectsPage(projectName);
    }
    @Step
    public void checkNewProjectNameErrorShown(String error) {
        projectsPage.checkNewProjectNameErrorShown(error);
    }
}
