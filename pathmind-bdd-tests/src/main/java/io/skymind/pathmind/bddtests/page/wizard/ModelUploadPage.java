package io.skymind.pathmind.bddtests.page.wizard;

import io.skymind.pathmind.bddtests.Utils;
import net.serenitybdd.core.pages.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ModelUploadPage extends PageObject {

    private Utils utils;

    @FindBy(xpath = "//vaadin-upload")
    private WebElement uploadShadow;
    @FindBy(xpath = "//upload-alp-instructions")
    private WebElement uploadAlpInstructionsShadow;

    private final By byInput = By.cssSelector("input");


    public void uploadModelFile(String model) {
        waitABit(2500);
        getDriver().findElement(By.xpath("//vaadin-button[text()='Upload as Zip']")).click();
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
        assertThat(instructionsElement.findElement(By.cssSelector("p:nth-child(1)")).getText(), is("Upload your model's ALP file to keep track of its version used for running experiments."));
        assertThat(instructionsElement.findElement(By.cssSelector("p:nth-child(2)")).getText(), is("Your ALP file should be in the original AnyLogic Project folder on your computer."));
        assertThat(instructionsElement.findElement(By.cssSelector("p:nth-child(3)")).getText(), is("You will be able to download this ALP file later to retrieve it."));
    }

    public void checkThatModelUploadLinkOpened() {
        getDriver().switchTo().frame(1);
        getDriver().switchTo().frame(1);
        getDriver().switchTo().frame(1);
        assertThat(getDriver().findElement(By.cssSelector("body > h1:nth-child(2)")).getText(), is("Exporting models to Java application"));
    }
}
