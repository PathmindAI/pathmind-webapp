package io.skymind.pathmind.bddtests.page;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.skymind.pathmind.bddtests.Utils;
import net.serenitybdd.core.pages.PageObject;
import net.thucydides.core.annotations.DefaultUrl;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsInRelativeOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.either;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@DefaultUrl("page:home.page")
public class ExperimentPage extends PageObject {

    private Utils utils;

    @FindBy(xpath = "//code-viewer")
    private WebElement rewardFunction;
    @FindBy(xpath = "//*[text()='Notes']/ancestor::*[@class='notes-field-wrapper']/descendant::vaadin-text-area")
    private WebElement experimentNotes;
    @FindBy(xpath = "//span[text()='Status']/following-sibling::span[1]")
    private WebElement experimentStatus;
    @FindBy(xpath = "//notes-field")
    private WebElement notesBlock;
    private By notesTextarea = By.cssSelector("#textarea");
    private By notesSaveBtn = By.cssSelector("#save");

    public void checkExperimentPageRewardFunction(String rewardFnFile) throws IOException {
        assertThat(rewardFunction.getText(), is(FileUtils.readFileToString(new File("models/" + rewardFnFile), StandardCharsets.UTF_8)));
    }

    public void addNoteToTheExperimentPage(String note) {
        WebElement notesShadow = utils.expandRootElement(notesBlock);
        notesShadow.findElement(notesTextarea).click();
        notesShadow.findElement(notesTextarea).sendKeys(note);
    }

    public void clickCurrentExperimentArchiveButton() {
        WebElement experimentNavBarItemShadow = utils.expandRootElement(getDriver().findElement(By.xpath("//experiment-navbar-item[@is-current]")));
        WebElement archiveButton = experimentNavBarItemShadow.findElement(By.cssSelector("vaadin-button"));
        waitFor(ExpectedConditions.elementToBeClickable(archiveButton));
        WebElement button = utils.expandRootElement(archiveButton);
        button.findElement(By.cssSelector("button")).click();
    }

    public void checkExperimentNotesIs(String note) {
        WebElement notesShadow = utils.expandRootElement(notesBlock);
        assertThat(notesShadow.findElement(notesTextarea).getAttribute("value"), is(note.replaceAll("/n", "\n")));
    }

    public void checkExperimentStatusCompletedWithLimitMinutes(int limit) {
        System.out.println("!Waiting for training completed with limit " + limit + " minutes!");
        for (int i = 0; i < limit; i++) {
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
        waitABit(3500);
        WebElement element = utils.expandRootElement(utils.getExperimentNavbarItemByExperimentName(experimentName, "vaadin-button"));
        element.findElement(By.cssSelector("button")).click();
    }

    public void checkExperimentPageRewardVariablesIs(String commaSeparatedVariableNames) {
        List<String> items = Arrays.asList(commaSeparatedVariableNames.split("\\s*,\\s*"));
        List<String> actual = new ArrayList<>();
        for (WebElement webElement : getDriver().findElements(By.xpath("//*[@class='reward-variables-table']/descendant::*[@class='reward-variable-name']"))) {
            actual.add(webElement.getText());
        }

        assertThat(actual, containsInRelativeOrder(items.toArray()));
    }

    public void checkRunningExperimentPageRewardVariablesIs(String commaSeparatedVariableNames) {
        List<String> items = Arrays.asList(commaSeparatedVariableNames.split("\\s*,\\s*"));
        List<String> actual = new ArrayList<>();
        for (WebElement webElement : getDriver().findElements(By.xpath("//*[@class='reward-variable-name']"))) {
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
        for (WebElement webElement : getDriver().findElements(By.xpath("//*[@class='sparkline']/data-chart"))) {
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
        WebElement notesShadow = utils.expandRootElement(notesBlock);
        notesShadow.findElement(notesTextarea).click();
        notesShadow.findElement(notesTextarea).sendKeys(Keys.CONTROL + "V");
        notesShadow.findElement(notesSaveBtn).click();
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
        if (favoriteStatus) {
            assertThat(favoriteStarShadow.findElement(By.cssSelector("iron-icon")).getAttribute("icon"), is("vaadin:star"));
        } else {
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
        assertThat(getDriver().findElement(By.xpath("//*[@class='header-row']/span[1]")).getText(), is("Variable Name"));
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

    public void clickExperimentPageShowSparklineBtnForVariable(String variable) {
        WebElement showBtn = getDriver().findElement(By.xpath("//span[contains(@class,'reward-variable-name') and text()='" + variable + "']/ancestor::vaadin-horizontal-layout[@class='simulation-metrics-table-wrapper']/descendant::vaadin-vertical-layout[@class='sparkline']"));
        Actions actions = new Actions(getDriver());
        actions.moveToElement(showBtn);
        actions.perform();
        getDriver().findElement(By.xpath("//span[contains(@class,'reward-variable-name') and text()='" + variable + "']/ancestor::vaadin-horizontal-layout[@class='simulation-metrics-table-wrapper']/descendant::vaadin-vertical-layout[@class='sparkline']/descendant::vaadin-button")).click();
    }

    public void checkExperimentPageChartPopUpIsShownForVariable(String variable) {
        assertThat(getDriver().findElements(By.xpath("//vaadin-dialog-overlay[@id='overlay']")).size(), is(not(0)));
        assertThat(getDriver().findElement(By.xpath("//vaadin-dialog-overlay[@id='overlay']/descendant::span[@class='bold-label']")).getText(), is(variable));
        assertThat(getDriver().findElements(By.xpath("//vaadin-dialog-overlay[@id='overlay']/descendant::data-chart")).size(), is(not(0)));
    }

    public void checkVariableSimulationMetricValue(String variable, String value) {
        assertThat(getDriver().findElement(By.xpath("//span[text()='" + variable + "']/ancestor::*[@class='simulation-metrics-table-wrapper']/descendant::*[@class='metrics-wrapper']/span")).getText(), is(value));
    }

    public void checkExperimentPageObservationsList(String observation) {
        List<String> items = Arrays.asList(observation.split("\\s*,\\s*"));
        List<String> actual = new ArrayList<>();
        for (WebElement webElement : getDriver().findElements(By.xpath("//*[@class='observations-panel']/descendant::vaadin-checkbox[not(@hidden)]"))) {
            actual.add(webElement.getText());
        }
        assertThat(actual, containsInAnyOrder(items.toArray()));
    }

    public void checkExportPolicyPage(String model) {
        waitFor(ExpectedConditions.visibilityOf(getDriver().findElement(By.xpath("//*[@class='view-section']/img"))));
        assertThat(getDriver().findElement(By.cssSelector(".section-title-label")).getText(), is("Export Policy"));
        assertThat(getDriver().findElement(By.xpath("//*[@class='view-section']/img")).getAttribute("src"), containsString("/frontend/images/exportPolicyIcon.gif"));
        assertThat(getDriver().findElement(By.xpath("//*[@class='section-title-label']/following-sibling::span")).getText(), containsString(model));
        assertThat(getDriver().findElement(By.xpath("//*[@class='section-title-label']/following-sibling::div/h3")).getText(), is("To use your policy:"));
        assertThat(getDriver().findElement(By.xpath("//*[@class='section-title-label']/following-sibling::div/ol")).getText(), is("Download this file.\n" +
                "Return to AnyLogic and open the Pathmind Helper properties in your simulation.\n" +
                "Change the 'Mode' to 'Use Policy'.\n" +
                "In 'policyFile', click 'Browse' and select the file you downloaded.\n" +
                "Run the simulation to see the policy in action."));
        assertThat(getDriver().findElement(By.xpath("//*[@class='section-title-label']/following-sibling::a[1]")).getText(), is("Learn how to validate your policy"));
        assertThat(getDriver().findElement(By.xpath("//*[@class='section-title-label']/following-sibling::a[1]")).getAttribute("href"), is("https://help.pathmind.com/en/articles/3655157-9-validate-trained-policy"));
        waitFor(ExpectedConditions.visibilityOf(getDriver().findElement(By.xpath("//*[@class='section-title-label']/following-sibling::a[2]"))));
        assertThat(getDriver().findElement(By.xpath("//*[@class='section-title-label']/following-sibling::a[2]")).getAttribute("href"), containsString(model));
        assertThat(getDriver().findElement(By.xpath("//*[@class='section-title-label']/following-sibling::a[2]/vaadin-button")).getText(), is("Export Policy"));
        waitFor(ExpectedConditions.visibilityOf(getDriver().findElement(By.cssSelector(".download-alp-link"))));
        assertThat(getDriver().findElement(By.xpath("//*[@class='section-title-label']/following-sibling::a[3]")).getAttribute("href"), containsString(model));
        assertThat(getDriver().findElement(By.xpath("//*[@class='section-title-label']/following-sibling::a[3]/vaadin-button")).getText(), is("Model ALP"));
    }

    public void checkLearningProgressTitle(String title) {
        assertThat(getDriver().findElement(By.xpath("//vaadin-vertical-layout[@class='row-2-of-3']/span")).getText(), is(title));
    }

    public void checkLearningProgressBlockSelectedTabNameIs(String selected, String tab) {
        assertThat(getDriver().findElement(By.xpath("//vaadin-vertical-layout[@class='row-2-of-3']/descendant::vaadin-tab[@aria-selected='" + selected + "']")).getText(), is(tab));
    }

    public void checkLearningProgressBlockMetricsHint(String hint) {
        waitFor(ExpectedConditions.visibilityOfElementLocated(By.xpath("//vaadin-vertical-layout[@class='row-2-of-3']/descendant::iron-icon")));
        assertThat(getDriver().findElement(By.xpath("//vaadin-vertical-layout[@class='row-2-of-3']/descendant::p")).getText(), is(hint));
    }

    public void checkLearningProgressBlockDataChartIsShown() {
        waitFor(ExpectedConditions.visibilityOfElementLocated(By.xpath("//vaadin-vertical-layout[@class='row-2-of-3']/descendant::data-chart[1]")));
    }

    public void checkLearningProgressBlockMeanRewardScoreDataChartIsShown() {
        waitFor(ExpectedConditions.visibilityOfElementLocated(By.xpath("//vaadin-vertical-layout[@class='row-2-of-3']/descendant::data-chart[2]")));
    }

    public void checkVariableGoalReachedIsChosenTrue(String variable, Boolean chosen) {
        if (chosen) {
            assertThat(getDriver().findElements(By.xpath("//*[@class='reward-variable-name' and text()='" + variable + "' and @chosen]")).size(), is(not(0)));
        } else {
            assertThat(getDriver().findElements(By.xpath("//*[@class='reward-variable-name' and text()='" + variable + "' and not(@chosen)]")).size(), is(not(0)));
        }
    }

    public void checkExperimentNameTagLabel(String label) {
        assertThat(getDriver().findElement(By.xpath("//*[@class='view-section']/descendant::span[@class='section-title-label']/following-sibling::tag-label[not(@hidden)]")).getText(), is(label));
    }

    public void checkExperimentPageObservationIsSelected(String observation, String isSelected) {
        assertThat(getDriver().findElement(By.xpath("//*[@class='observations-panel']/descendant::vaadin-checkbox[text()='" + observation + "']")).getAttribute("aria-checked"), is(isSelected));
    }
}
