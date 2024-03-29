package io.skymind.pathmind.bddtests.page.wizard;

import io.skymind.pathmind.bddtests.Utils;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.core.pages.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;

public class ModelUploadPage extends PageObject {

    private Utils utils;

    @FindBy(xpath = "//vaadin-upload")
    private WebElement uploadShadow;
    @FindBy(xpath = "//upload-alp-instructions")
    private WebElement uploadAlpInstructionsShadow;
    @FindBy(xpath = "//span[@class='warning-label']")
    private WebElement warningLabelElement;
    @FindBy(xpath = "//vaadin-button[text()='Upload as Zip']")
    private WebElement uploadAsZipBtn;

    private final By byInput = By.cssSelector("input");


    public void uploadModelFile(String model) {
        waitABit(2500);
        setImplicitTimeout(3, SECONDS);
        if (getDriver().findElements(By.xpath("//vaadin-button[text()='Upload as Zip']")).size() != 0) {
            uploadAsZipBtn.click();
        }
        resetImplicitTimeout();
        waitABit(2500);
        WebElement e = utils.expandRootElement(uploadShadow);
        WebElement projectNameInputField = e.findElement(byInput);
        upload(System.getProperty("user.dir") + "/models/" + model).fromLocalMachine().to(projectNameInputField);
    }

    public void checkThatModelUploadPageOpened() {
        waitABit(2500);
        assertThat(getDriver().getCurrentUrl(), containsString("uploadModel"));
        assertThat(getDriver().findElement(By.xpath("//span[@class='no-top-margin-label']")).getText(), is("Upload Model"));
    }

    public void checkErrorMessageInModelCheckPanel(String errorMessage) {
        setImplicitTimeout(240, SECONDS);
        waitFor(ExpectedConditions.visibilityOfElementLocated(By.xpath("//vaadin-progress-bar[@theme='error']/following-sibling::span")));
        By xpath = By.xpath("//vaadin-progress-bar[@theme='error']/following-sibling::span");
        assertThat(getDriver().findElement(xpath).getText(), is(errorMessage));
        resetImplicitTimeout();
    }

    public void checkErrorMessageStartsWithInModelCheckPanel(String errorMessage) {
        setImplicitTimeout(240, SECONDS);
        waitFor(ExpectedConditions.visibilityOfElementLocated(By.xpath("//vaadin-progress-bar[@theme='error']/following-sibling::span")));
        By xpath = By.xpath("//vaadin-progress-bar[@theme='error']/following-sibling::span");
        assertThat(getDriver().findElement(xpath).getText(), startsWith(errorMessage));
        resetImplicitTimeout();
    }

    public void clickAlpUploadStepNextBtn() {
        getDriver().findElement(By.xpath("//span[text()='Upload alp file']/parent::vaadin-horizontal-layout/following-sibling::vaadin-horizontal-layout/vaadin-button")).click();
    }

    public void uploadALPFile(String alpFile) {
        WebElement e = utils.expandRootElement(getDriver().findElement(By.xpath("//span[text()='Upload alp file']/parent::vaadin-horizontal-layout/parent::vaadin-vertical-layout/descendant::vaadin-upload")));
        WebElement projectNameInputField = e.findElement(byInput);
        upload(System.getProperty("user.dir") + "/models/" + alpFile).fromLocalMachine().to(projectNameInputField);
        waitABit(3000);
    }

    public void checkThatWizardUploadAlpFilePageIsOpened() {
        waitFor(ExpectedConditions.visibilityOf(getDriver().findElement(By.xpath("//span[text()='Upload alp file']"))));
        WebElement instructionsElement = utils.expandRootElement(uploadAlpInstructionsShadow);
        assertThat(getDriver().findElement(By.xpath("//*[@class='section-title-label']")).getText(), is("Project:"));
        assertThat(getDriver().findElement(By.xpath("//*[@class='section-title-label-regular-font-weight section-subtitle-label']")).getText(), is("AutotestProject" + Serenity.sessionVariableCalled("randomNumber")));
        assertThat(getDriver().findElements(By.xpath("//vaadin-horizontal-layout/*[@class='no-top-margin-label']")).get(1).getText(), is("Upload alp file"));
        assertThat(getDriver().findElement(By.xpath("//tag-label")).getText(), is("Optional"));
        assertThat(instructionsElement.findElement(By.cssSelector("p:nth-child(1)")).getText(), is("Upload your model's ALP file to keep track of its version used for running experiments."));
        assertThat(instructionsElement.findElement(By.cssSelector("p:nth-child(2)")).getText(), is("Your ALP file should be in the original AnyLogic Project folder on your computer."));
        assertThat(instructionsElement.findElement(By.cssSelector("p:nth-child(3)")).getText(), is("You will be able to download this ALP file later to retrieve it."));
        assertThat(getDriver().findElement(By.xpath("//upload-alp-instructions/following-sibling::vaadin-vertical-layout//vaadin-button[@slot='add-button']")).getText(), is("Upload alp file"));
    }

    public void checkThatModelUploadLinkOpened() {
        waitABit(4000);
        assertThat(getDriver().findElement(By.cssSelector("h1")).getText(), is("Exporting models to Java application"));
    }

    public void checkWizardWarningLabelIsShown(String warningLabel, Boolean isShown) {
        if (isShown) {
            waitFor(ExpectedConditions.visibilityOf(warningLabelElement));
            assertThat(warningLabelElement.getText(), is(warningLabel));
        } else {
            setImplicitTimeout(3, SECONDS);
            assertThat(getDriver().findElements(By.xpath("//span[@class='warning-label']")).size(), is(0));
            resetImplicitTimeout();
        }
    }

    public void wizardModelUploadCheckFolderUploadPage() {
        assertThat(getDriver().findElement(By.xpath("//div[@class='project-title-label']")).getText(), is("Project: AutotestProject" + Serenity.sessionVariableCalled("randomNumber")));
        assertThat(getDriver().findElement(By.xpath("//div[@class='project-title-label']/parent::vaadin-horizontal-layout/following-sibling::vaadin-vertical-layout/vaadin-horizontal-layout/span")).getText(), is("Upload Model"));
        assertThat(getDriver().findElement(By.xpath("//upload-model-instructions")).getText(), is("Export your model as a standalone Java application.\nUpload the exported folder."));
        waitFor(ExpectedConditions.visibilityOfElementLocated(By.xpath("//vaadin-button[@slot='add-button']")));
        assertThat(getDriver().findElement(By.xpath("//vaadin-button[@slot='add-button']")).getText(), is("Upload exported folder"));
        waitFor(ExpectedConditions.visibilityOfElementLocated(By.xpath("//vaadin-button[@theme='tertiary']")));
        assertThat(getDriver().findElement(By.xpath("//vaadin-button[@theme='tertiary']")).getText(), is("Upload as Zip"));
    }

    public void wizardModelUploadCheckArchiveUploadPage() {
        assertThat(getDriver().findElement(By.xpath("//div[@class='project-title-label']")).getText(), is("Project: AutotestProject" + Serenity.sessionVariableCalled("randomNumber")));
        assertThat(getDriver().findElement(By.xpath("//div[@class='project-title-label']/parent::vaadin-horizontal-layout/following-sibling::vaadin-vertical-layout/vaadin-horizontal-layout/span")).getText(), is("Upload Model"));
        assertThat(getDriver().findElement(By.xpath("//upload-model-instructions")).getText(), is("Export your model as a standalone Java application.\n*Using the exported folder, Create a zip file that contains:\nmodel.jar\nthe \"database\" and \"cache\" folder if they exist\nany excel sheets necessary for your AnyLogic simulation\nUpload the new zip file below.\n*Note: If your AnyLogic simulation is composed of multiple .alp files, please upload the exported folder instead."));
        waitFor(ExpectedConditions.visibilityOfElementLocated(By.xpath("//vaadin-button[@slot='add-button']")));
        assertThat(getDriver().findElement(By.xpath("//vaadin-button[@slot='add-button']")).getText(), is("Upload zip file"));
        waitFor(ExpectedConditions.visibilityOfElementLocated(By.xpath("//vaadin-button[@theme='tertiary']")));
        assertThat(getDriver().findElement(By.xpath("//vaadin-button[@theme='tertiary']")).getText(), is("Upload Folder"));
    }
}
