package io.skymind.pathmind.bddtests.page;

import io.skymind.pathmind.bddtests.Utils;
import net.serenitybdd.core.pages.PageObject;
import net.thucydides.core.annotations.DefaultUrl;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DefaultUrl("page:home.page")
public class ExperimentPage extends PageObject {

    private Utils utils;

    @FindBy(xpath = "//code-viewer")
    private WebElement rewardFunction;
    @FindBy(xpath = "//*[text()='Notes']/ancestor::*[@class='notes-field-wrapper']/descendant::vaadin-text-area")
    private WebElement experimentNotes;
    @FindBy(xpath = "//span[text()='Status']/following-sibling::span[1]")
    private WebElement experimentStatus;

    public void checkExperimentPageRewardFunction(String rewardFnFile) throws IOException {
        assertThat(rewardFunction.getText(), is(FileUtils.readFileToString(new File("models/" + rewardFnFile), StandardCharsets.UTF_8)));
    }

    public void addNoteToTheExperimentPage(String note) {
        experimentNotes.click();
        experimentNotes.sendKeys(note);
    }

    public void clickCurrentExperimentArchiveButton() {
        String xpath = String.format("//*[@class='experiment-navbar-item current']/vaadin-button");
        getDriver().findElement(By.xpath(xpath)).click();
    }

    public void checkExperimentNotesIs(String note) {
        assertThat(experimentNotes.getAttribute("value"), is(note.replaceAll("/n", "\n")));
    }

    public void checkExperimentStatusCompletedWithLimitHours(int limit) {
        System.out.println("!Waiting for training completed with limit " + limit + " hours!");
        for (int i = 0; i < limit * 60; i++) {
            String status = getDriver().findElement(By.xpath("//span[text()='Status']/following-sibling::span[1]")).getText();
            if (getDriver().findElements(By.xpath("//span[text()='Completed']")).size() != 0 || status.equals("Error") || status.equals("Stopped")) {
                break;
            } else {
                waitABit(60000);
                getDriver().navigate().refresh();
            }
        }
        assertThat(experimentStatus.getText(), containsString("Completed"));
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
        switch (status) {
            case "Stopping":
                assertThat(getDriver().findElement(By.xpath(trainingStatus)).getText(), either(is(status)).or(is("Stopped")));
                break;
            case "Starting Cluster":
                assertThat(getDriver().findElement(By.xpath(trainingStatus)).getText(), either(is(status)).or(is("Running")));
                break;
            default:
                assertThat(getDriver().findElement(By.xpath(trainingStatus)).getText(), is(status));
                break;
        }
        resetImplicitTimeout();
    }

    public void changeRewardVariableOnExperimentView(String variableNumber, String variableName) {
        WebElement inputShadow = utils.expandRootElement(getDriver().findElement(By.xpath("//*[@class='reward-variables-table']/descendant::span[text()='" + variableNumber + "']/following-sibling::vaadin-text-field")));
        inputShadow.findElement(By.cssSelector("input")).click();
        inputShadow.findElement(By.cssSelector("input")).clear();
        inputShadow.findElement(By.cssSelector("input")).sendKeys(variableName);
    }

    public void clickSideNavArchiveButtonFor(String experimentName) {
        getDriver().findElement(By.xpath("//p[text()='" + experimentName + "']/ancestor::vaadin-horizontal-layout[contains(@class,'experiment-navbar-item')]/vaadin-button")).click();
    }

    public void checkExperimentPageRewardVariablesIs(String commaSeparatedVariableNames) {
        List<String> items = Arrays.asList(commaSeparatedVariableNames.split("\\s*,\\s*"));
        List<String> actual = new ArrayList<>();
        for (WebElement webElement : getDriver().findElements(By.xpath("//vaadin-text-field"))) {
            actual.add(webElement.getAttribute("value"));
        }

        assertThat(actual, containsInRelativeOrder(items.toArray()));
    }

    public void checkThatMetricsAreShownForRewardVariables(int metricsNumber) {
        List<String> actual = new ArrayList<>();
        for (WebElement webElement : getDriver().findElements(By.xpath("//*[@class='metrics-wrapper']/span"))) {
            actual.add(webElement.getText());
        }

        assertThat(actual, hasSize(metricsNumber));
    }

    public void checkThatSparklinesAreShownForRewardVariables(int sparklinesNumber) {
        List<String> actual = new ArrayList<>();
        for (WebElement webElement : getDriver().findElements(By.xpath("//*[@class='sparklines-wrapper']/spark-line"))) {
            actual.add(webElement.getText());
        }

        assertThat(actual, hasSize(sparklinesNumber));
    }

    public void checkThatSimulationMetricsBlockIsShown() {
        assertThat(getDriver().findElement(By.xpath("//span[text()='Simulation Metrics']/parent::vaadin-vertical-layout")).isDisplayed(), is(true));
    }

    public void checkThatExperimentExistOnTheExperimentPage(String experiment) {
        waitABit(4000);
        assertThat(utils.getStringListRepeatIfStaleException(By.xpath("//*[@class='experiment-name']/p[1]")), hasItem(experiment));
    }

    public void clickCopyRewardFunctionBtn() {
        WebElement e = utils.expandRootElement(rewardFunction);
        e.findElement(By.cssSelector("vaadin-button")).click();
        experimentNotes.click();
        experimentNotes.sendKeys(Keys.CONTROL + "V");
        getDriver().findElement(By.xpath("//*[text()='Save']")).click();
    }
}
