package io.skymind.pathmind.bddtests.page.wizard;

import io.skymind.pathmind.bddtests.Utils;
import net.serenitybdd.core.pages.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

public class ModelUploadPage extends PageObject {

    private Utils utils;

    @FindBy(xpath = "//vaadin-upload")
    private WebElement uploadShadow;

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
        assertThat(getDriver().getCurrentUrl(), containsString("uploadModel"));
        assertThat(getDriver().findElement(By.xpath("//span[@class='no-top-margin-label']")).getText(), is("Upload Model"));
    }

    public void checkErrorMessageInModelCheckPanel(String errorMessage) {
        setImplicitTimeout(90, SECONDS);
        waitFor(ExpectedConditions.visibilityOfElementLocated(By.xpath("//vaadin-vertical-layout[normalize-space(text())='" + errorMessage + "']")));
        By xpath = By.xpath("//vaadin-progress-bar/parent::*");
        assertThat(getDriver().findElement(xpath).getText(), is(errorMessage));
        resetImplicitTimeout();
    }
}
