package io.skymind.pathmind.bddtests.page;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInRelativeOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.google.common.collect.Ordering;

import io.skymind.pathmind.bddtests.Utils;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.core.pages.PageObject;
import net.thucydides.core.annotations.DefaultUrl;

@DefaultUrl("page:home.page")
public class ProjectsPage extends PageObject {

    private Utils utils;

    @FindBy(xpath = "//vaadin-text-field[@required]")
    private WebElement projectNameInputFieldShadow;
    @FindBy(xpath = "//vaadin-button[text()='Create Project']")
    private WebElement projectNameCreateBtn;
    @FindBy(xpath = "(//vaadin-button[contains(text(),'Next')])[last()]")
    private WebElement pathmindHelperNextStepBtn;
    @FindBy(xpath = "//vaadin-upload")
    private WebElement uploadShadow;
    @FindBy(xpath = "//span[text()='Notes']/following-sibling::vaadin-text-area")
    private WebElement modelDetailsNotesShadow;
    @FindBy(xpath = "//vaadin-grid-cell-content")
    private List<WebElement> experimentModelsNames;
    @FindBy(xpath = "//juicy-ace-editor")
    private WebElement rewardField;
    @FindBy(xpath = "//vaadin-text-area[1]")
    private WebElement experimentNotesField;
    @FindBy(xpath = "//vaadin-button[text()='Train Policy']")
    private WebElement startDiscoveryRunBtn;
    @FindBy(xpath = "//vaadin-dialog-overlay")
    private WebElement dialogShadow;
    @FindBy(xpath = "//vaadin-button[@theme='primary']")
    private WebElement projectPageUploadBtnShadow;
    @FindBy(xpath = "//vaadin-grid")
    private WebElement projectPageModelsTable;
    @FindBy(xpath = "//juicy-ace-editor")
    private WebElement juicyAceEditorShadow;
    @FindBy(xpath = "//*[@class='breadcrumb']")
    private List<WebElement> breadcrumb;
    @FindBy(xpath = "//vaadin-button[@title='Archive']")
    private WebElement archiveBtnShadow;
    @FindBy(xpath = "//vaadin-button[@title='Unarchive']")
    private WebElement unarchiveBtnShadow;
    @FindBy(xpath = "//vaadin-text-field[contains(@class,'reward-variable-name-field')]")
    private List<WebElement> rewardVariableNameInputs;
    @FindBy(xpath = "//vaadin-text-area[@theme='notes']")
    private WebElement notesField;
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

    public void inputModelDetailsNotes(String notes) {
        WebElement e = utils.expandRootElement(modelDetailsNotesShadow);
        WebElement notestTextArea = e.findElement(byTextarea);
        notestTextArea.click();
        notestTextArea.clear();
        notestTextArea.sendKeys(notes);
    }

    public void checkThatProjectPageOpened(String projectName) {
        assertThat(getDriver().findElement(By.xpath("//span[@class='breadcrumb']")).getText(), containsString(projectName));
    }
    public void checkThatExperimentPageOpened(String projectName) {
        assertThat(getDriver().findElement(By.xpath("//a[contains(@href, 'project/')]")).getText(), containsString(projectName));
    }

    public void checkThatProjectExistInProjectsList(String projectName) {
        utils.moveToElementRepeatIfStaleException(By.xpath("//span[text()='" + projectName + "']/ancestor::vaadin-grid-cell-content"));
        assertThat(utils.getStringListRepeatIfStaleException(By.xpath("//*[@class='project-name-column']/descendant::span")), hasItem(projectName));
    }

    public void inputRewardFunctionFile(String rewardFile) throws IOException {
        rewardField.click();
        rewardField.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
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

    public void clickTheModelName(String modelName) {
        getDriver().findElement(By.xpath("//vaadin-grid-cell-content[normalize-space(text())='" + modelName + "']")).click();
    }

    public void clickTheExperimentName(String experimentName) {
        waitABit(2000);
        WebElement experiment = getDriver().findElement(By.xpath("//vaadin-grid-cell-content[text()='" + experimentName + " " + "']"));
        waitFor(ExpectedConditions.elementToBeClickable(experiment));
        experiment.click();
        waitABit(2000);
    }

    public void clickProjectsArchiveButton(String projectName) {
        waitABit(2000);
        getDriver().findElement(By.xpath("//span[text()='" + projectName + "']/ancestor::vaadin-grid-cell-content/following-sibling::vaadin-grid-cell-content[4]/descendant::vaadin-button")).click();
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
    public void clickArchivesTab() {
        utils.clickElementRepeatIfStaleException(By.xpath("//vaadin-tab[text()='Archives']"));
    }
    public void clickModelsTab() {
        getDriver().findElement(By.xpath("//vaadin-tab[text()='Models']")).click();
    }
    public void clickProjectsTab() {
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
        WebElement project = getDriver().findElement(By.xpath("//span[text()='" + projectName + "']/ancestor::vaadin-grid-cell-content"));
        waitFor(ExpectedConditions.elementToBeClickable(project));
        JavascriptExecutor executor = (JavascriptExecutor) getDriver();
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

    public void checkThatModelsPageOpened() {
        assertThat(getDriver().getCurrentUrl(), containsString("/model/"));
        assertThat(getDriver().getTitle(), is("Pathmind | Model"));
    }

    public void checkThatModelExistInArchivedTab(String modelName) {
        waitABit(2500);
        List<String> strings = new ArrayList<>();
        for (WebElement e : experimentModelsNames) {
            strings.add(e.getText());
        }
        assertThat(strings, hasItem(modelName));
    }

    public void checkThatModelNOTExistInArchivedTab() {
        waitABit(2000);
        List<String> strings = new ArrayList<>();
        for (WebElement e : experimentModelsNames) {
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
        JavascriptExecutor executor = (JavascriptExecutor)getDriver();
        executor.executeScript("arguments[0].click();", we);
        try {
            WebElement closePopUp = getDriver().findElement(By.xpath("//vaadin-button[@theme='icon']"));
            waitFor(ExpectedConditions.visibilityOf(closePopUp));
            waitFor(ExpectedConditions.elementToBeClickable(closePopUp));
            action.moveToElement(closePopUp).click().perform();
        } catch (Exception e) {
            System.out.println("Button not exist");
        }
    }

    public void checkRewardFunctionIs(String rewardFunction) {
        waitABit(2000);
        WebElement e = utils.expandRootElement(juicyAceEditorShadow);
        assertThat(e.findElement(By.cssSelector(".ace_line")).getText(), is(rewardFunction));
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
        for (WebElement e : breadcrumb) {
            strings.add(e.getText());
        }
        assertThat(strings, hasItem("Projects"));
        assertThat(strings, hasItem("AutotestProject" + Serenity.sessionVariableCalled("randomNumber")));
        assertThat(strings, hasItem("Model #1"));
    }

    public void clickProjectsBreadcrumbBtn(String breadcrumb) {
        WebElement bread = getDriver().findElement(By.xpath("//a[@class='breadcrumb' and contains(@href,'" + breadcrumb + "')]"));
        waitFor(ExpectedConditions.elementToBeClickable(bread));
        bread.click();
        waitABit(2500);
    }

    public void checkExperimentModelStatusIsStarting(String status) {
        List<String> strings = new ArrayList<>();
        for (WebElement e : experimentModelsNames) {
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
        waitFor(ExpectedConditions.visibilityOfElementLocated(By.xpath("//vaadin-vertical-layout[normalize-space(text())='" + errorMessage + "']")));
        By xpath = By.xpath("//vaadin-progress-bar/parent::*");
        assertThat(getDriver().findElement(xpath).getText(), is(errorMessage));
        resetImplicitTimeout();
    }

    public void addNoteToTheProjectPage(String note) {
        notesField.click();
        notesField.sendKeys(note);
    }
    public void projectPageClickSaveBtn() {
        getDriver().findElement(By.xpath("//vaadin-vertical-layout[@class='notes-block']/descendant::vaadin-button")).click();
    }

    public void checkProjectNoteIs(String note) {
        assertThat(notesField.getAttribute("value"), is(note));
    }

    public void checkOnTheModelPageExperimentNotesIs(String experiment, String note) {
        assertThat(utils.getStringRepeatIfStaleException(By.xpath("//vaadin-grid-cell-content[text()='" + experiment + " ']/following-sibling::vaadin-grid-cell-content[4]")), is(note));
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
        assertThat(variables, hasItem(variableName));
    }

    public void clickEditProjectIconFromProjectsPage(String projectName) {
        waitABit(3000);
        WebElement project = getDriver().findElement(By.xpath("//span[text()='" + projectName + "']/ancestor::vaadin-grid-cell-content"));
        Actions actions = new Actions(getDriver());
        actions.moveToElement(project);
        actions.perform();
        WebElement editProjectBtn = getDriver().findElement(By.xpath("//span[text()='" + projectName + "']/ancestor::vaadin-horizontal-layout/descendant::iron-icon[@icon='vaadin:edit']"));
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

    public void checkModelPageModelDetailsPackageNameIs(String packageName) {
        assertThat(utils.getTextRootElement(getDriver().findElement(By.xpath("//span[text()='Package Name']/ancestor::p"))), is(packageName));
    }

    public void checkModelPageModelDetailsActionsIs(String actions) {
        assertThat(utils.getTextRootElement(getDriver().findElement(By.xpath("//span[text()='Actions']/ancestor::p"))), is(actions));
    }

    public void checkModelPageModelDetailsObservationsIs(String observations) {
        assertThat(utils.getTextRootElement(getDriver().findElement(By.xpath("//span[text()='Observations']/ancestor::p"))), is(observations));
    }

    public void checkModelPageModelDetailsRewardVariablesOrder() {
        List<String> variables = new ArrayList<>();

        for (WebElement webElement : getDriver().findElements(By.xpath("//div[@class='model-reward-variables']/descendant::span[not(@class)]"))) {
            variables.add(webElement.getText());
        }

        assertThat(Ordering.natural().isOrdered(variables), is(true));
    }

    public void checkModelPageModelDetailsRewardVariablesIs(String commaSeparatedVariableNames) {
        List<String> items = Arrays.asList(commaSeparatedVariableNames.split("\\s*,\\s*"));
        List<String> actual = new ArrayList<>();
        for (WebElement webElement : getDriver().findElements(By.xpath("//div[@class='model-reward-variables']/descendant::span[@class]"))) {
            actual.add(webElement.getText());
        }

        assertThat(actual, containsInRelativeOrder(items.toArray()));
    }

    public void checkThatModelNameExistInArchivedTab(String experiment) {
        assertThat(utils.getStringListRepeatIfStaleException(By.xpath("//vaadin-grid-cell-content")), hasItem(experiment));
    }

    public void checkThatCheckmarkIsShown() {
        assertThat(getDriver().findElement(By.xpath("//iron-icon[@icon='vaadin:check' and @class='fade-in']")).isDisplayed(), is(true));
    }

    public void checkThatNotesSavedMsgShown() {
        assertThat(getDriver().findElement(By.xpath("//span[text()='Notes saved!' and @class='fade-out-hint-label fade-in']")).isDisplayed(), is(true));
    }

    public void checkRewardFunctionDefaultValue(String reward) {
        WebElement e = utils.expandRootElement(rewardField);
        assertThat(e.findElement(By.cssSelector("div[class='ace_line']")).getText(), is(reward));
    }

    public void checkThatNewProjectPageOpened() {
        assertThat(getDriver().findElement(By.xpath("//span[@class='section-title-label']")).getText(), is("Start a New Project!"));
    }

    public void checkThatModelNotExistInArchivedTab(String modelName) {
        waitABit(2500);
        List<String> strings = new ArrayList<>();
        for (WebElement e : experimentModelsNames) {
            strings.add(e.getText());
        }
        assertThat(strings, not(hasItem(modelName)));
    }
}