package io.skymind.pathmind.bddtests.page;

import io.skymind.pathmind.bddtests.Utils;
import net.serenitybdd.core.pages.PageObject;
import net.thucydides.core.annotations.DefaultUrl;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    @FindBy(xpath = "//a[text()='Learn']")
    private WebElement learnBtn;
    @FindBy(xpath = "//a[@href='dashboard']")
    private WebElement dashboardBtn;
    @FindBy(xpath = "//span[@class='breadcrumb']")
    private WebElement pageLabel;
    @FindBy(xpath = "//vaadin-menu-bar[@class='account-menu']")
    private WebElement accountMenuBtn;
    @FindBy(xpath = "//vaadin-context-menu-item[@role='menuitem' and text()='Account']")
    private WebElement accountBtn;
    @FindBy(css = ".search-box_text-field")
    private WebElement searchBoxShadow;

    public void checkNavAccLinkVisible(String name) {
        assertThat(getDriver().findElement(By.xpath("//vaadin-horizontal-layout[@id='nav-main-links']//a[2]")).getText(), is("Dashboard"));
    }

    public void openProjectsPage() {
        projectsBtn.click();
        waitFor(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@style='display: none;']")));
    }

    public void logoutFromPathmind() {
        menuBarShadow.click();
        logoutBtn.click();
    }

    public void clickLearnBtn() {
        learnBtn.click();
    }

    public void checkThatLearnPageOpened(String learnPage) {
        ArrayList<String> tabs = new ArrayList<String>(getDriver().getWindowHandles());
        getDriver().switchTo().window(tabs.get(1));
        assertThat(getDriver().getCurrentUrl(), equalTo(learnPage));
        assertThat(getDriver().getTitle(), containsString("Pathmind Knowledge Base"));
    }

    public void openDashboardPage() {
        dashboardBtn.click();
    }

    public void checkThatDashboardPageOpened() {
        waitABit(1000);
        waitFor(ExpectedConditions.attributeToBe(dashboardBtn, "highlight", ""));
        waitFor(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@style='display: none;']")));
        assertThat(getDriver().getTitle(), is("Pathmind | Dashboard"));
        assertThat(getDriver().getCurrentUrl(), containsString("/dashboard"));
    }

    public void checkThatProjectsPageOpened() {
        waitFor(ExpectedConditions.titleIs("Pathmind | Projects"));
        assertThat(getDriver().getCurrentUrl(), containsString("/projects"));
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

    public void clickGettingStartedGuideButton() {
        getDriver().findElement(By.xpath("//a[text()='Getting Started Guide']")).click();
    }

    public void checkThatProjectsButtonHighlightIs(Boolean status) {
        if (status){
            assertThat(getDriver().findElement(By.xpath("//a[text()='Projects']")).getAttribute("highlight"), is(""));
        }else {
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
        List<String> actual = new ArrayList<>();
        for (WebElement webElement : getDriver().findElements(By.xpath("//vaadin-grid-cell-content[contains(@slot, 'vaadin-grid-cell-content-')]"))) {
            actual.add(webElement.getText());
        }
        actual.subList(0, 4).clear();

        List<String> result =  IntStream.range(0, actual.size())
            .filter(n -> n % 5 == 0)
            .mapToObj(actual::get)
            .collect(Collectors.toList());

        result.subList(result.size() - 4, result.size()).clear();

        for (String string : result) {
            assertThat(string, containsString(text));
        }
    }

    public void clickNotesClearBtn() {
        WebElement e = utils.expandRootElement(searchBoxShadow);
        e.findElement(By.id("clearButton")).click();
    }

    public void checkNotesSearchFieldIs(String text) {
        assertThat(searchBoxShadow.getText(),is(text));
    }

    public void checkSearchResultPageContainsProjectName(String name) {
        assertThat(getDriver().findElement(By.xpath("//vaadin-grid-cell-content[@slot='vaadin-grid-cell-content-2']")).getText(), is(name));
    }

    public void checkSearchResultPageContainsModelName(String name) {
        assertThat(getDriver().findElement(By.xpath("//vaadin-grid-cell-content[contains(text(),'Model')]/following::vaadin-grid-cell-content[1]")).getText(), is(name));
    }

    public void clickAutotestProjectFromSearchPage(String name) {
        getDriver().findElement(By.xpath("//vaadin-grid-cell-content[text()='"+ name +"']")).click();
    }
}
