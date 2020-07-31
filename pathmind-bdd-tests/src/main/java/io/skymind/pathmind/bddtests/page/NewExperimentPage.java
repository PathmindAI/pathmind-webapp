package io.skymind.pathmind.bddtests.page;

import io.skymind.pathmind.bddtests.Utils;
import net.serenitybdd.core.pages.PageObject;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

public class NewExperimentPage extends PageObject {

    private Utils utils;

    @FindBy(xpath = "//juicy-ace-editor")
    private WebElement rewardField;
    @FindBy(xpath = "//vaadin-button[text()='Train Policy']")
    private WebElement startDiscoveryRunBtn;
    @FindBy(xpath = "//vaadin-text-field[contains(@class,'reward-variable-name-field')]")
    private List<WebElement> rewardVariableNameInputs;
    @FindBy(xpath = "//vaadin-dialog-overlay")
    private WebElement overlay;
    private final By byInput = By.cssSelector("input");

    public void checkThatExperimentPageOpened(String projectName) {
        assertThat(getDriver().findElement(By.xpath("//a[contains(@href, 'project/')]")).getText(), containsString(projectName));
    }

    public void inputRewardFunctionFile(String rewardFile) {
        rewardField.click();
        rewardField.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        if (rewardFile.isEmpty()) {
            System.out.println("No file");
        } else {
            try {
                rewardField.sendKeys(FileUtils.readFileToString(new File("models/" + rewardFile), StandardCharsets.UTF_8));
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
        WebElement we = getDriver().findElement(By.xpath("//vaadin-button[text()='Save']"));
        JavascriptExecutor executor = (JavascriptExecutor) getDriver();
        executor.executeScript("arguments[0].click();", we);
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

    public void checkThatBeforeYouLeavePopUpIsShownWithError(String error) {
        WebElement e = utils.expandRootElement(overlay);
        WebElement contentShadow = e.findElement(By.cssSelector("#content"));
        WebElement popUp = utils.expandRootElement(contentShadow);
        assertThat(popUp.findElement(By.cssSelector("h3")).getText(), is("Before you leave...."));
        assertThat(popUp.findElement(By.cssSelector("#message")).getText(), is(error));
    }

    public void clickSideBarExperiment(String experimentName) {
        getDriver().findElement(By.xpath("//div[@class='experiment-name']/p[text()='"+ experimentName +"']")).click();
    }
}
