package io.skymind.pathmind.bddtests.page;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import io.skymind.pathmind.bddtests.Utils;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.core.pages.PageObject;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public class GenericPage extends PageObject {

    private Utils utils;

    @FindBy(xpath = "//confirm-popup")
    private WebElement popupShadow;
    @FindBy(xpath = "//vaadin-grid-cell-content")
    private List<WebElement> experimentModelsNames;
    @FindBy(xpath = "(//vaadin-text-area)[1]")
    private WebElement notesField;
    @FindBy(xpath = "//span[@class='section-title-label' and text()='Rename project']/following-sibling::vaadin-text-field")
    private WebElement editProjectNameInputShadow;
    @FindBy(xpath = "//notes-field")
    private WebElement notesBlock;
    private By notesTextarea = By.cssSelector("#textarea");
    private By notesSaveBtn = By.cssSelector("#save");
    private By saveIcon = By.cssSelector("saveIcon");

    public void checkThatButtonExists(String buttonText) {
        String xpath = String.format("//vaadin-button[text()='%s']", buttonText);
        assertThat(getDriver().findElements(By.xpath(xpath)).size(), greaterThan(0));
    }

    public void checkThatButtonDoesntExist(String buttonText) {
        setImplicitTimeout(5, SECONDS);
        String xpath = String.format("//vaadin-button[text()='%s']", buttonText);
        waitFor(ExpectedConditions.invisibilityOfAllElements(getDriver().findElements(By.xpath(xpath))));
        resetImplicitTimeout();
    }

    public void clickTextContainsLink(String text) {
        String xpath = String.format("//*[contains(text(), '%s')]", text);
        utils.clickElementRepeatIfStaleException(By.xpath(xpath));
        System.out.println("user dir " + System.getProperty("user.dir"));
    }

    public void clickInButton(String buttonText) {
        String xpath = String.format("//*[normalize-space(text())='%s']", buttonText);
        utils.clickElementRepeatIfStaleException(By.xpath(xpath));
        System.out.println("user dir " + System.getProperty("user.dir"));
    }

    public void checkThatNotificationIsShown(String notificationText) {
        waitABit(1000);
        String xpath = String.format("//vaadin-notification-container//span[text()='%s']", notificationText);
        assertThat(getDriver().findElements(By.xpath(xpath)).size(), greaterThan(0));
    }

    public void checkThatTheConfirmationDialogIsShown(String confirmationDialogHeader) {
        setImplicitTimeout(5, SECONDS);
        WebElement popupShadow = getDriver().findElement(By.xpath("//confirm-popup"));
        waitFor(ExpectedConditions.visibilityOf(popupShadow));
        WebElement popupShadowRoot = utils.expandRootElement(popupShadow);
        WebElement header = popupShadowRoot.findElement(By.cssSelector("h3"));

        assertThat(header.getText(), is(confirmationDialogHeader));
        switch (confirmationDialogHeader) {
            case ("Stop training"):
                assertThat(getDriver().findElement(By.xpath("//confirm-popup/div/p[1]")).getText(), is("Are you sure you want to stop training?"));
                assertThat(getDriver().findElement(By.xpath("//confirm-popup/div/p[2]")).getText(), is("If you stop the training before it completes, you won't be able to download the policy. If you decide you want to start the training again, you can start a new experiment and use the same reward function."));
                assertThat(getDriver().findElement(By.xpath("//confirm-popup/div/p/b")).getText(), is("If you decide you want to start the training again, you can start a new experiment and use the same reward function."));
                assertThat(popupShadowRoot.findElement(By.cssSelector("#confirm")).getCssValue("background-color"), is("rgba(216, 9, 71, 1)"));
                break;
            case ("Experiment Archived"):
                assertThat(popupShadowRoot.findElement(By.cssSelector("popup-content > div.message")).getText(), is("The experiment was archived."));
                break;
            case ("Experiment Unarchived"):
                assertThat(popupShadowRoot.findElement(By.cssSelector("popup-content > div.message")).getText(), is("The experiment was unarchived."));
                break;
        }
        resetImplicitTimeout();
    }

    public void checkThatNoConfirmationDialogIsShown() {
        setImplicitTimeout(5, SECONDS);
        waitFor(ExpectedConditions.invisibilityOfAllElements(getDriver().findElements(By.xpath("//confirm-popup"))));
        resetImplicitTimeout();
    }

    public void inConfirmationDialogClickInButton(String buttonText) {
        setImplicitTimeout(5, SECONDS);
        WebElement popupShadow = getDriver().findElement(By.xpath("//confirm-popup"));
        waitFor(ExpectedConditions.visibilityOf(popupShadow));
        WebElement popupShadowRoot = utils.expandRootElement(popupShadow);

        List<WebElement> buttons = popupShadowRoot.findElements(By.cssSelector("vaadin-button"));
        Optional<WebElement> first = buttons.stream()
            .filter(b -> b.isDisplayed() && b.getText().equals(buttonText))
            .findFirst();
        String errorMessage = String.format("Button '%s' doesn't exist. Available buttons: %s.",
            buttonText,
            StringUtils.join(buttons.stream().map(WebElement::getText).collect(Collectors.joining(", ")))
        );
        assertThat(errorMessage, first.isPresent());
        first.get().click();
        resetImplicitTimeout();
        waitABit(4000);
    }

    public void switchProjectsTab() {
        utils.clickElementRepeatIfStaleException(By.xpath("//vaadin-tab[@aria-selected='false']"));
    }

    public void checkThatModelExistInArchivedTab(String modelName) {
        waitABit(2500);
        List<String> strings = new ArrayList<>();
        for (WebElement e : experimentModelsNames) {
            strings.add(e.getText());
        }
        assertThat(strings, hasItem(modelName));
    }

    public void checkThatModelNotExistInArchivedTab(String modelName) {
        waitABit(2500);
        List<String> strings = new ArrayList<>();
        for (WebElement e : experimentModelsNames) {
            strings.add(e.getText());
        }
        assertThat(strings, not(hasItem(modelName)));
    }

    public void clickBreadcrumbBtn(String breadcrumb) {
        WebElement bread = getDriver().findElement(By.xpath("//a[@class='breadcrumb' and contains(@href,'" + breadcrumb + "')]"));
        waitFor(ExpectedConditions.elementToBeClickable(bread));
        bread.click();
        waitABit(2500);
    }

    public void addNoteToTheProjectPage(String note) {
        WebElement notesShadow = utils.expandRootElement(notesBlock);
        notesShadow.findElement(notesTextarea).click();
        notesShadow.findElement(notesTextarea).sendKeys(note);
    }

    public void projectPageClickSaveBtn() {
        WebElement notesShadow = utils.expandRootElement(notesBlock);
        notesShadow.findElement(notesSaveBtn).click();
    }

    public void checkProjectNoteIs(String note) {
        WebElement notesShadow = utils.expandRootElement(notesBlock);
        assertThat(notesShadow.findElement(notesTextarea).getAttribute("value"), is(note.replaceAll("/n", "\n")));
    }

    public void inputProjectNameToTheEditPopup(String projectName) {
        WebElement e = utils.expandRootElement(editProjectNameInputShadow);
        WebElement input = e.findElement(By.cssSelector("input"));
        input.click();
        input.clear();
        input.sendKeys(projectName);
    }

    public void checkThatCheckmarkIsShown() {
        WebElement notesShadow = utils.expandRootElement(notesBlock);
        assertThat(notesShadow.findElement(notesSaveBtn).isDisplayed(), is(true));
    }

    public void duplicateCurrentTab() {
        waitABit(3000);
        String url = getDriver().getCurrentUrl();
        ((JavascriptExecutor) getDriver()).executeScript("window.open()");
        ArrayList<String> tabs = new ArrayList<>(getDriver().getWindowHandles());
        getDriver().switchTo().window(tabs.get(1));
        getDriver().get(url);
    }

    public void openTab(int tab) {
        waitABit(3000);
        ArrayList<String> tabs = new ArrayList<>(getDriver().getWindowHandles());
        getDriver().switchTo().window(tabs.get(tab));
    }

    public void checkPageUrlIs(String url) {
        waitABit(4000);
        waitFor(ExpectedConditions.urlMatches(url));
        assertThat(getDriver().getCurrentUrl(), is(url));
    }

    public void checkPageUrlContains(String url) {
        waitABit(4000);
        waitFor(ExpectedConditions.urlMatches(url));
        assertThat(getDriver().getCurrentUrl(), containsString(url));
    }

    public void checkPageTitleTagTextIs(String text) {
        waitABit(5000);
        assertThat(getDriver().getTitle(), is(text));
    }

    public void waitABitMs(int time) {
        waitABit(time);
    }

    public void checkThatConfirmationDialogNotShown(Boolean status) {
        setImplicitTimeout(4, SECONDS);
        if (status) {
            assertThat(getDriver().findElements(By.xpath("//confirm-popup")).size(), is(1));
        } else {
            assertThat(getDriver().findElements(By.xpath("//confirm-popup")).size(), is(0));
        }
        resetImplicitTimeout();
    }

    public void compareALPFileWithDownloadedFile(String alpFile) {
        File downloadedFile = new File(System.getProperty("user.dir") + "/models/" + alpFile);
        long downloadedFileSize = downloadedFile.length();
        System.out.println("downloadedFileSize " + downloadedFileSize);

        List<String> filesContainingSubstring = new ArrayList<String>();
        File file = new File(System.getProperty("user.dir"));
        if (file.exists() && file.isDirectory()) {
            String[] files = file.list();
            for (String fileName : files) {
                if (fileName.contains(Serenity.sessionVariableCalled("randomNumber").toString())) {
                    filesContainingSubstring.add(fileName);
                }
            }
        }

        for (String fileName : filesContainingSubstring) {
            System.out.println(fileName);
            File uploadedFile = new File(System.getProperty("user.dir") + "/" + fileName);
            long uploadedFileSize = uploadedFile.length();
            System.out.println("Actual size " + uploadedFileSize);
            assertThat(downloadedFileSize, is(uploadedFileSize));
        }
    }

    public void clickPopUpDialogCloseBtn() {
        getDriver().findElement(By.xpath("//vaadin-dialog-overlay[@id='overlay']/descendant::vaadin-button[last()]")).click();
    }

    public void checkThatUnexpectedErrorAlertIsNotShown() {
        setImplicitTimeout(5, SECONDS);
        assertThat(getDriver().findElements(By.xpath("//vaadin-notification-card[@theme='error' and @role='alert']")).size(), is(0));
        resetImplicitTimeout();
    }

    public void clickInTheNewTabModelButton(String text) {
        waitABit(2000);
        WebElement button = getDriver().findElement(By.xpath("//*[contains(text(),'" + text + "') and not(contains(@class,'section-title-label'))]"));
        Actions actions = new Actions(getDriver());
        actions.keyDown(Keys.CONTROL).build().perform();
        actions.moveToElement(button).build().perform();
        waitABit(2500);
        actions.click(button).build().perform();
        actions.keyUp(Keys.CONTROL).build().perform();
        waitABit(3000);
    }

    public void checkNetworkErrors() {
        List<LogEntry> entries = getDriver().manage().logs().get(LogType.PERFORMANCE).getAll();
        System.out.println(entries.size() + " " + LogType.PERFORMANCE + " log entries found");
        for (LogEntry entry : entries) {
            System.out.println(entry.getMessage());
            assertThat(entry.getMessage(), not(containsString("\"status\":\"4")));
        }
    }

    public void openUrlFromTheVariable(String url) {
        getDriver().navigate().to(Serenity.sessionVariableCalled(url).toString());
    }

    public void checkElement(boolean elementExist, String elementXpath, String elementText) {
        setImplicitTimeout(2, SECONDS);
        if (elementExist) {
            assertThat(getDriver().findElements(By.xpath(elementXpath)).size(), is(not(0)));
            assertThat(getDriver().findElement(By.xpath(elementXpath)).getText(), is(elementText));
        }else {
            assertThat(getDriver().findElements(By.xpath(elementXpath)).size(), is(0));
        }
        resetImplicitTimeout();
    }

    public String definePanel(String slot) {
        return (slot.equals("primary")) ? "middle-panel" : "comparison-panel";
    }

    public void clickKeyboardEnterBtn() {
        WebElement shadow = utils.expandRootElement(getDriver().findElement(By.xpath("//vaadin-dialog-overlay")));
        shadow.findElement(By.cssSelector("#overlay")).sendKeys(Keys.ENTER);
    }

    public void clickKeyboardEnterBtnOnConfirmationPopup() {
        WebElement shadow = utils.expandRootElement(getDriver().findElement(By.xpath("//confirm-popup")));
        shadow.findElement(By.cssSelector("#confirm")).sendKeys(Keys.ENTER);
    }
}
