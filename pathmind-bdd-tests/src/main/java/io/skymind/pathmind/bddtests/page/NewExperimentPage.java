package io.skymind.pathmind.bddtests.page;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.skymind.pathmind.bddtests.Utils;
import net.serenitybdd.core.pages.PageObject;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class NewExperimentPage extends PageObject {

    private Utils utils;

    @FindBy(xpath = "//*[@class='experiments-navbar']/vaadin-button")
    private WebElement newExperimentBtn;
    @FindBy(xpath = "//juicy-ace-editor")
    private WebElement rewardField;
    @FindBy(xpath = "//vaadin-button[@theme='small new-experiment-split-button split-button primary']")
    private WebElement startDiscoveryRunBtn;
    @FindBy(xpath = "//vaadin-text-field[contains(@class,'reward-variable-name-field')]")
    private List<WebElement> rewardVariableNameInputs;
    @FindBy(xpath = "//confirm-popup")
    private WebElement confirmPopup;
    private final By byInput = By.cssSelector("input");

    public void clickSideBarNewExperimentBtn() {
        Actions actions = new Actions(getDriver());
        actions.moveToElement(newExperimentBtn).build().perform();
        waitABit(2500);
        actions.click(newExperimentBtn).build().perform();
        waitABit(3000);
    }

    public void checkThatExperimentPageOpened(String projectName) {
        assertThat(utils.getStringRepeatIfStaleException(By.xpath("//a[contains(@href, 'project/')]")), containsString(projectName));
    }

    public void inputRewardFunctionFile(String rewardFile) {
        rewardField.click();
        rewardField.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        if (rewardFile.isEmpty()) {
            System.out.println("No file");
        } else {
            try {
                utils.sendKeysCarefully(FileUtils.readFileToString(new File("models/" + rewardFile), StandardCharsets.UTF_8), rewardField);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        waitABit(5000);
    }

    public void clickProjectStartDiscoveryRunButton() {
        waitFor(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@style='display: none;']")));
        waitFor(ExpectedConditions.elementToBeClickable(startDiscoveryRunBtn));
        waitABit(2500);
        startDiscoveryRunBtn.click();
        waitFor(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class='section-title-label' and contains(text(),'Experiment')]")));
        waitABit(3500);
    }

    public void inputRewardFunction(String rewardFunction) {
        waitABit(2500);
        utils.clickElementRepeatIfStaleException(By.xpath("//juicy-ace-editor"));
        rewardField.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        rewardField.sendKeys(Keys.BACK_SPACE);
        rewardField.sendKeys(rewardFunction);
    }

    public void clickProjectSaveDraftBtn() {
        waitABit(5000);
        Actions action = new Actions(getDriver());
        WebElement we = getDriver().findElement(By.xpath("//vaadin-select[@theme='split-button align-center new-experiment-split-button']"));
//        JavascriptExecutor executor = (JavascriptExecutor) getDriver();
//        executor.executeScript("arguments[0].click();", we);
        we.click();
        getDriver().findElement(By.xpath("//vaadin-item[@value='1']")).click();

        setImplicitTimeout(5, SECONDS);
        try {
            WebElement closePopUp = getDriver().findElement(By.xpath("//span[text()='Draft successfully saved']/following-sibling::vaadin-button[@theme='icon']"));
            waitFor(ExpectedConditions.visibilityOf(closePopUp));
            waitFor(ExpectedConditions.elementToBeClickable(closePopUp));
            closePopUp.click();
            action.moveToElement(closePopUp).click().perform();
        } catch (Exception e) {
            System.out.println("Button not exist");
        }
        resetImplicitTimeout();
        waitABit(5000);
    }

    public void checkRewardFunctionIs(String rewardFunction) {
        waitABit(2000);
        WebElement e = utils.expandRootElement(rewardField);
        assertThat(e.findElement(By.cssSelector(".ace_line")).getText(), is(rewardFunction));
    }

    public void checkCodeEditorRowHasVariableMarked(int row, int expectedSize, String variableName, int variableIndex) {
        waitABit(1000);
        WebElement e = utils.expandRootElement(rewardField);
        WebElement rowElement = e.findElements(By.className("ace_line")).get(row);
        List<WebElement> rewardVariables = rowElement.findElements(By.className("ace_reward_variable"));
        assertThat("Number of variable occurances", rewardVariables.size() == expectedSize);
        rewardVariables.forEach(rewardVariable -> {
            assertThat(rewardVariable.getText(), is(variableName));
            assertThat(rewardVariable.getAttribute("class"), containsString("variable-color-" + variableIndex));
        });
    }

    public void inputVariableName(String variableName, int variableIndex) {
        WebElement textField = rewardVariableNameInputs.get(variableIndex);
        WebElement e = utils.expandRootElement(textField);
        WebElement variableNameInputField = e.findElement(byInput);
        variableNameInputField.click();
        variableNameInputField.clear();
        variableNameInputField.sendKeys(variableName);
        variableNameInputField.sendKeys(Keys.ENTER);
    }

    public void checkRewardFunctionDefaultValue(String reward) {
        WebElement e = utils.expandRootElement(rewardField);
        assertThat(e.findElement(By.cssSelector("div[class='ace_line']")).getText(), is(reward));
    }

    public void checkThatNotesSavedMsgShown() {
        assertThat(getDriver().findElement(By.xpath("//span[text()='Notes saved!' and @class='fade-out-hint-label fade-in']")).isDisplayed(), is(true));
    }

    public void clickSideBarExperiment(String experimentName) {
        waitABit(2000);
        utils.getExperimentNavbarItemByExperimentName(experimentName, null).click();
        waitFor(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class='section-title-label' and text()='" + experimentName + "']")));
    }

    public void clickObservationsCheckbox(String checkbox) {
        getDriver().findElement(By.xpath("//vaadin-checkbox[@role='checkbox' and text()='" + checkbox + "']")).click();
    }

    public void checkThatNewExperimentRewardVariableGoalAndValue(String rewardVariable, String goalSign) {
        assertThat(getDriver().findElement(By.xpath("//span[contains(@class,'reward-variable-name') and text()='" + rewardVariable + "']/parent::vaadin-horizontal-layout/span[@class='goal-display-span']")).getText(), is(goalSign));
    }

    public void checkThatExperimentPageTitleIs(String experiment) {
        assertThat(getDriver().findElement(By.cssSelector(".section-title-label")).getText(), is(experiment));
    }

    public void checkNewExperimentPageModelALPBtn(String filename) {
        waitFor(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@class='download-alp-link']/descendant::vaadin-button")));
        assertThat(getDriver().findElement(By.xpath("//a[@class='download-alp-link']/descendant::vaadin-button")).getText(), is("Model ALP"));
        assertThat(getDriver().findElement(By.xpath("//a[@class='download-alp-link']")).getAttribute("href"), containsString(filename));
    }

    public void checkSideBarStarBtnTooltipIsFavorite(String tooltip) {
        waitABit(3500);
        WebElement experimentNavBarItemShadow = utils.expandRootElement(getDriver().findElement(By.xpath("//experiment-navbar-item[@is-current]")));
        WebElement favoriteStarShadow = utils.expandRootElement(experimentNavBarItemShadow.findElement(By.cssSelector("favorite-star")));
        waitFor(ExpectedConditions.elementToBeClickable(favoriteStarShadow.findElement(By.cssSelector("vaadin-button"))));
        assertThat(favoriteStarShadow.findElement(By.cssSelector("vaadin-button")).getAttribute("title"), is(tooltip));
    }

    public void checkNewExperimentPageTrainPolicyBtn(Boolean btnStatus) {
        waitABit(4000);
        if (btnStatus) {
            waitFor(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@class='panel-title']/following-sibling::vaadin-horizontal-layout/descendant::vaadin-button[not(@aria-disabled='true')]")));
            waitFor(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@class='panel-title']/following-sibling::vaadin-horizontal-layout/descendant::vaadin-button[not(@disabled)]")));
            assertThat(getDriver().findElements(By.xpath("//*[@class='panel-title']/following-sibling::vaadin-horizontal-layout/descendant::vaadin-button[not(@aria-disabled='true')]")).size(), is(not(0)));
            assertThat(getDriver().findElements(By.xpath("//*[@class='panel-title']/following-sibling::vaadin-horizontal-layout/descendant::vaadin-button[not(@disabled)]")).size(), is(not(0)));
        } else {
            waitFor(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@class='panel-title']/following-sibling::vaadin-horizontal-layout/descendant::vaadin-button[@aria-disabled='true' and @disabled]")));
            assertThat(getDriver().findElement(By.xpath("//*[@class='panel-title']/following-sibling::vaadin-horizontal-layout/descendant::vaadin-button[1]")).getAttribute("aria-disabled"), is("true"));
            assertThat(getDriver().findElement(By.xpath("//*[@class='panel-title']/following-sibling::vaadin-horizontal-layout/descendant::vaadin-button[1]")).getAttribute("disabled"), is("true"));
        }
    }

    public void cleanNewExperimentRewardFunctionField() {
        waitABit(2500);
        utils.clickElementRepeatIfStaleException(By.xpath("//juicy-ace-editor"));
        rewardField.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        rewardField.sendKeys(Keys.BACK_SPACE);
    }

    public void openExperimentFromSidebarInTheNewTab(String experiment) {
        waitABit(2000);
        WebDriver driver = getDriver();
        WebElement experimentNavItemAnchor = utils.getExperimentNavbarItemByExperimentName(experiment, "a");
        String linkPath = experimentNavItemAnchor.getAttribute("href");
        String jsCommand = String.format("window.open('%s', '_blank');", linkPath);
        ((JavascriptExecutor) driver).executeScript(jsCommand);
        waitABit(3000);
    }

    public void clickNewExperimentPageObservationCheckbox(String observation) {
        utils.clickElementRepeatIfStaleException(By.xpath("//*[@class='observations-panel']/descendant::vaadin-checkbox[text()='" + observation + "']"));
    }

    public void checkNewExperimentObservationsListContains(String observations) {
        List<String> items = Arrays.asList(observations.split("\\s*,\\s*"));
        List<String> actual = new ArrayList<>();
        for (WebElement webElement : getDriver().findElements(By.xpath("//vaadin-checkbox-group/vaadin-checkbox"))) {
            actual.add(webElement.getText());
        }

        assertThat(actual, containsInRelativeOrder(items.toArray()));
    }
}
