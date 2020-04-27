package io.skymind.pathmind.bddtests.page;

import io.skymind.pathmind.bddtests.Utils;
import net.serenitybdd.core.pages.PageObject;
import net.thucydides.core.annotations.DefaultUrl;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
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
    @FindBy(xpath = "//span[@class='breadcrumb']")
	private WebElement projectPageLabel;
    @FindBy(xpath = "//vaadin-menu-bar[@class='account-menu']")
    private WebElement accountMenuBtn;
    @FindBy(xpath = "//vaadin-context-menu-item[@role='menuitem' and text()='Account']")
    private WebElement accountBtn;

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
        assertThat(pageLabel.getText(), is("Dashboard"));
        assertThat(getDriver().getTitle(), is("Pathmind | Dashboard"));
    }

    public void checkThatProjectsPageOpened() {
        waitFor(ExpectedConditions.titleIs("Pathmind | Projects"));
        assertThat(projectPageLabel.getText(), is("Projects"));
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
}
