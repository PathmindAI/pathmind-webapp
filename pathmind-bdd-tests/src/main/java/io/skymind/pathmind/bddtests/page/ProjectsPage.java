package io.skymind.pathmind.bddtests.page;

import io.skymind.pathmind.bddtests.Utils;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.core.pages.PageObject;
import net.thucydides.core.annotations.DefaultUrl;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@DefaultUrl("page:home.page")
public class ProjectsPage extends PageObject {

    private Utils utils;

    @FindBy(xpath = "//vaadin-button[text()='New Project']")
    private WebElement createNewProjectBtn;
    @FindBy(xpath = "//vaadin-text-field[@required]")
    private WebElement projectNameInputFieldShadow;
    @FindBy(xpath = "//vaadin-button[text()='Create Project']")
    private WebElement projectNameCreateBtn;
    @FindBy(xpath = "(//vaadin-button[contains(text(),'Next')])[last()]")
    private WebElement pathmindHelperNextStepBtn;
    @FindBy(xpath = "//vaadin-upload")
    private WebElement uploadShadow;
    @FindBy(xpath = "//vaadin-button[text()='Check Your model']")
    private WebElement checkModelBtn;
    @FindBy(xpath = "//span[text()='Notes']/following-sibling::vaadin-text-area")
    private WebElement modelDetailsNotesShadow;
    @FindBy(xpath = "//span[text()='Reward Variables']/following-sibling::vaadin-text-area")
    private WebElement modelDetailsRewardShadow;
    @FindBy(xpath = "//a[text()='Projects' and @href='projects']")
    private WebElement headerProjectsBtn;
    @FindBy(xpath = "//vaadin-grid-cell-content")
    private List<WebElement> experimentModelsNames;
    @FindBy(xpath = "//*[@class='project-name-column']/descendant::span")
    private List<WebElement> projectsNames;
    @FindBy(xpath = "(//vaadin-text-area)[1])")
    private WebElement errorsTextFieldShadow;
    @FindBy(xpath = "(//vaadin-text-area)[2]")
    private WebElement getObservationTextFieldShadow;
    @FindBy(xpath = "(//vaadin-text-area)[2]")
    private WebElement tipsTextFieldShadow;
    @FindBy(xpath = "//juicy-ace-editor")
    private WebElement rewardField;
    @FindBy(xpath = "//vaadin-text-area[1]")
    private WebElement experimentNotesField;
    @FindBy(xpath = "//vaadin-button[text()='Train Policy']")
    private WebElement startDiscoveryRunBtn;
    @FindBy(xpath = "//vaadin-dialog-overlay")
    private WebElement dialogShadow;
    @FindBy(xpath = "//span[text()='Status']/following-sibling::span[1]")
    private WebElement experimentStatusCompleted;
    @FindBy(xpath = "//vaadin-text-field[@tabindex='0']")
    private WebElement searchFieldShadow;
    @FindBy(id = "overlay")
    private WebElement overlayShadow;
    @FindBy(xpath = "//vaadin-button[@theme='primary']")
    private WebElement projectPageUploadBtnShadow;
    @FindBy(xpath = "//vaadin-grid")
    private WebElement projectPageModelsTable;
    @FindBy(xpath = "//*[@class='section-label-title']")
    private WebElement pageLabel;
    @FindBy(xpath = "//juicy-ace-editor")
    private WebElement juicyAceEditorShadow;
    @FindBy(xpath = "//vaadin-text-area")
    private WebElement getObservationsShadow;
    @FindBy(xpath = "//*[@class='breadcrumb']")
    private List<WebElement> breadcrumb;
    @FindBy(xpath = "//*[@id='skipToUploadModelBtn']")
	private WebElement skipToUploadBtnShadow;
    @FindBy(xpath = "//vaadin-button[@title='Archive']")
	private WebElement archiveBtnShadow;
	@FindBy(xpath = "//vaadin-button[@title='Unarchive']")
	private WebElement unarchiveBtnShadow;
	@FindBy(xpath = "//vaadin-text-field[@class='reward-variable-name-field']")
	private List<WebElement> rewardVariableNameInputs;
	@FindBy(xpath = "//vaadin-text-area[@theme='notes']")
	private WebElement notesField;
	@FindBy(xpath = "//*[text()='Experiment Notes']/ancestor::*[@class='notes-field-wrapper']/descendant::vaadin-text-area")
	private WebElement experimentNotes;
	@FindBy(xpath = "//vaadin-text-field")
	private WebElement editProjectNameInputShadow;

    private final By byInput = By.cssSelector("input");
    private final By byTextarea = By.cssSelector("textarea");

    public void clickCreateNewProjectBtn() {
        waitABit(2000);
        getDriver().findElement(By.xpath("//vaadin-button[text()='New Project']")).click();
    }

    public void inputNameOfTheNewProject(String projectName) {
        WebElement e = utils.expandRootElement(projectNameInputFieldShadow);
        WebElement projectNameInputField = e.findElement(byInput);
        projectNameInputField.click();
        projectNameInputField.sendKeys(projectName);
    }

    public void clickProjectNameCreateBtn() {
        waitABit(3000);
        projectNameCreateBtn.click();
    }

    public void clickPathmindHelperNextStepButton() {
        Actions actions = new Actions(getDriver());
        actions.moveToElement(pathmindHelperNextStepBtn);
        actions.perform();
        pathmindHelperNextStepBtn.click();
    }

    public void uploadModelFile(String model) {
		waitABit(2500);
        getDriver().findElement(By.xpath("//vaadin-button[text()='Upload as Zip']")).click();
        waitABit(2500);
        WebElement e = utils.expandRootElement(uploadShadow);
        WebElement projectNameInputField = e.findElement(byInput);

		upload(System.getProperty("user.dir") + "/models/" + model).fromLocalMachine().to(projectNameInputField);
    }

    public void clickCheckModelBtn() {
        waitFor(ExpectedConditions.elementToBeClickable(checkModelBtn));
        Actions actions = new Actions(getDriver());
        actions.moveToElement(checkModelBtn).click().perform();
    }

    public void inputModelDetailsNotes(String notes) {
        WebElement e = utils.expandRootElement(modelDetailsNotesShadow);
        WebElement notestTextArea = e.findElement(byTextarea);
        notestTextArea.click();
        notestTextArea.clear();
        notestTextArea.sendKeys(notes);
    }

    public void inputModelDetailsReward(String getObservationFile) throws IOException {
        WebElement e = utils.expandRootElement(modelDetailsRewardShadow);
        WebElement rewardInputField = e.findElement(byTextarea);
        rewardInputField.click();
        rewardInputField.clear();
        rewardInputField.sendKeys(FileUtils.readFileToString(new File("models/" + getObservationFile), StandardCharsets.UTF_8));
    }

    public void checkThatProjectPageOpened(String projectName) {
        assertThat(getDriver().findElement(By.xpath("//span[@class='breadcrumb']")).getText(), containsString(projectName));
    }
	public void checkThatExperimentPageOpened(String projectName) {
		assertThat(getDriver().findElement(By.xpath("//a[contains(@href, 'project/')]")).getText(), containsString(projectName));
	}

    public void clickHeaderProjectsBtn() {
        headerProjectsBtn.click();
        waitABit(2000);
    }

    public void checkThatProjectExistInProjectsList(String projectName) {
        utils.moveToElementRepeatIfStaleException(By.xpath("//span[text()='"+projectName+"']/ancestor::vaadin-grid-cell-content"));
        assertThat(utils.getStringListRepeatIfStaleException(By.xpath("//*[@class='project-name-column']/descendant::span")), hasItem(projectName));
    }

    public void checkThatObservationFunctionDisplayed(String getObservationFile) throws IOException {
        WebElement e = utils.expandRootElement(getObservationTextFieldShadow);
        WebElement getObservationTextField = e.findElement(byTextarea);
        assertThat(getObservationTextField.getAttribute("value").replace("\n", "").replace("\r", ""),
                equalTo(FileUtils.readFileToString(new File("models/" + getObservationFile), StandardCharsets.UTF_8).replace("\n", "").replace("\r", "")));
    }

    public void inputRewardFunctionFile(String rewardFile) throws IOException {
        rewardField.click();
        rewardField.sendKeys(FileUtils.readFileToString(new File("models/" + rewardFile), StandardCharsets.UTF_8));
    }

    public void inputExperimentNotes(String notes) {
        experimentNotesField.click();
        experimentNotesField.sendKeys(notes);
    }

    public void clickProjectStartDiscoveryRunButton() {
		waitFor(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@style='display: none;']")));
    	waitFor(ExpectedConditions.elementToBeClickable(startDiscoveryRunBtn));
    	waitABit(2500);
        startDiscoveryRunBtn.click();
    }

    public void clickOkayInThePopup() {
        waitFor(ExpectedConditions.visibilityOf(dialogShadow));
        WebElement overlay = utils.expandRootElement(dialogShadow);
        WebElement d = overlay.findElement(By.cssSelector("#content"));
        WebElement dialog = utils.expandRootElement(d);
        WebElement okBtn = dialog.findElement(By.cssSelector("#confirm"));
        okBtn.click();
    }

    public void checkExperimentStatusCompleted() {
        for (int i=0; i < 50; i++) {
            if (getDriver().findElements(By.xpath("//label[text()='Completed']")).size() != 0) {
                break;
            } else {
                waitABit(30000);
                getDriver().navigate().refresh();
            }
        }
        assertThat(experimentStatusCompleted.getText(), containsString("Completed"));
    }

    public void inputToTheProjectsSearchField(String projectName) {
        waitABit(2000);
        WebElement e = utils.expandRootElement(searchFieldShadow);
        WebElement searchInputField = e.findElement(By.cssSelector("input[part='value']"));
        waitFor(ExpectedConditions.elementToBeClickable(searchInputField));
        searchInputField.click();
        searchInputField.clear();
        searchInputField.sendKeys(projectName);
    }

    public void checkThatProjectsSearchFieldWorks(String projectName) {
        assertThat(getDriver().findElement(By.xpath("//vaadin-grid-cell-content[1]")).getText(), containsString(projectName));
    }

    public void clickSearchFieldClearBtn() {
        waitABit(2000);
        WebElement e = utils.expandRootElement(searchFieldShadow);
        WebElement searchClearBtn = e.findElement(By.cssSelector("#clearButton"));
        waitFor(ExpectedConditions.elementToBeClickable(searchClearBtn));
        searchClearBtn.click();
    }

    public void checkThatProjectsInputFieldIsEmpty() {
        WebElement e = utils.expandRootElement(searchFieldShadow);
        WebElement searchInputField = e.findElement(By.cssSelector("input[part='value']"));
        assertThat(searchInputField.getAttribute("value"), isEmptyString());
    }

    public void clickTheModelName(String modelName) {
        getDriver().findElement(By.xpath("//vaadin-grid-cell-content[normalize-space(text())='"+modelName+"']")).click();
    }

    public void clickTheExperimentName(String experimentName) {
        waitABit(2000);
        WebElement experiment = getDriver().findElement(By.xpath("//vaadin-grid-cell-content[text()='"+experimentName+ " " + "']"));
        waitFor(ExpectedConditions.elementToBeClickable(experiment));
        experiment.click();
    }

    public void clickProjectsArchiveButton(String projectName) {
        waitABit(2000);
        getDriver().findElement(By.xpath("//span[text()='"+projectName+"']/ancestor::vaadin-grid-cell-content/following-sibling::vaadin-grid-cell-content[4]/descendant::vaadin-button")).click();
    }

	public void clickExperimentArchiveButton() {
		waitABit(2000);
		WebElement e = utils.expandRootElement(archiveBtnShadow);
		e.findElement(By.cssSelector("button")).click();
	}
	public void clickExperimentUnArchiveButton() {
		waitABit(2000);
		WebElement e = utils.expandRootElement(unarchiveBtnShadow);
		e.findElement(By.cssSelector("button")).click();
	}

    public void confirmArchivePopup() {
        waitABit(2500);
        WebElement overlay = utils.expandRootElement(dialogShadow);
        WebElement d = overlay.findElement(By.cssSelector("#content"));
        WebElement dialog = utils.expandRootElement(d);
        WebElement confirmBtn = dialog.findElement(By.cssSelector("#confirm"));
        confirmBtn.click();
    }

    public void switchProjectsTab() {
        getDriver().findElement(By.xpath("//vaadin-tab[@aria-selected='false']")).click();
    }
    public void clickArchivesTab(){
		utils.clickElementRepeatIfStaleException(By.xpath("//vaadin-tab[text()='Archives']"));
	}
	public void clickModelsTab(){
		getDriver().findElement(By.xpath("//vaadin-tab[text()='Models']")).click();
	}
	public void clickProjectsTab(){
		getDriver().findElement(By.xpath("//vaadin-tab[text()='Active']")).click();
	}

    public void checkThatProjectNotExistInProjectList(String projectName) {
        assertThat(utils.getStringListRepeatIfStaleException(By.xpath("//*[@class='project-name-column']/descendant::span")), not(hasItem(projectName)));
    }

    public void checkCreateANewProjectPage() {
        assertThat(getDriver().findElement(By.xpath("//*[@class='light-text-label']")).getText(), containsString("Welcome to"));
        assertThat(getDriver().findElement(By.xpath("//img[@class='navbar-logo']")).isDisplayed(), is(true));
        assertThat(getDriver().findElement(By.cssSelector(".section-title-label")).getText(), containsString("Start a New Project!"));
        assertThat(getDriver().findElement(By.cssSelector(".section-subtitle-label")).getText(), containsString("Projects organize your Pathmind Experiments based on your AnyLogic model"));

        WebElement e = utils.expandRootElement(projectNameInputFieldShadow);
        WebElement searchInputField = e.findElement(By.cssSelector("label[part='label']"));
        assertThat(searchInputField.getText(), containsString("Give your project a name"));

        assertThat(getDriver().findElement(By.xpath("//vaadin-button")).getText(), containsString("Create Project"));
    }

    public void openProjectOnProjectsPage(String projectName) {
		WebElement project = getDriver().findElement(By.xpath("//span[text()='"+projectName+"']/ancestor::vaadin-grid-cell-content"));
		waitFor(ExpectedConditions.elementToBeClickable(project));
		JavascriptExecutor executor = (JavascriptExecutor)getDriver();
		executor.executeScript("arguments[0].click();", project);
    }

    public void clickUploadModelBtnFromProjectPage() {
        waitABit(2000);
        WebElement e = utils.expandRootElement(projectPageUploadBtnShadow);
        e.findElement(By.cssSelector("#button")).click();
    }

    public void projectPageCheckThatModelsCountIs(int modelsCount) {
        waitABit(2000);
        WebElement e = utils.expandRootElement(projectPageModelsTable);
        assertThat(e.findElements(By.cssSelector("#items tr[part='row']")).size(), is(modelsCount));
    }

    public void clickBackToProjectsBtn() {
        waitFor(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@style='display: none;']")));
        getDriver().findElement(By.xpath("//vaadin-button[@theme='tertiary']")).click();
    }

    public void clickBackToModelsBtn() {
        waitFor(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@style='display: none;']")));
        waitFor(ExpectedConditions.elementToBeClickable(By.xpath("//vaadin-button[@theme='tertiary']")));
        utils.clickElementRepeatIfStaleException(By.xpath("//vaadin-button[@theme='tertiary']"));
    }

    public void checkThatModelsPageOpened() {
		assertThat(getDriver().getCurrentUrl(), containsString("/model/"));
        assertThat(getDriver().getTitle(), is("Pathmind | Model"));
    }

    public void checkThatModelExistInArchivedTab(String modelName) {
        waitABit(2500);
        List<String> strings = new ArrayList<>();
        for(WebElement e : experimentModelsNames){
            strings.add(e.getText());
        }
        assertThat(strings, hasItem(modelName));
    }

    public void checkThatModelNOTExistInArchivedTab() {
        waitABit(2000);
        List<String> strings = new ArrayList<>();
        for(WebElement e : experimentModelsNames){
            strings.add(e.getText());
        }
        assertThat(strings, not(hasItem("1")));
    }

    public void clickProjectPageNewExperimentButton() {
        getDriver().findElement(By.xpath("//vaadin-button[text()='New Experiment']")).click();
        waitFor(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[text()='Write your reward function']")));
    }

    public void inputRewardFunction(String rewardFunction) {
    	waitABit(2500);
    	utils.clickElementRepeatIfStaleException(By.xpath("//juicy-ace-editor"));
        rewardField.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        rewardField.sendKeys(Keys.BACK_SPACE);
        rewardField.sendKeys(rewardFunction);
    }

    public void clickProjectSaveDraftBtn() {
        Actions action = new Actions(getDriver());
        WebElement we = getDriver().findElement(By.xpath("//vaadin-button[text()='Save']"));
        action.moveToElement(we).build().perform();
        getDriver().findElement(By.xpath("//vaadin-button[text()='Save']")).click();
        try {
            WebElement closePopUp = getDriver().findElement(By.xpath("//vaadin-button[@theme='icon']"));
            waitFor(ExpectedConditions.visibilityOf(closePopUp));
            waitFor(ExpectedConditions.elementToBeClickable(closePopUp));
            action.moveToElement(closePopUp).click().perform();
		}catch (Exception e){
        	System.out.println("Button not exist");
		}
    }

    public void clickExperimentShowRewardFunctionBtn(String experimentName) {
        String slotAttr = getDriver().findElement(By.xpath("//vaadin-grid-cell-content[text()='"+experimentName+" "+"']")).getAttribute("slot");
        int showRewardBtnNumber = Integer.parseInt(slotAttr.split("grid-cell-content-")[1]) + 5;
        getDriver().findElement(By.xpath("//vaadin-grid-cell-content[@slot='vaadin-grid-cell-content-"+showRewardBtnNumber+"']/descendant::vaadin-button[@title='Show reward function']")).click();
    }

    public void checkRewardFunctionIs(String rewardFunction) {
        waitABit(2000);
        WebElement e = utils.expandRootElement(juicyAceEditorShadow);
        assertThat(e.findElement(By.cssSelector(".ace_line")).getText(), is(rewardFunction));
    }

    public void clickUploadModelBtn() {
        getDriver().findElement(By.xpath("//vaadin-button[text()='Upload Model']")).click();
    }

    public void projectWizardClickDownloadItHereBtn() {
        getDriver().findElement(By.xpath("//a[text()='download it here']")).click();
    }

    public void projectWizardForMoreDetailsSeeOurDocumentationBtn() {
        getDriver().findElement(By.xpath("//a[text()='For more details, see our documentation']")).click();
    }

    public void checkTextInTheProjectPage() {
        assertThat(getDriver().findElement(By.xpath("//div[@class='view-section']/descendant::div/p[1]")).getText(), containsString("To prepare your AnyLogic model for reinforcement learning, install the Pathmind Helper"));
        assertThat(getDriver().findElement(By.xpath("//div[@class='view-section']/descendant::div/p[2]")).getText(), containsString("The basics:"));
        assertThat(getDriver().findElement(By.xpath("//div[@class='view-section']/descendant::div/p[3]")).getText(), containsString("When you're ready, upload your model in the next step."));
        assertThat(getDriver().findElement(By.xpath("//div[@class='view-section']/descendant::div/p[4]")).getText(), containsString("For more details, see our documentation"));
        assertThat(getDriver().findElement(By.xpath("//div[@class='view-section']/descendant::ol/li[1]")).getText(), containsString("The Pathmind Helper is an AnyLogic palette item that you add to your simulation. You can download it here."));
        assertThat(getDriver().findElement(By.xpath("//div[@class='view-section']/descendant::ol/li[2]")).getText(), containsString("Add Pathmind Helper as a library in AnyLogic."));
        assertThat(getDriver().findElement(By.xpath("//div[@class='view-section']/descendant::ol/li[3]")).getText(), containsString("Add a Pathmind Helper to your model."));
        assertThat(getDriver().findElement(By.xpath("//div[@class='view-section']/descendant::ol/li[4]")).getText(), containsString("Fill in these functions:"));
        assertThat(getDriver().findElement(By.xpath("//div[@class='view-section']/descendant::ol/ul/li[1]")).getText(), containsString("Observation for rewards"));
        assertThat(getDriver().findElement(By.xpath("//div[@class='view-section']/descendant::ol/ul/li[2]")).getText(), containsString("Observation for training"));
        assertThat(getDriver().findElement(By.xpath("//div[@class='view-section']/descendant::ol/ul/li[3]")).getText(), containsString("doAction"));
    }

    public void checkThatErrorShown(String error) {
        waitABit(2500);
        WebElement e = utils.expandRootElement(projectNameInputFieldShadow);
        e.findElement(By.cssSelector("div[part='error-message']"));
        assertThat(e.findElement(By.cssSelector("div[part='error-message']")).getText(), containsString(error));
    }

    public void checkExperimentsPageElements() {
        assertThat(getDriver().findElement(By.xpath("//vaadin-button[@class='action-button'][1]")).getAttribute("title"), is("Archive"));
        List<String> strings = new ArrayList<>();
        for(WebElement e : breadcrumb){
            strings.add(e.getText());
        }
        assertThat(strings, hasItem("Projects"));
        assertThat(strings, hasItem("AutotestProject" + Serenity.sessionVariableCalled("randomNumber")));
        assertThat(strings, hasItem("Model #1"));
    }

    public void clickProjectsBreadcrumbBtn(String breadcrumb) {
    	WebElement bread = getDriver().findElement(By.xpath("//a[@class='breadcrumb' and contains(@href,'"+breadcrumb+"')]"));
		waitFor(ExpectedConditions.elementToBeClickable(bread));
        bread.click();
        waitABit(2500);
    }

    public void checkThatExperimentsPageOpened() {
        assertThat(getDriver().getCurrentUrl(), containsString("experiments"));
        assertThat(getDriver().getTitle(), is("Pathmind | Experiments"));
    }

    public void checkExperimentModelStatusIsStarting(String status) {
        List<String> strings = new ArrayList<>();
        for(WebElement e : experimentModelsNames){
            strings.add(e.getText());
        }
        assertThat(strings, hasItem(status));
    }

	public void checkThatNewExperimentPageOpened() {
		waitFor(ExpectedConditions.urlContains("newExperiment"));
		assertThat(getDriver().getTitle(), is("Pathmind | New Experiment"));
	}

	public void checkThatExperimentPageOfTheProjectOpened(String projectName) {
        assertThat(utils.getStringRepeatIfStaleException(By.xpath("//*[contains(@href,'project/')]")), is(projectName));
	}

	public void clickModelArchiveButton(String model) {
        waitABit(4000);
        WebElement archiveBtn = getDriver().findElement(By.xpath("//vaadin-grid-cell-content[text()='" + model + " " + "']/following-sibling::vaadin-grid-cell-content[4]/descendant::vaadin-button"));
        archiveBtn.click();
	}

	public void checkThatModelUploadPageOpened() {
		assertThat(getDriver().getCurrentUrl(), containsString("uploadModel"));
		assertThat(getDriver().findElement(By.xpath("//span[@class='no-top-margin-label']")).getText(), is("Upload Model"));
	}

	public void clickWizardModelDetailsNextBtn() {
		getDriver().findElement(By.xpath("//span[text()='Model Details']/ancestor::*[@class='view-section']/descendant::vaadin-button[normalize-space(text())='Next']")).click();
	}

	public void clickWizardRewardVariableNamesNextBtn() {
		getDriver().findElement(By.xpath("//span[text()='Reward Variable Names']/ancestor::*[@class='view-section']/descendant::vaadin-button[normalize-space(text())='Next'][2]")).click();
	}

	public void checkExperimentStatusCompletedWithLimitHours(int limit) {
    	System.out.println("!Waiting for training completed with limit " + limit + " hours!");
		for (int i=0; i < limit*60; i++) {
		    String status = getDriver().findElement(By.xpath("//span[text()='Status']/following-sibling::span[1]")).getText();
			if (getDriver().findElements(By.xpath("//span[text()='Completed']")).size() != 0 || status.equals("Error") || status.equals("Stopped")) {
				break;
			} else {
				waitABit(60000);
				getDriver().navigate().refresh();
			}
		}
		assertThat(experimentStatusCompleted.getText(), containsString("Completed"));
	}

	public void inputVariableName(String variableName, int variableIndex) {
		WebElement textField = rewardVariableNameInputs.get(variableIndex);
		WebElement e = utils.expandRootElement(textField);
		WebElement variableNameInputField = e.findElement(byInput);
		variableNameInputField.click();
		variableNameInputField.clear();
		variableNameInputField.sendKeys(variableName);
	}
	
	public void checkCodeEditorRowHasVariableMarked(int row, int expectedSize, String variableName, int variableIndex) {
		waitABit(1000);
		WebElement e = utils.expandRootElement(juicyAceEditorShadow);
		WebElement rowElement = e.findElements(By.className("ace_line")).get(row);
		List<WebElement> rewardVariables = rowElement.findElements(By.className("ace_reward_variable"));
		assertThat("Number of variable occurances", rewardVariables.size() == expectedSize);
		rewardVariables.forEach(rewardVariable -> {
			assertThat(rewardVariable.getText(), is(variableName));
			assertThat(rewardVariable.getAttribute("class"), containsString("variable-color-" + variableIndex));
		});
	}

    public void checkErrorMessageInModelCheckPanel(String errorMessage) {
    	setImplicitTimeout(90, SECONDS);
		waitFor(ExpectedConditions.visibilityOfElementLocated(By.xpath("//vaadin-vertical-layout[normalize-space(text())='"+errorMessage+"']")));
		By xpath = By
                .xpath("//vaadin-progress-bar/parent::*");
        assertThat(getDriver().findElement(xpath).getText(), is(errorMessage));
        resetImplicitTimeout();
    }

    public void checkThatTheExperimentStatusIsDifferentFrom(String status) {
        setImplicitTimeout(5, SECONDS);
        String xpath = String.format("//vaadin-form-item//label[contains(text(), 'Status')]/parent::*//span[contains(text(), '%s')]", status);
        assertThat(getDriver().findElements(By.xpath(xpath)).size(), is(0));
        resetImplicitTimeout();
    }

    public void checkThatTheExperimentStatusIs(String status) {
        setImplicitTimeout(5, SECONDS);
		String trainingStatus = "//span[contains(text(), 'Status')]/following-sibling::span[1]";
        if(status.equals("Stopping")){
			assertThat(getDriver().findElement(By.xpath(trainingStatus)).getText(), either(is(status)).or(is("Stopped")));
		}else {
			assertThat(getDriver().findElement(By.xpath(trainingStatus)).getText(), is(status));
		}
        resetImplicitTimeout();
    }

	public void addNoteToTheProjectPage(String note) {
		notesField.click();
		notesField.sendKeys(note);
	}
	public void projectPageClickSaveBtn(){
		getDriver().findElement(By.xpath("//vaadin-vertical-layout[@class='notes-block']/descendant::vaadin-button")).click();
	}

	public void checkProjectNoteIs(String note) {
		assertThat(notesField.getAttribute("value"), is(note));
	}

	public void addNoteToTheExperimentPage(String note) {
		experimentNotes.click();
		experimentNotes.sendKeys(note);
	}

	public void checkExperimentNotesIs(String note) {
		assertThat(experimentNotes.getAttribute("value"), is(note));
	}

	public void checkOnTheModelPageExperimentNotesIs(String experiment, String note) {
		assertThat(utils.getStringRepeatIfStaleException(By.xpath("//vaadin-grid-cell-content[text()='"+experiment+" ']/following-sibling::vaadin-grid-cell-content[4]")), is(note));
    }

    public void checkNumberOfProjectsWithDraftTag(int numberOfProjects) {
    	setImplicitTimeout(5, SECONDS);
        String xpath = "//vaadin-grid-cell-content/span[@class='tag' and text()='Draft']";
        assertThat(getDriver().findElements(By.xpath(xpath)).size(), is(numberOfProjects));
        resetImplicitTimeout();
    }

    public void clickTheFirstDraftModel() {
        String xpath = "//vaadin-grid-cell-content/span[@class='tag' and text()='Draft']";
        getDriver().findElements(By.xpath(xpath)).get(0).click();
    }

    public void checkThatResumeUploadPageIsOpened() {
        setImplicitTimeout(5, SECONDS);
        String xpath = String.format("//*[text()='%s']", "Your model was successfully uploaded!");
        waitFor(ExpectedConditions.invisibilityOfAllElements(getDriver().findElements(By.xpath(xpath))));
        resetImplicitTimeout();
    }

    public void checkThatTheNotesFieldHasTheValue(String text) {
        WebElement e = utils.expandRootElement(modelDetailsNotesShadow);
        WebElement notestTextArea = e.findElement(byTextarea);
        assertThat(notestTextArea.getAttribute("value"), is(text));
    }

    public void checkThatRewardVariablesPageOpened() {
        setImplicitTimeout(5, SECONDS);
        waitFor(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[text()='Reward Variable Names']")));
        resetImplicitTimeout();
    }

	public void inputProjectNameToTheEditPopup(String projectName) {
		WebElement e = utils.expandRootElement(editProjectNameInputShadow);
		WebElement input = e.findElement(By.cssSelector("input"));
		input.click();
		input.clear();
		input.sendKeys(projectName);
	}

	public void checkThatProjectNameDetailsOnProjectPage(String name) {
        waitABit(3500);
		assertThat(getDriver().findElement(By.xpath("//span[@class='section-title-label project-title-label']")).getText(), is(name));
	}

	public void checkThatProjectNameBreadcrumbOnProjectPage(String name) {
		assertThat(getDriver().findElement(By.xpath("//vaadin-horizontal-layout[@class='page-title']/descendant::span[@class='breadcrumb']")).getText(), is(name));
	}

    public void clickWizardRewardVariablesSaveDraftBtn() {
        getDriver().findElement(By.xpath("//span[text()='Reward Variable Names']/following-sibling::vaadin-button[text()='Save Draft']")).click();
    }

    public void checkThatThereIsAVariableNamed(String variableName) {
        List<String> variables = new ArrayList<>();
        for (WebElement webElement : getDriver().findElements(By.xpath("//vaadin-text-field"))) {
            variables.add(webElement.getAttribute("value"));
        }
        assertThat(variables, hasItem(variableName) );
    }

    public void clickEditProjectIconFromProjectsPage(String projectName) {
        waitABit(3000);
        WebElement project = getDriver().findElement(By.xpath("//span[text()='"+projectName+"']/ancestor::vaadin-grid-cell-content"));
        Actions actions = new Actions(getDriver());
        actions.moveToElement(project);
        actions.perform();
        WebElement editProjectBtn = getDriver().findElement(By.xpath("//span[text()='"+projectName+"']/ancestor::vaadin-horizontal-layout/descendant::iron-icon[@icon='vaadin:edit']"));
        actions.moveToElement(editProjectBtn);
        actions.click();
        actions.perform();
        waitFor(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class='section-title-label' and text()='Rename project']")));
    }

    public void checkNewProjectNameErrorShown(String error) {
        WebElement e = utils.expandRootElement(projectNameInputFieldShadow);
        assertThat(e.findElement(By.cssSelector("div[part='error-message']")).getText(), is(error));
    }

    public void checkThatModelSuccessfullyUploaded() {
        waitFor(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[text()='Your model was successfully uploaded!']")));
    }
}
