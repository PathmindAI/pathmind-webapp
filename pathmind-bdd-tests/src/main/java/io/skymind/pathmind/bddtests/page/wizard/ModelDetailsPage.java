package io.skymind.pathmind.bddtests.page.wizard;

import io.skymind.pathmind.bddtests.Utils;
import net.serenitybdd.core.pages.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ModelDetailsPage extends PageObject {

    private Utils utils;

    @FindBy(xpath = "//span[text()='Notes']/following-sibling::vaadin-text-area")
    private WebElement modelDetailsNotesShadow;

    private final By byTextarea = By.cssSelector("textarea");

    public void inputModelDetailsNotes(String notes) {
        WebElement e = utils.expandRootElement(modelDetailsNotesShadow);
        WebElement notestTextArea = e.findElement(byTextarea);
        notestTextArea.click();
        notestTextArea.clear();
        notestTextArea.sendKeys(notes);
    }

    public void clickWizardModelDetailsNextBtn() {
        getDriver().findElement(By.xpath("//span[text()='Model Details']/ancestor::*[@class='view-section']/descendant::vaadin-button[normalize-space(text())='Next']")).click();
    }

    public void checkThatModelDetailsPageIsOpened() {
        setImplicitTimeout(5, SECONDS);
        String xpath = String.format("//*[text()='%s']", "Your model was successfully uploaded!");
        waitFor(ExpectedConditions.invisibilityOfAllElements(getDriver().findElements(By.xpath(xpath))));
        assertThat(getDriver().findElement(By.xpath("//span[@class='bold-label']/following-sibling::span")).getText(), is("Add any notes for yourself about the model you're uploading."));
        resetImplicitTimeout();
    }

    public void checkThatModelSuccessfullyUploaded() {
        setImplicitTimeout(120, SECONDS);
        waitFor(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[text()='Your model was successfully uploaded!']")));
        resetImplicitTimeout();
    }
}
