package io.skymind.pathmind.bddtests.page;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.skymind.pathmind.bddtests.Utils;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.core.pages.PageObject;
import net.thucydides.core.annotations.DefaultUrl;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
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
    @FindBy(xpath = "//reward-terms-viewer")
    private WebElement rewardFunctionNew;
    @FindBy(xpath = "//*[text()='Notes']/ancestor::*[@class='notes-field-wrapper']/descendant::vaadin-text-area")
    private WebElement experimentNotes;
    @FindBy(xpath = "//span[text()='Status']/following-sibling::span[1]")
    private WebElement experimentStatus;
    @FindBy(xpath = "//notes-field")
    private WebElement notesBlock;
    private By notesTextarea = By.cssSelector("#textarea");
    private By notesSaveBtn = By.cssSelector("#save");

    public void checkExperimentPageRewardFunction(String rewardFnFile) throws IOException {
        assertThat(rewardFunction.getText(), is(FileUtils.readFileToString(new File("models/" + rewardFnFile), StandardCharsets.UTF_8).replaceAll("\r", "")));
    }

    public void checkExperimentPageRewardFunctionNew(String rewardFnFile) throws IOException {
        assertThat(rewardFunctionNew.getText(), is(FileUtils.readFileToString(new File("models/" + rewardFnFile), StandardCharsets.UTF_8).replaceAll("\r", "")));
    }

    public void addNoteToTheExperimentPage(String note) {
        WebElement notesShadow = utils.expandRootElement(notesBlock);
        notesShadow.findElement(notesTextarea).click();
        notesShadow.findElement(notesTextarea).sendKeys(note);
        waitABit(3000);
    }

    public void clickCurrentExperimentArchiveButton() {
        getDriver().findElement(By.xpath("//iron-icon[@icon='vaadin:archive']/ancestor::vaadin-button")).click();
    }

    public void clickArchiveButtonForCurrentDraftExperiment() {
        getDriver().findElement(By.xpath("//*[@theme='split-button align-center new-experiment-split-button']")).click();
        WebElement archiveButton = getDriver().findElement(By.xpath("//vaadin-item[text()='Archive']"));
        waitFor(ExpectedConditions.elementToBeClickable(archiveButton));
        archiveButton.click();
    }

    public void checkExperimentNotesIs(String note) {
        WebElement notesShadow = utils.expandRootElement(notesBlock);
        assertThat(notesShadow.findElement(notesTextarea).getAttribute("value"), containsString(note.replaceAll("/n", "\n")));
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

    public void clickSideNavButtonFromNavbarItemMenuFor(String btn, String experiment) {
        waitABit(3500);
        WebElement element = utils.expandRootElement(utils.getExperimentNavbarItemByExperimentName(experiment, null));
        waitABit(4000);
        WebElement compareBtn = utils.expandRootElement(element.findElement(By.cssSelector("#compareButton")));
        compareBtn.findElement(By.id("button")).click();
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
        assertThat(getDriver().findElement(By.xpath("//span[text()='Simulation Metrics']/parent::vaadin-horizontal-layout/parent::vaadin-vertical-layout")).isDisplayed(), is(true));
    }

    public void checkThatExperimentExistOnTheExperimentPage(String experiment) {
        waitABit(4000);
        assertThat(utils.getStringListFromShadowRootRepeatIfStaleException(By.xpath("//experiments-navbar-item"), By.cssSelector(".experiment-name p:first-child")), hasItem(experiment));
    }

    public void clickCopyRewardFunctionBtn() {
        WebElement e = utils.expandRootElement(rewardFunction);
        e.findElement(By.cssSelector("vaadin-button")).click();
        WebElement notesShadow = utils.expandRootElement(notesBlock);
        notesShadow.findElement(notesTextarea).click();
        notesShadow.findElement(notesTextarea).sendKeys(Keys.CONTROL + "A");
        notesShadow.findElement(notesTextarea).sendKeys(Keys.BACK_SPACE);
        notesShadow.findElement(notesTextarea).sendKeys(Keys.CONTROL + "V");
        notesShadow.findElement(notesSaveBtn).click();
    }

    public void checkThatExperimentNotExistOnTheExperimentPage(String experiment) {
        waitABit(4000);
        assertThat(utils.getStringListFromShadowRootRepeatIfStaleException(By.xpath("//experiments-navbar-item"), By.cssSelector(".experiment-name p:first-child")), not(hasItem(experiment)));
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
        for (WebElement webElement : getDriver().findElements(By.xpath("//experiments-navbar-item"))) {
            WebElement experimentNavbarItemShadow = utils.expandRootElement(webElement);
            String experimentNameText = experimentNavbarItemShadow.findElement(By.cssSelector(".experiment-name p:first-child")).getText();
            actual.add(experimentNameText);
        }

        assertThat(actual, containsInAnyOrder(items.toArray()));
    }

    public void checkThatExperimentPageArchivedTagIsShown() {
        assertThat(getDriver().findElement(By.xpath("//vaadin-horizontal-layout[@class='experiment-header']/descendant::tag-label[not(@hidden)]")).getText(), is("Archived"));
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
        assertThat(getDriver().findElement(By.xpath("//*[@class='header-row']/span[1]")).getText(), is("Metric"));
        assertThat(getDriver().findElement(By.xpath("//*[@class='metrics-wrapper']/div/span")).getText(), is("Value"));
        assertThat(getDriver().findElement(By.xpath("//*[@class='simulation-metrics-panel-header']/a")).getAttribute("href"), is("https://help.pathmind.com/en/articles/4305404-simulation-metrics"));
        assertThat(getDriver().findElement(By.xpath("//*[@class='sparklines-wrapper']/div/span")).getText(), is("Overview"));
    }

    public void clickSimulationMetricsValueIcon() {
        getDriver().findElement(By.xpath("//*[@class='simulation-metrics-panel-header']/a")).click();
    }

    public void clickSimulationMetricsOverviewIcon() {
        getDriver().findElement(By.xpath("//*[@class='sparklines-wrapper']/div/a")).click();
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

    public void checkLearningProgressTitle(String title) {
        assertThat(getDriver().findElement(By.xpath("//vaadin-vertical-layout[@class='row-2-of-3']/span")).getText(), is(title));
    }

    public void checkLearningProgressBlockSelectedTabNameIs(String selected, String tab) {
        assertThat(getDriver().findElements(By.xpath("//vaadin-vertical-layout[@class='row-2-of-3']/descendant::vaadin-tab[@aria-selected='" + selected + "' and text()='" + tab + "']")).size(), is(not(0)));
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
        waitABit(3000);
        assertThat(getDriver().findElement(By.xpath("//vaadin-horizontal-layout[@class='experiment-header']/descendant::tag-label[not(@hidden)]")).getText(), is(label));
    }

    public void checkExperimentPageObservationIsSelected(String observation, String isSelected) {
        assertThat(getDriver().findElement(By.xpath("//*[@class='observations-panel']/descendant::vaadin-checkbox[text()='" + observation + "']")).getAttribute("aria-checked"), is(isSelected));
    }

    public void checkSideBarExperimentDateIs(String experiment, String date) {
        assertThat(utils.getExperimentNavbarItemByExperimentName(experiment, "#experimentLink > div > p:nth-child(2)").getText(), is(date));
    }

    public void checkNumberOfTheExperimentsIsInTheLeftSidebar(int experimentsNumber) {
        assertThat(getDriver().findElements(By.xpath("//*[@class='experiments-navbar-items']/experiments-navbar-item")).size(), is(experimentsNumber));
    }

    public void checkLearningProgressBlockTabs(String tabs) {
        List<String> items = Arrays.asList(tabs.split("\\s*,\\s*"));
        List<String> actual = new ArrayList<>();
        for (WebElement webElement : getDriver().findElements(By.xpath("//vaadin-vertical-layout[@class='row-2-of-3']/descendant::vaadin-tab"))) {
            actual.add(webElement.getText());
        }

        assertThat(actual, containsInRelativeOrder(items.toArray()));
    }

    public void checkLearningProgressBlockHistogramSimulationMetricIs(String metric, String value) {
        assertThat(getDriver().findElement(By.xpath("//*[@class='histogram-chart-mean']/descendant::span[2]")).getText(), is(metric));
        assertThat(getDriver().findElement(By.xpath("//*[@class='histogram-chart-mean']/descendant::span[3]")).getText(), is(value));
    }

    public void clickExperimentPageShareWithSupportBtn() {
        getDriver().findElement(By.xpath("//iron-icon[@icon='vaadin:share-square']/ancestor::vaadin-button")).click();
    }

    public void clickExperimentPageActionsBtn(String btn) {
        switch (btn){
            case "Archive":
                getDriver().findElement(By.xpath("//iron-icon[@icon='vaadin:archive']/ancestor::vaadin-button")).click();
                break;
            case "Unarchive":
                getDriver().findElement(By.xpath("//iron-icon[@icon='vaadin:arrow-backward']/ancestor::vaadin-button")).click();
                break;
            case "Share":
                getDriver().findElement(By.xpath("//iron-icon[@icon='vaadin:share-square']/ancestor::vaadin-button")).click();
                break;
        }
    }

    public void experimentPageClickComparisonFloatingCloseBtn() {
        getDriver().findElement(By.xpath("//floating-close-button")).click();
        assertThat(getDriver().findElements(By.xpath("//experiments-navbar-item[@is-current-comparison-experiment]")).size(), is(0));
    }

    public void checkLearningProgressBlockHistogramXAxisIsShown() {
        WebElement e = utils.expandRootElement(getDriver().findElement(By.cssSelector("histogram-chart")));
        WebElement chart = utils.expandRootElement(e.findElement(By.cssSelector("google-chart")));
        assertThat(chart.findElement(By.cssSelector("#chartdiv > div > div:nth-child(1) > div > svg > g:nth-child(4) > g:nth-child(1) > text")).getText(), is("Value"));
    }

    public void checkExperimentPageStartRunBtnIsActiveTrue(Boolean shown) {
        waitABit(8000);
        WebElement btnShadow = utils.expandRootElement(getDriver().findElement(By.xpath("//vaadin-button[@theme='small new-experiment-split-button split-button primary']")));
        if (!shown) {
            assertThat(btnShadow.findElement(By.cssSelector("button")).getAttribute("disabled"), is("true"));
        } else {
            assertThat(btnShadow.findElement(By.cssSelector("button")).getAttribute("disabled"), is(null));
        }
    }

    public void checkDeployingPolicyServerOverlay() {
        assertThat(getDriver().findElement(By.xpath("//vaadin-dialog-overlay/descendant::div[@class='serve-policy-instructions']/h3")).getText(), is("Deploying Policy Server"));
        assertThat(getDriver().findElement(By.xpath("//vaadin-dialog-overlay/descendant::div[@class='serve-policy-instructions']/p")).getText(), is("Your policy will be available in about five minutes."));
    }

    public void checkPolicyServerLiveWithMinutes(int limit) {
        System.out.println("!Waiting for policy server live with limit " + limit + " minutes!");
        for (int i = 0; i < limit; i++) {
            if (getDriver().findElements(By.xpath("//vaadin-button[text()='Policy Server Live']")).size() != 0) {
                break;
            } else {
                waitABit(60000);
                getDriver().navigate().refresh();
            }
        }
        assertThat(getDriver().findElement(By.xpath("//*[@class='experiment-header']/*[@class='buttons-wrapper']/vaadin-button[2]")).getText(), containsString("Policy Server Live"));
    }

    public void checkPolicyServerLiveOverlay() throws IOException, UnsupportedFlavorException {
        assertThat(getDriver().findElement(By.xpath("//vaadin-dialog-overlay/descendant::div[@class='serve-policy-instructions']/policy-server-live-content/h3")).getText(), is("The Policy is Live"));
        assertThat(getDriver().findElement(By.xpath("//vaadin-dialog-overlay/descendant::div[@class='serve-policy-instructions']/policy-server-live-content/span[1]")).getText(), is("The policy is being served at this URL:"));
        assertThat(getDriver().findElement(By.xpath("//vaadin-dialog-overlay/descendant::div[@class='serve-policy-instructions']/policy-server-live-content/p/span")).getText(), is("Read the docs for more details:"));
        assertThat(getDriver().findElement(By.xpath("//vaadin-dialog-overlay/descendant::div[@class='serve-policy-instructions']/policy-server-live-content/p/a")).getText(), containsString("https://api.dev.devpathmind.com/policy/"));
        assertThat(getDriver().findElement(By.xpath("//vaadin-dialog-overlay/descendant::div[@class='serve-policy-instructions']/policy-server-live-content/p/a")).getAttribute("href"), containsString("https://api.dev.devpathmind.com/policy/"));
        WebElement e = utils.expandRootElement(getDriver().findElement(By.xpath("//vaadin-dialog-overlay/descendant::copy-field")));
        String copyFieldUrl = e.findElement(By.id("textToCopy")).getText();
        assertThat(e.findElement(By.id("textToCopy")).getText(), containsString("https://api.dev.devpathmind.com/policy/"));
        e.findElement(By.id("copy")).click();
        ((JavascriptExecutor) getDriver()).executeScript("window.open()");
        ArrayList<String> tabs = new ArrayList<String>(getDriver().getWindowHandles());
        getDriver().switchTo().window(tabs.get(1));
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable contents = clipboard.getContents(null);
        String url = (String) contents.getTransferData(DataFlavor.stringFlavor);
        Serenity.setSessionVariable("policyServerUrl").to(url);
        getDriver().get(url);
        assertThat(getDriver().findElement(By.xpath("//pre")).getText(), containsString("ok"));
        assertThat(getDriver().getCurrentUrl(), is(copyFieldUrl));
        getDriver().close();
        getDriver().switchTo().window(tabs.get(0));
    }

    public void checkShutdownPolicyServerConfirmationPopup() {
        String experimentId = getDriver().getCurrentUrl().split("experiment/")[1];
        WebElement e = utils.expandRootElement(getDriver().findElement(By.xpath("//confirm-popup")));
        assertThat(e.findElement(By.cssSelector("h3")).getText(), is("Shut down policy server"));
        assertThat(e.findElement(By.cssSelector(".message")).getText(), containsString("This will shut down the deployed policy server for this experiment (id: " + experimentId + "). You will be able to redeploy the policy server."));
    }

    public void clickPopUpDialogIdCancel(String id) {
        WebElement popupShadow = getDriver().findElement(By.xpath("//confirm-popup"));
        waitFor(ExpectedConditions.visibilityOf(popupShadow));
        WebElement popupShadowRoot = utils.expandRootElement(popupShadow);
        popupShadowRoot.findElement(By.cssSelector("#" + id)).click();
    }

    public void checkExperimentSharedBy(String firstName, String lastName) {
        assertThat(getDriver().findElement(By.xpath("//shared-by-username")).getText(), is(firstName + " " + lastName));
    }

    public void checkPolicyServerOverlayTokenWithAccountPage() {
        String policyServerToken = getDriver().findElement(By.xpath("//vaadin-dialog-overlay/descendant::div[@class='serve-policy-instructions']/policy-server-live-content/copy-field[2]")).getAttribute("text");
        String url = getDriver().getCurrentUrl();
        ((JavascriptExecutor) getDriver()).executeScript("window.open('about:blank','_blank');");
        waitABit(3000);
        ArrayList<String> tabs = new ArrayList<String>(getDriver().getWindowHandles());
        getDriver().switchTo().window(tabs.get(1));
        getDriver().navigate().to(url);
        getDriver().findElement(By.xpath("//vaadin-menu-bar[@class='account-menu']")).click();
        getDriver().findElement(By.xpath("//vaadin-context-menu-item[@role='menuitem' and text()='Account']")).click();
        assertThat(getDriver().findElement(By.id("accessToken")).getText(), is(policyServerToken));
        getDriver().switchTo().window(tabs.get(0));
    }
}
