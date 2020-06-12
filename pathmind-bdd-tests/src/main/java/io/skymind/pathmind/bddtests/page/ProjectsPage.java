package io.skymind.pathmind.bddtests.page;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import io.skymind.pathmind.bddtests.Utils;
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
    @FindBy(xpath = "//juicy-ace-editor")
    private WebElement juicyAceEditorShadow;
    @FindBy(xpath = "//*[@class='breadcrumb']")
    private List<WebElement> breadcrumb;
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

    public void checkThatProjectExistInProjectsList(String projectName) {
        utils.moveToElementRepeatIfStaleException(By.xpath("//span[text()='" + projectName + "']/ancestor::vaadin-grid-cell-content"));
        assertThat(utils.getStringListRepeatIfStaleException(By.xpath("//*[@class='project-name-column']/descendant::span")), hasItem(projectName));
    }

    public void inputExperimentNotes(String notes) {
        experimentNotesField.click();
        experimentNotesField.sendKeys(notes);
    }

    public void clickProjectsArchiveButton(String projectName) {
        waitABit(2000);
        getDriver().findElement(By.xpath("//span[text()='" + projectName + "']/ancestor::vaadin-grid-cell-content/following-sibling::vaadin-grid-cell-content[4]/descendant::vaadin-button")).click();
    }

    public void clickArchivesTab() {
        utils.clickElementRepeatIfStaleException(By.xpath("//vaadin-tab[text()='Archives']"));
    }

    public void clickProjectsTab() {
        getDriver().findElement(By.xpath("//vaadin-tab[text()='Active']")).click();
    }

    public void checkThatProjectNotExistInProjectList(String projectName) {
        assertThat(utils.getStringListRepeatIfStaleException(By.xpath("//*[@class='project-name-column']/descendant::span")), not(hasItem(projectName)));
    }

    public void openProjectOnProjectsPage(String projectName) {
        waitABit(2500);
        WebElement project = getDriver().findElement(By.xpath("//span[text()='" + projectName + "']/ancestor::vaadin-grid-cell-content"));
        waitFor(ExpectedConditions.elementToBeClickable(project));
        JavascriptExecutor executor = (JavascriptExecutor) getDriver();
        executor.executeScript("arguments[0].click();", project);
    }

    public void checkThatNewExperimentPageOpened() {
        waitFor(ExpectedConditions.urlContains("newExperiment"));
        assertThat(getDriver().getTitle(), is("Pathmind | New Experiment"));
    }

    public void checkThatExperimentPageOfTheProjectOpened(String projectName) {
        assertThat(utils.getStringRepeatIfStaleException(By.xpath("//*[contains(@href,'project/')]")), is(projectName));
    }

    public void clickTheFirstDraftModel() {
        String xpath = "//vaadin-grid-cell-content/span[@class='tag' and text()='Draft']";
        getDriver().findElements(By.xpath(xpath)).get(0).click();
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
}