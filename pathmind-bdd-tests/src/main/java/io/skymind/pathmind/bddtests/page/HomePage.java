package io.skymind.pathmind.bddtests.page;

import io.skymind.pathmind.bddtests.Utils;
import net.serenitybdd.core.pages.PageObject;
import net.thucydides.core.annotations.DefaultUrl;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DefaultUrl("page:home.page")
public class HomePage extends PageObject {

    private Utils utils;

    @FindBy(id = "nav-account-links")
    private WebElement navAccoutLinks;
    @FindBy(xpath = "//a[text()='Projects']")
    private WebElement projectsBtn;
    @FindBy(xpath = "//vaadin-menu-bar[@class='account-menu']")
    private WebElement menuBarShadow;
    @FindBy(xpath = "(//vaadin-context-menu-item[@role='menuitem'])[last()]")
    private WebElement logoutBtn;
    @FindBy(xpath = "//a[text()='Help']")
    private WebElement helpBtn;
    @FindBy(xpath = "//span[@class='breadcrumb']")
    private WebElement pageLabel;
    @FindBy(xpath = "//vaadin-menu-bar[@class='account-menu']")
    private WebElement accountMenuBtn;
    @FindBy(xpath = "//vaadin-context-menu-item[@role='menuitem' and text()='Account']")
    private WebElement accountBtn;
    @FindBy(css = ".search-box_text-field")
    private WebElement searchBoxShadow;
    @FindBy(css = ".navbar-logo")
    private WebElement navBarLogo;

    private By upgradeToProBtn = By.xpath("//upgrade-to-pro-button");

    public void checkNavAccLinkVisible(String name) {
        assertThat(getDriver().findElement(By.xpath("//vaadin-horizontal-layout[@id='nav-main-links']//a[2]")).getText(), is("Projects"));
    }

    public void openProjectsPage() {
        projectsBtn.click();
        waitFor(ExpectedConditions.titleIs("Pathmind | Projects"));
        utils.waitForLoadingBar();
    }

    public void logoutFromPathmind() {
        menuBarShadow.click();
        logoutBtn.click();
    }

    public void clickLearnBtn() {
        helpBtn.click();
    }

    public void checkThatLearnPageOpened(String learnPage) {
        ArrayList<String> tabs = new ArrayList<String>(getDriver().getWindowHandles());
        getDriver().switchTo().window(tabs.get(1));
        assertThat(getDriver().getCurrentUrl(), equalTo(learnPage));
        assertThat(getDriver().getTitle(), containsString("Pathmind Knowledge Base"));
    }

    public void checkThatProjectsPageOpened() {
        waitFor(ExpectedConditions.titleIs("Pathmind | Projects"));
        assertThat(getDriver().findElement(By.xpath("//span[@class='section-title-label truncated-label']")).getText(), is("Projects"));
    }

    public void closeBrowserTab() {
        ArrayList<String> tabs = new ArrayList<String>(getDriver().getWindowHandles());
        getDriver().switchTo().window(tabs.get(1));
        getDriver().close();
        getDriver().switchTo().window(tabs.get(0));
    }

    public void openUserDropdown() {
        waitABit(2000);
        accountMenuBtn.click();
    }

    public void clickAccountBtn() {
        waitABit(2000);
        accountBtn.click();
    }

    public void checkThatProjectsButtonHighlightIs(Boolean status) {
        Actions actions = new Actions(getDriver());
        if (status) {
            utils.waitForLoadingBar();
            actions.moveToElement(navBarLogo).build().perform();
            waitABit(3000);
            assertThat(getDriver().findElement(By.xpath("//a[text()='Projects']")).getAttribute("highlight"), is(""));
        } else {
            actions.moveToElement(navBarLogo).build().perform();
            waitABit(3000);
            assertThat(getDriver().findElement(By.xpath("//a[text()='Projects']")).getAttribute("highlight"), is(nullValue()));
        }
    }

    public void inputToTheNotesSearchField(String text) {
        WebElement e = utils.expandRootElement(searchBoxShadow);
        WebElement input = e.findElement(By.cssSelector("input"));
        input.click();
        input.sendKeys(text);
    }

    public void clickNotesSearchBtn() {
        getDriver().findElement(By.cssSelector(".search-box_button")).click();
    }

    public void checkSearchResultPageNotesContainsSearch(String text) {
        waitABit(3500);
        for (WebElement webElement : getDriver().findElements(By.xpath("//*[@class='grid-notes-column']//span[@class='highlight-label']"))) {
            assertThat(webElement.getText(), containsString(text));
        }
    }

    public void clickNotesClearBtn() {
        WebElement e = utils.expandRootElement(searchBoxShadow);
        e.findElement(By.id("clearButton")).click();
    }

    public void checkNotesSearchFieldIs(String text) {
        assertThat(searchBoxShadow.getText(), is(text));
    }

    public void checkSearchResultPageContainsProjectName(String name) {
        assertThat(getDriver().findElement(By.xpath("//*[@class='highlighted-text-wrapper']//span[@class='highlight-label']")).getText(), is(name));
    }

    public void checkSearchResultPageContainsModelName(String name) {
        assertThat(getDriver().findElement(By.xpath("//*[@class='highlighted-text-wrapper' and contains(text(), 'Model #')]//span[@class='highlight-label' and contains(text(),'" + name + "')]")).getText(), is(name));
    }

    public void clickAutotestProjectFromSearchPage(String name) {
        getDriver().findElement(By.xpath("//*[@class='highlighted-text-wrapper']//span[@class='highlight-label' and contains(text(), '" + name + "')]")).click();
    }

    public void clickToTheUniqueNoteOnTheSearchResultPage(String text) {
        getDriver().findElement(By.xpath("//*[@class='highlighted-text-wrapper grid-notes-column']//span[@class='highlight-label' and contains(text(),'" + text + "')]/ancestor::*[@class='search-result-item']/descendant::a[contains(@href, 'newExperiment')]")).click();
    }

    public void checkSearchResultPageProjectNameContainsArchivedTag(String name) {
        assertThat(getDriver().findElement(By.xpath("//*[@class='highlight-label' and contains(text(), '" + name + "')]/ancestor::vaadin-vertical-layout[@class='search-result-item']/descendant::vaadin-horizontal-layout[@class='info-row']//vaadin-horizontal-layout[1]//tag-label[3]")).getText(), is("Archived"));
    }

    public void checkSearchResultPageProjectNameContainsModelTag(String name, String modelNumber) {
        assertThat(getDriver().findElement(By.xpath("//*[@class='highlight-label' and contains(text(), '" + name + "')]/ancestor::vaadin-vertical-layout[@class='search-result-item']/descendant::vaadin-horizontal-layout[@class='info-row']//vaadin-horizontal-layout[1]//tag-label[2]")).getText(), is(modelNumber));
    }

    public void checkSearchResultsForValueIs(String value) {
        waitABit(3000);
        String[] text = getDriver().findElement(By.xpath("//*[@class='section-title-label truncated-label']")).getText().split(": ", 2);
        System.out.println(Arrays.toString(text));
        assertThat(text[1], is(value));
    }

    public void checkThatSearchCounterIs(String counter) {
        waitFor(ExpectedConditions.textToBePresentInElement(getDriver().findElement(By.xpath("//span[@class='section-title-label truncated-label']")), "Search Results for"));
        String[] text = getDriver().findElement(By.xpath("//*[@class='section-subtitle-label']")).getText().split(" ", 3);
        assertThat(text[1], is(counter));
    }

    public void chooseSearchOption(String option) {
        getDriver().findElement(By.cssSelector(".search-box_select")).click();
        getDriver().findElement(By.xpath("//vaadin-item[text()='" + option + "' and @role='option']")).click();
    }

    public void checkSearchResultProjectIs(String value) {
        for (WebElement webElement : getDriver().findElements(By.xpath("//*[@class='highlighted-text-wrapper'][1]"))) {
            utils.moveToElementRepeatIfStaleException(webElement);
            assertThat(webElement.getText(), containsString(value));
        }
    }

    public void checkSearchResultModelIs(String value) {
        for (WebElement webElement : getDriver().findElements(By.xpath("//*[@class='highlighted-text-wrapper'][2]"))) {
            utils.moveToElementRepeatIfStaleException(webElement);
            assertThat(webElement.getText(), containsString(value));
        }
    }

    public void checkSearchResultExperimentIs(String value) {
        for (WebElement webElement : getDriver().findElements(By.xpath("//*[@class='highlighted-text-wrapper'][3]"))) {
            utils.moveToElementRepeatIfStaleException(webElement);
            assertThat(webElement.getText(), containsString(value));
        }
    }

    public void checkSearchResultTagIs(String tag) {
        for (WebElement webElement : getDriver().findElements(By.xpath("//tag-label[@outline='true']"))) {
            utils.moveToElementRepeatIfStaleException(webElement);
            assertThat(webElement.getText(), containsString(tag));
        }
    }

    public void waitForSearchResultPage() {
        waitFor(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class='section-title-label truncated-label']")));
    }

    public void clickAndSendEnterBtnToTheSearchField() {
        searchBoxShadow.click();
        searchBoxShadow.sendKeys(Keys.ENTER);
    }

    public void clickUserMenuBtn(String btn) {
        getDriver().findElement(By.cssSelector(".vaadin-menu-item")).click();
    }

    public void clickRequestOnboardingServiceBtn() {
        assertThat(getDriver().findElement(By.xpath("//request-onboarding-service-button/vaadin-button")).getText(), containsString("Request Onboarding Service"));
        getDriver().findElement(By.xpath("//request-onboarding-service-button/vaadin-button")).click();
    }

    public void clickRequestOnboardingServiceBackBtn() {
        getDriver().findElement(By.xpath("//a[@title='Pathmind Inc.']")).click();
    }

    public void checkRequestOnboardingServicePage() {
        assertThat(getDriver().findElement(By.xpath("//div[@class='ProductSummary-info']/span[1]")).getText(), is("Concierge Onboarding Service"));
        assertThat(getDriver().findElement(By.xpath("//div[@class='ProductSummary-info']/span[2]")).getText(), is("$500.00"));
        assertThat(getDriver().findElement(By.xpath("//div[@class='ProductSummary-info']/span[3]")).getText(), is("A dedicated Pathmind Engineer to help retrofit your existing simulation for reinforcement learning."));
        assertThat(getDriver().findElement(By.xpath("//div[@class='PaymentHeader']/div")).getText(), is("Pay with card"));
        assertThat(getDriver().findElement(By.xpath("//dl[@class='PrefilledInfo']/div")).getText(), is("Email\nevegeniy@skymind.io"));
        assertThat(getDriver().findElement(By.xpath("//div[@class='SubmitButton-TextContainer']/span[1]")).getText(), is("Pay $500.00"));
    }

    public void fillRequestOnboardingServicePaymentForm() {
        getDriver().findElement(By.id("cardNumber")).sendKeys("4242424242424242");
        getDriver().findElement(By.id("cardExpiry")).sendKeys("1221");
        getDriver().findElement(By.id("cardCvc")).sendKeys("123");
        getDriver().findElement(By.id("billingName")).sendKeys("Evgeniy Ivanchenko");
    }

    public void clickRequestOnboardingServicePayBtn() {
        getDriver().findElement(By.xpath("//button[@type='submit']")).click();
    }

    public void checkOnboardingSuccessPage() {
        assertThat(getDriver().findElement(By.xpath("//span[@class='welcome-text']")).getText(), is("Welcome to"));
        assertThat(getDriver().findElement(By.xpath("//img[@class='logo']")).getAttribute("src"), containsString("frontend/images/pathmind-logo.svg"));
        assertThat(getDriver().findElement(By.xpath("//h3")).getText(), is("You have paid for the onboarding service"));
        assertThat(getDriver().findElement(By.xpath("//vaadin-vertical-layout/p")).getText(), is("You should have received the payment receipt email. Our Customer Success Specialist will contact you shortly. If you have any questions, do not hesitate to message us."));
    }

    public void clickSearchResultResult(String searchResult) {
        String xpath = String.format("//vaadin-vertical-layout[@class='search-result-item']/descendant::span[contains(text(), '%s')]", searchResult);
        utils.clickElementRepeatIfStaleException(By.xpath(xpath));
    }

    public void checkThatButtonUpgradeToProBtnIsShown(Boolean btnShown) {
        if (btnShown){
            assertThat(getDriver().findElements(upgradeToProBtn).size(), is(1));
            assertThat(getDriver().findElement(upgradeToProBtn).getText(), is("Upgrade to Pro"));
            WebElement e = utils.expandRootElement(getDriver().findElement(upgradeToProBtn));
            assertThat(e.findElement(By.cssSelector("a")).getAttribute("href"), containsString("/account/upgrade"));
        }else {
            assertThat(getDriver().findElements(upgradeToProBtn).size(), is(0));
        }
    }

    public void clickInUpgradeToProButton() {
        getDriver().findElement(upgradeToProBtn).click();
    }
}
