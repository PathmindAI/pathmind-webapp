package io.skymind.pathmind.bddtests.page.wizard;

import io.skymind.pathmind.bddtests.Utils;
import net.serenitybdd.core.pages.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

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
        getDriver().findElement(By.xpath("//span[text()='Model Details']/parent::vaadin-horizontal-layout/parent::vaadin-vertical-layout/descendant::vaadin-button")).click();
    }

    public void checkThatModelDetailsPageIsOpened() {
        setImplicitTimeout(5, SECONDS);
        String xpath = String.format("//*[text()='%s']", "Your model was successfully uploaded!");
        waitFor(ExpectedConditions.invisibilityOfAllElements(getDriver().findElements(By.xpath(xpath))));
        assertThat(getDriver().findElement(By.xpath("//span[@class='bold-label']/following-sibling::span")).getText(), is("Add any notes for yourself about the model you're uploading."));
        resetImplicitTimeout();
    }

    public void checkThatModelSuccessfullyUploaded() {
        setImplicitTimeout(1, SECONDS);
        int attempts = 0;
        while (attempts < 300) {
            if (getDriver().findElements(By.xpath("//*[text()='Your model was successfully uploaded!']")).size() != 0 || getDriver().findElements(By.xpath("//span[text()='Unable to analyze the model.']")).size() != 0) {
                break;
            }
            attempts++;
        }
        assertThat(getDriver().findElements(By.xpath("//span[text()='Unable to analyze the model.']")).size(), is(0));

        waitFor(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[text()='Your model was successfully uploaded!']")));
        resetImplicitTimeout();
    }

    public void checkObservationsListContains(String experiment, String variableName) {
        assertThat(getDriver().findElement(By.xpath("//vaadin-grid-cell-content[text()='" + experiment + " ']/following-sibling::vaadin-grid-cell-content[3]")).getText(), containsString(variableName));
    }

    public void clickModelsPageMetricsDropdown() {
        getDriver().findElement(By.xpath("//span[text()='Metrics']/following-sibling::multiselect-combo-box")).click();
    }

    public void checkMetrics(String commaSeparatedMetrics) {
        WebElement comboBox = utils.expandRootElement(getDriver().findElement(By.xpath("//vaadin-combo-box-overlay")));
        WebElement content = utils.expandRootElement(comboBox.findElement(By.cssSelector("#content")));
        List<String> actualMetrics = new ArrayList<>();
        for (WebElement element : content.findElements(By.cssSelector("vaadin-combo-box-item"))) {
            WebElement item = utils.expandRootElement(element);
            actualMetrics.add(item.findElement(By.cssSelector("div")).getText());
        }
        assertThat(actualMetrics, containsInAnyOrder(Arrays.asList(commaSeparatedMetrics.split(",")).toArray()));
    }

    public void modelPageChooseMetricFromDropdown(String metric) {
        WebElement comboBox = utils.expandRootElement(getDriver().findElement(By.xpath("//vaadin-combo-box-overlay")));
        WebElement content = utils.expandRootElement(comboBox.findElement(By.cssSelector("#content")));
        for (WebElement element : content.findElements(By.cssSelector("vaadin-combo-box-item"))) {
            WebElement item = utils.expandRootElement(element);
            if (item.findElement(By.cssSelector("div")).getText().equals(metric)) {
                element.click();
            }
        }
    }

    public void modelPageCheckExperimentColumnValueIs(String experiment, String column, String value) {
        assertThat(getDriver().findElement(By.cssSelector("#pathmind-app-layout > vaadin-vertical-layout > div > vaadin-horizontal-layout:nth-child(2) > vaadin-vertical-layout.model-wrapper > vaadin-grid > vaadin-grid-cell-content:nth-child(50)")).getText(), is(value));
    }

    public void checkModelPageColumnsMultiselect(String columns) {
        assertThat(getDriver().findElement(By.xpath("//*[@class='column-selection-row']/multiselect-combo-box")).getAttribute("title"), is(columns));
    }

    public void modelPageDisableFavoriteColumn(String column) {
        WebElement multiSelect = utils.expandRootElement(getDriver().findElement(By.xpath("//*[@class='column-selection-row']/multiselect-combo-box")));
        WebElement multiSelectInput = utils.expandRootElement(multiSelect.findElement(By.cssSelector("multiselect-combo-box-input")));
        for (WebElement element : multiSelectInput.findElements(By.cssSelector("div[part='token-remove-button']"))) {
            if (element.getText().equals(column)) {
                waitABit(3000);
                JavascriptExecutor executor = (JavascriptExecutor) getDriver();
                executor.executeScript("arguments[0].click();", element);
                waitABit(3000);
            }
        }
    }

    public void clickModelsPageColumnsDropdown() {
        getDriver().findElement(By.xpath("//span[text()='Columns']/following-sibling::multiselect-combo-box")).click();
    }
}
