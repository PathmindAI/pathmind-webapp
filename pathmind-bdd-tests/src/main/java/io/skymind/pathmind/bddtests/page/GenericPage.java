package io.skymind.pathmind.bddtests.page;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.skymind.pathmind.bddtests.Utils;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.core.pages.PageObject;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class GenericPage extends PageObject {

    private Utils utils;

    @FindBy(xpath = "//vaadin-dialog-overlay")
    private WebElement dialogShadow;
    @FindBy(xpath = "//vaadin-grid-cell-content")
    private List<WebElement> experimentModelsNames;
    @FindBy(xpath = "//vaadin-text-area[@theme='notes']")
    private WebElement notesField;
    @FindBy(xpath = "(//vaadin-text-field)[2]")
    private WebElement editProjectNameInputShadow;

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

    public void clickInButton(String buttonText) {
        String xpath = String.format("//*[text()='%s']", buttonText);
//        getDriver().findElement(By.xpath(xpath)).click();
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
        WebElement dialogShadow = getDriver().findElement(By.xpath("//vaadin-dialog-overlay"));
        waitFor(ExpectedConditions.visibilityOf(dialogShadow));
        WebElement overlay = utils.expandRootElement(dialogShadow);
        WebElement contentShadow = overlay.findElement(By.cssSelector("#content"));
        WebElement contentElements = utils.expandRootElement(contentShadow);
        WebElement header = contentElements.findElement(By.cssSelector(".header"));

        assertThat(header.getText(), is(confirmationDialogHeader));
        resetImplicitTimeout();
    }

    public void checkThatNoConfirmationDialogIsShown() {
        setImplicitTimeout(5, SECONDS);
        waitFor(ExpectedConditions.invisibilityOfAllElements(getDriver().findElements(By.xpath("//vaadin-dialog-overlay"))));
        resetImplicitTimeout();
    }

    public void inConfirmationDialogClickInButton(String buttonText) {
        setImplicitTimeout(5, SECONDS);
        WebElement dialogShadow = getDriver().findElement(By.xpath("//vaadin-dialog-overlay"));
        waitFor(ExpectedConditions.visibilityOf(dialogShadow));
        WebElement overlay = utils.expandRootElement(dialogShadow);
        WebElement contentShadow = overlay.findElement(By.cssSelector("#content"));
        WebElement contentElements = utils.expandRootElement(contentShadow);

        List<WebElement> buttons = contentElements.findElements(By.cssSelector("vaadin-button"));
        List<WebElement> nonShadowButtons = contentShadow.findElements(By.cssSelector("vaadin-button"));
        List<WebElement> allButtons = Stream.concat(buttons.stream(), nonShadowButtons.stream()).collect(Collectors.toList());
        Optional<WebElement> first = allButtons.stream()
            .filter(b -> b.isDisplayed() && b.getText().equals(buttonText))
            .findFirst();
        String errorMessage = String.format("Button '%s' doesn't exist. Available buttons: %s.",
            buttonText,
            StringUtils.join(allButtons.stream().map(WebElement::getText).collect(Collectors.joining(", ")))
        );
        assertThat(errorMessage, first.isPresent());
        first.get().click();
        resetImplicitTimeout();
    }

    public void switchProjectsTab() {
        getDriver().findElement(By.xpath("//vaadin-tab[@aria-selected='false']")).click();
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
        notesField.click();
        notesField.sendKeys(note);
    }

    public void projectPageClickSaveBtn() {
        getDriver().findElement(By.xpath("//vaadin-vertical-layout[@class='notes-block']/descendant::vaadin-button")).click();
    }

    public void checkProjectNoteIs(String note) {
        assertThat(notesField.getAttribute("value"), is(note));
    }

    public void inputProjectNameToTheEditPopup(String projectName) {
        WebElement e = utils.expandRootElement(editProjectNameInputShadow);
        WebElement input = e.findElement(By.cssSelector("input"));
        input.click();
        input.clear();
        input.sendKeys(projectName);
    }

    public void checkThatCheckmarkIsShown() {
        assertThat(getDriver().findElement(By.xpath("//iron-icon[@icon='vaadin:check' and @class='fade-in']")).isDisplayed(), is(true));
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
            assertThat(getDriver().findElements(By.xpath("//*[@role='dialog']")).size(), is(1));
        } else {
            assertThat(getDriver().findElements(By.xpath("//*[@role='dialog']")).size(), is(0));
        }
        resetImplicitTimeout();
    }

    public void checkTitleLabelTagIsArchived(String tag) {
        assertThat(getDriver().findElement(By.xpath("//span[@class='section-subtitle-label']/following-sibling::tag-label")).getText(), is(tag));
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
                if (fileName.contains(Serenity.sessionVariableCalled("randomNumber").toString()))
                    filesContainingSubstring.add(fileName);
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
}
