package pathmind.page;

import net.serenitybdd.core.Serenity;
import net.serenitybdd.core.pages.PageObject;
import net.thucydides.core.annotations.DefaultUrl;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pathmind.Utils;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@DefaultUrl("page:home.page")
public class ProjectsPage extends PageObject {

    private Utils utils;

    @FindBy(xpath = "//vaadin-button[text()='New project']")
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
    @FindBy(xpath = "//label[text()='Number of Observations for Training']/following-sibling::vaadin-number-field")
    private WebElement modelDetailsObservationsShadow;
    @FindBy(xpath = "//label[text()='Number of Possible Actions']/following-sibling::vaadin-number-field")
    private WebElement modelDetailsActionsShadow;
    @FindBy(xpath = "//label[text()='getObservation for Reward Function']/following-sibling::vaadin-text-area")
    private WebElement modelDetailsRewardShadow;
    @FindBy(xpath = "//a[text()='Projects' and @href='projects']")
    private WebElement headerProjectsBtn;
    @FindBy(xpath = "//vaadin-grid-cell-content")
    private List<WebElement> projectsNames;
    @FindBy(xpath = "(//vaadin-text-area)[1])")
    private WebElement errorsTextFieldShadow;
    @FindBy(xpath = "(//vaadin-text-area)[2]")
    private WebElement getObservationTextFieldShadow;
    @FindBy(xpath = "(//vaadin-text-area)[3]")
    private WebElement tipsTextFieldShadow;
    @FindBy(xpath = "//juicy-ace-editor")
    private WebElement rewardField;
    @FindBy(xpath = "//vaadin-button[text()='Start Discovery Run']")
    private WebElement startDiscoveryRunBtn;
    @FindBy(xpath = "//vaadin-dialog-overlay")
    private WebElement dialogShadow;
    @FindBy(xpath = "//label[text()='Completed']")
    private WebElement experimentStatusCompleted;
    @FindBy(xpath = "//vaadin-text-field[@tabindex='0']")
    private WebElement searchFieldShadow;
    @FindBy(id = "overlay")
    private WebElement overlayShadow;
    @FindBy(xpath = "//vaadin-button[@theme='primary']")
    private WebElement projectPageUploadBtnShadow;
    @FindBy(xpath = "//vaadin-grid")
    private WebElement projectPageModelsTable;
    @FindBy(xpath = "//label[@class='section-label-title']")
    private WebElement pageLabel;
    @FindBy(xpath = "//juicy-ace-editor")
    private WebElement juicyAceEditorShadow;
    @FindBy(xpath = "//vaadin-text-area")
    private WebElement getObservationsShadow;
    @FindBy(xpath = "//a[@class='breadcrumb']")
    private List<WebElement> breadcrumb;

    private By byInput = By.cssSelector("input");

    public void clickCreateNewProjectBtn() {
        waitABit(2000);
        createNewProjectBtn.click();
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
    public void clickModelDetailsNextStepButton() {
        Actions actions = new Actions(getDriver());
        actions.moveToElement(pathmindHelperNextStepBtn);
        actions.perform();
        pathmindHelperNextStepBtn.click();
    }

    public void uploadModelFile(String model) {
//        getDriver().findElement(By.xpath("//vaadin-button[text()='Upload as zip file']")).click();

        WebElement e = utils.expandRootElement(uploadShadow);
        WebElement projectNameInputField = e.findElement(byInput);
        File file = new File("models/" + model);
//        System.out.println("ABSOLUTE PATH: "+file.getAbsolutePath());
        projectNameInputField.sendKeys(file.getAbsolutePath());
    }

    public void clickCheckModelBtn() {
        waitFor(ExpectedConditions.elementToBeClickable(checkModelBtn));
        Actions actions = new Actions(getDriver());
        actions.moveToElement(checkModelBtn).click().perform();
    }

    public void inputModelDetailsObservation(String observation) {
        WebElement e = utils.expandRootElement(modelDetailsObservationsShadow);
        WebElement observationInputField = e.findElement(byInput);
        observationInputField.click();
        observationInputField.clear();
        observationInputField.sendKeys(observation);
    }

    public void inputModelDetailsAction(String action) {
        WebElement e = utils.expandRootElement(modelDetailsActionsShadow);
        WebElement actionInputField = e.findElement(byInput);
        actionInputField.click();
        actionInputField.clear();
        actionInputField.sendKeys(action);
    }

    public void inputModelDetailsReward(String getObservationFile) throws IOException {
        WebElement e = utils.expandRootElement(modelDetailsRewardShadow);
        WebElement rewardInputField = e.findElement(By.cssSelector("textarea"));
        rewardInputField.click();
        rewardInputField.clear();
        rewardInputField.sendKeys(FileUtils.readFileToString(new File("models/" + getObservationFile), StandardCharsets.UTF_8));
    }

    public void checkThatProjectPageOpened(String projectName) {
        assertThat(getDriver().findElement(By.xpath("//label[@class='section-label-subtitle']")).getText(), containsString(projectName));
    }

    public void clickHeaderProjectsBtn() {
        headerProjectsBtn.click();
        waitABit(2000);
    }

    public void checkThatProjectExistInProjectsList(String projectName) {
        Actions actions = new Actions(getDriver());
        actions.moveToElement(getDriver().findElement(By.xpath("//vaadin-grid-cell-content[text()='"+projectName+"']")));
        actions.perform();
        List<String> strings = new ArrayList<>();
        for(WebElement e : projectsNames){
            strings.add(e.getText());
        }
        assertThat(strings, hasItem(projectName));
    }

    public void checkThatObservationFunctionDisplayed(String getObservationFile) throws IOException {
        WebElement e = utils.expandRootElement(getObservationTextFieldShadow);
        WebElement getObservationTextField = e.findElement(By.cssSelector("textarea"));
        assertThat(getObservationTextField.getAttribute("value").replace("\n", "").replace("\r", ""),
                equalTo(FileUtils.readFileToString(new File("models/" + getObservationFile), StandardCharsets.UTF_8).replace("\n", "").replace("\r", "")));
    }

    public void inputRewardFunctionFile(String rewardFile) throws IOException {
        rewardField.click();
        rewardField.sendKeys(FileUtils.readFileToString(new File("models/" + rewardFile), StandardCharsets.UTF_8));
    }

    public void clickProjectStartDiscoveryRunButton() {
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

    public void clickProjectName(String project) {
        getDriver().findElement(By.xpath("//vaadin-grid-cell-content[text()='" + project + Serenity.sessionVariableCalled("randomNumber") + "']")).click();
    }

    public void clickTheModelName(String modelName) {
        getDriver().findElement(By.xpath("//vaadin-grid-cell-content[text()='"+modelName+"']")).click();
    }

    public void clickTheExperimentName(String experimentName) {
        waitABit(2000);
        WebElement experiment = getDriver().findElement(By.xpath("//vaadin-grid-cell-content[text()='"+experimentName+ " " + "']"));
        waitFor(ExpectedConditions.elementToBeClickable(experiment));
        experiment.click();
    }

    public void clickProjectsArchiveButton() {
        waitABit(2000);
        getDriver().findElement(By.xpath("//vaadin-button[@theme='icon tertiary']")).click();
    }

    public void confirmArchivePopup() {
        WebElement overlay = utils.expandRootElement(dialogShadow);
        WebElement d = overlay.findElement(By.cssSelector("#content"));
        WebElement dialog = utils.expandRootElement(d);
        WebElement confirmBtn = dialog.findElement(By.cssSelector("#confirm"));
        confirmBtn.click();
    }

    public void switchProjectsTab() {
        getDriver().findElement(By.xpath("//vaadin-tab[@aria-selected='false']")).click();
    }

    public void checkThatProjectNotExistInProjectList(String projectName) {
        List<String> strings = new ArrayList<>();
        for(WebElement e : projectsNames){
            strings.add(e.getText());
        }
        assertThat(strings, not(hasItem(projectName)));
    }

    public void checkCreateANewProjectPage() {
        assertThat(getDriver().findElement(By.xpath("//label[@class='light-text-label']")).getText(), containsString("Welcome to"));
        assertThat(getDriver().findElement(By.xpath("//img[@class='navbar-logo']")).isDisplayed(), is(true));
        assertThat(getDriver().findElement(By.cssSelector(".section-title-label")).getText(), containsString("Start a New Project!"));
        assertThat(getDriver().findElement(By.cssSelector(".section-subtitle-label")).getText(), containsString("Projects organize your Pathmind Experiments based on your AnyLogic model"));

        WebElement e = utils.expandRootElement(projectNameInputFieldShadow);
        WebElement searchInputField = e.findElement(By.cssSelector("label[part='label']"));
        assertThat(searchInputField.getText(), containsString("Give your project a name"));

        assertThat(getDriver().findElement(By.xpath("//vaadin-button")).getText(), containsString("Create Project"));
    }

    public void openProjectOnProjectsPage(String projectName) {
        getDriver().findElement(By.xpath("//vaadin-grid-cell-content[text()='"+projectName+"']")).click();
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
        try {
            getDriver().findElement(By.xpath("//vaadin-button[@theme='tertiary']")).click();
        }
        catch(org.openqa.selenium.StaleElementReferenceException ex)
        {
            getDriver().findElement(By.xpath("//vaadin-button[@theme='tertiary']")).click();
        }
    }

    public void checkThatModelsPageOpened() {
        waitABit(2000);
        assertThat(pageLabel.getText(), is("PROJECT"));
        assertThat(getDriver().getTitle(), is("Pathmind | Models"));
    }

    public void checkThatModelExistInArchivedTab(String modelName) {
        waitABit(2500);
        List<String> strings = new ArrayList<>();
        for(WebElement e : projectsNames){
            strings.add(e.getText());
        }
        assertThat(strings, hasItem(modelName));
    }

    public void checkThatModelNOTExistInArchivedTab() {
        waitABit(2000);
        List<String> strings = new ArrayList<>();
        for(WebElement e : projectsNames){
            strings.add(e.getText());
        }
        assertThat(strings, not(hasItem("1")));
    }

    public void clickProjectPageNewExperimentButton() {
        getDriver().findElement(By.xpath("//vaadin-button[text()='New Experiment']")).click();
        waitFor(ExpectedConditions.presenceOfElementLocated(By.xpath("//label[text()='Write your reward function:']")));
    }

    public void inputRewardFunction(String rewardFunction) {
        try {
            rewardField.click();
        }
        catch(org.openqa.selenium.StaleElementReferenceException ex)
        {
            rewardField.click();
        }
        rewardField.click();
        rewardField.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        rewardField.sendKeys(Keys.BACK_SPACE);
        rewardField.sendKeys(rewardFunction);
    }

    public void clickProjectSaveDraftBtn() {
        Actions action = new Actions(getDriver());
        WebElement we = getDriver().findElement(By.xpath("//vaadin-button[text()='Save Draft']"));
        action.moveToElement(we).build().perform();
        getDriver().findElement(By.xpath("//vaadin-button[text()='Save Draft']")).click();
    }

    public void clickExperimentShowRewardFunctionBtn(String experimentName) {
        String slotAttr = getDriver().findElement(By.xpath("//vaadin-grid-cell-content[text()='"+experimentName+" "+"']")).getAttribute("slot");
        int showRewardBtnNumber = Integer.parseInt(slotAttr.split("grid-cell-content-")[1]) + 4;
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
        assertThat(getDriver().findElement(By.xpath("//vaadin-button[@class='action-button'][2]")).getAttribute("title"), is("Show reward function"));
        List<String> strings = new ArrayList<>();
        for(WebElement e : breadcrumb){
            strings.add(e.getText());
        }
        assertThat(strings, hasItem("Projects"));
        assertThat(strings, hasItem("AutotestProject" + Serenity.sessionVariableCalled("randomNumber")));
        assertThat(strings, hasItem("Model #1"));
    }

    public void clickProjectsBreadcrumbBtn(String breadcrumb) {
        getDriver().findElement(By.xpath("//a[@class='breadcrumb' and contains(@href,'"+breadcrumb+"')]")).click();
    }

    public void checkThatExperimentsPageOpened() {
        assertThat(getDriver().getCurrentUrl(), containsString("experiments"));
        assertThat(getDriver().getTitle(), is("Pathmind | Experiments"));
    }

    public void checkExperimentModelStatusIsStarting(String status) {
        List<String> strings = new ArrayList<>();
        for(WebElement e : projectsNames){
            strings.add(e.getText());
        }
        assertThat(strings, hasItem(status));
    }
}
