package io.skymind.pathmind.bddtests.page;

import io.skymind.pathmind.bddtests.Utils;
import net.serenitybdd.core.pages.PageObject;
import net.thucydides.core.annotations.DefaultUrl;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

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
        WebElement experimentNavBarItemShadow = utils.expandRootElement(getDriver().findElement(By.xpath("//experiment-navbar-item[@is-current]")));
        WebElement archiveButton = experimentNavBarItemShadow.findElement(By.cssSelector("vaadin-button"));
        waitFor(ExpectedConditions.elementToBeClickable(archiveButton));
        archiveButton.click();
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
        waitABit(3500);
        WebElement inputShadow = utils.expandRootElement(getDriver().findElement(By.xpath("//*[@class='reward-variables-table']/descendant::span[text()='" + variableNumber + "']/following-sibling::vaadin-text-field")));
        inputShadow.findElement(By.cssSelector("input")).click();
        inputShadow.findElement(By.cssSelector("input")).sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        inputShadow.findElement(By.cssSelector("input")).sendKeys(Keys.ENTER);
        waitABit(3500);
        inputShadow.findElement(By.cssSelector("input")).sendKeys(variableName);
        inputShadow.findElement(By.cssSelector("input")).sendKeys(Keys.ENTER);
    }

    public void clickSideNavArchiveButtonFor(String experimentName) {
        utils.getExperimentNavbarItemByExperimentName(experimentName, "vaadin-button").click();
    }

    public void checkExperimentPageRewardVariablesIs(String commaSeparatedVariableNames) {
        List<String> items = Arrays.asList(commaSeparatedVariableNames.split("\\s*,\\s*"));
        List<String> actual = new ArrayList<>();
        for (WebElement webElement : getDriver().findElements(By.xpath("//vaadin-text-field"))) {
            actual.add(webElement.getAttribute("value"));
        }

        assertThat(actual, containsInRelativeOrder(items.toArray()));
    }

    public void checkRunningExperimentPageRewardVariablesIs(String commaSeparatedVariableNames) {
        List<String> items = Arrays.asList(commaSeparatedVariableNames.split("\\s*,\\s*"));
        List<String> actual = new ArrayList<>();
        for (WebElement webElement : getDriver().findElements(By.xpath("//*[@class='reward-variables-table compact']/descendant::vaadin-horizontal-layout/span[not(text()='Variable Name')][2]"))) {
            actual.add(webElement.getText());
        }

        assertThat(actual, containsInRelativeOrder(items.toArray()));
    }

    public void checkExperimentPageSimulationMetrics(String commaSeparatedVariableNames) {
        List<String> items = Arrays.asList(commaSeparatedVariableNames.split("\\s*,\\s*"));
        List<String> actual = new ArrayList<>();
        for (WebElement webElement : getDriver().findElements(By.xpath("//span[contains(@class,'variable')]"))) {
            actual.add(webElement.getText());
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
        assertThat(utils.getStringListFromShadowRootRepeatIfStaleException(By.xpath("//experiment-navbar-item"), By.cssSelector(".experiment-name p:first-child")), hasItem(experiment));
    }

    public void clickCopyRewardFunctionBtn() {
        WebElement e = utils.expandRootElement(rewardFunction);
        e.findElement(By.cssSelector("vaadin-button")).click();
        experimentNotes.click();
        experimentNotes.sendKeys(Keys.CONTROL + "V");
        getDriver().findElement(By.xpath("//*[text()='Save']")).click();
    }

    public void checkThatExperimentNotExistOnTheExperimentPage(String experiment) {
        waitABit(4000);
        assertThat(utils.getStringListFromShadowRootRepeatIfStaleException(By.xpath("//experiment-navbar-item"), By.cssSelector(".experiment-name p:first-child")), not(hasItem(experiment)));
    }

    public void checkThatExperimentStatusIconIs(String experimentName, String icon) {
        waitABit(10000);
        WebElement statusIconShadow = utils.expandRootElement(utils.getExperimentNavbarItemByExperimentName(experimentName, "status-icon"));
        assertThat(statusIconShadow.findElements(By.cssSelector(icon)).size(), is(1));
    }

    public void clickExperimentPageStarButton(String experimentName) {
        waitABit(3500);
        WebElement favoriteStarShadow = utils.expandRootElement(utils.getExperimentNavbarItemByExperimentName(experimentName, "favorite-star"));
        waitFor(ExpectedConditions.elementToBeClickable(favoriteStarShadow.findElement(By.cssSelector("vaadin-button"))));
        favoriteStarShadow.findElement(By.cssSelector("vaadin-button")).click();
    }

    public void checkExperimentPageSideBarIsFavorite(String experimentName, Boolean favoriteStatus) {
        waitABit(3500);
        WebElement favoriteStarShadow = utils.expandRootElement(utils.getExperimentNavbarItemByExperimentName(experimentName, "favorite-star"));
        waitFor(ExpectedConditions.elementToBeClickable(favoriteStarShadow.findElement(By.cssSelector("vaadin-button"))));
        if (favoriteStatus){
            assertThat(favoriteStarShadow.findElement(By.cssSelector("iron-icon")).getAttribute("icon"), is("vaadin:star"));
        }else {
            assertThat(favoriteStarShadow.findElement(By.cssSelector("iron-icon")).getAttribute("icon"), is("vaadin:star-o"));
        }
    }

    public void checkThatExperimentPageIsOpened() {
        assertThat(getDriver().getTitle(), either(is("Pathmind | Experiment")).or(is("Pathmind | New Experiment")));
        assertThat(getDriver().getCurrentUrl(), either(containsString("/newExperiment/")).or(containsString("/experiment/")));
    }

    public void checkSideBarExperimentsListExperiment(String commaSeparatedExperimentNames) {
        List<String> items = Arrays.asList(commaSeparatedExperimentNames.split("\\s*,\\s*"));
        List<String> actual = new ArrayList<>();
        for (WebElement webElement : getDriver().findElements(By.xpath("//experiment-navbar-item"))) {
            WebElement experimentNavbarItemShadow = utils.expandRootElement(webElement);
            String experimentNameText = experimentNavbarItemShadow.findElement(By.cssSelector(".experiment-name p:first-child")).getText();
            actual.add(experimentNameText);
        }

        assertThat(actual, containsInAnyOrder(items.toArray()));
    }

    public void checkThatExperimentPageArchivedTagIsShown() {
        assertThat(getDriver().findElement(By.xpath("//span[@class='section-title-label']/following-sibling::tag-label")).getText(), is("Archived"));
    }

    public void checkSimulationMetricsColumnsTitles() {
        setImplicitTimeout(3, SECONDS);
        for (int i = 0; i < 4; i++) {
            if (getDriver().findElements(By.xpath("//*[@class='metrics-wrapper']/span")).size() != 0) {
                break;
            } else {
                waitABit(60000);
                getDriver().navigate().refresh();
            }
        }
        resetImplicitTimeout();
        assertThat(getDriver().findElement(By.xpath("//*[@class='header-row']/span[2]")).getText(), is("Variable Name"));
        assertThat(getDriver().findElement(By.xpath("//*[@class='metrics-wrapper']/div/span")).getText(), is("Value"));
        assertThat(getDriver().findElement(By.xpath("//*[@class='metrics-wrapper']/div/a")).getAttribute("href"), is("https://help.pathmind.com/en/articles/4305404-simulation-metrics"));
        assertThat(getDriver().findElement(By.xpath("//*[@class='sparklines-wrapper']/div/span")).getText(), is("Overview"));
        assertThat(getDriver().findElement(By.xpath("//*[@class='sparklines-wrapper']/div/a")).getAttribute("href"), is("https://help.pathmind.com/en/articles/4305404-simulation-metrics"));
    }

    public void clickSimulationMetricsValueIcon() {
        getDriver().findElement(By.xpath("//*[@class='metrics-wrapper']/div/a")).click();
    }

    public void clickSimulationMetricsOverviewIcon() {
        getDriver().findElement(By.xpath("//*[@class='sparklines-wrapper']/div/a")).click();
    }
}
