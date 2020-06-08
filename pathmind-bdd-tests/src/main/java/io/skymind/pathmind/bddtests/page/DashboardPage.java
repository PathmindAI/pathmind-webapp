package io.skymind.pathmind.bddtests.page;

import io.skymind.pathmind.bddtests.Utils;
import net.serenitybdd.core.pages.PageObject;
import net.thucydides.core.annotations.DefaultUrl;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

import java.util.List;

@DefaultUrl("page:home.page")
public class DashboardPage extends PageObject {

    private Utils utils;

    public void clickProjectFromDashboard(String randomNumber) {
        getDriver().findElement(By.xpath("//*[@class='breadcrumb' and text()='" + randomNumber + "']")).click();
        waitABit(2500);
    }

    public void clickModelBreadcrumbFromDashboard(String projectName) {
        utils.clickElementRepeatIfStaleException(By.xpath("//*[@class='breadcrumb' and text()='" + projectName + "']/following-sibling::a[text()='Model #1']"));
        waitABit(2500);
    }

    public void clickExperimentBreadcrumbFromDashboard(String projectName) {
        waitFor(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@style='display: none;']")));
        getDriver().findElement(By.xpath("//a[text()='" + projectName + "']/ancestor::vaadin-grid-cell-content")).click();
        waitABit(2500);
    }

    public void checkStageStatus(String projectName, String stage, String stageStatus) {
        WebElement stageElement = getDriver().findElement(By.xpath("//*[@class='breadcrumb' and text()='" + projectName + "']/parent::vaadin-horizontal-layout/parent::vaadin-vertical-layout/descendant::span[text()='" + stage + "']"));
        assertThat(stageElement.getAttribute("class"), containsString(stageStatus));
    }

    public void checkExperimentNotesNotExist(String projectName) {
        List<WebElement> notesElementList = getDriver().findElements(By.xpath("//*[@class='breadcrumb' and text()='" + projectName + "']/parent::vaadin-horizontal-layout/parent::vaadin-vertical-layout/following-sibling::*[@class='dashboard-item-notes']/span[text()='Experiment notes']"));
        assertThat(notesElementList.size(), is(0));
    }

    public void checkExperimentNotes(String projectName, String experimentNotes) {
        WebElement notesElement = getDriver().findElement(By.xpath("//*[@class='breadcrumb' and text()='" + projectName + "']/parent::vaadin-horizontal-layout/parent::vaadin-vertical-layout/following-sibling::*[@class='dashboard-item-notes']//p"));
        assertThat(notesElement.getText(), containsString(experimentNotes));
    }

    public void clickInNavigationIcon(String projectName) {
        String xpath = dashboardLineXPathPrefix(projectName, "Model #1") + "//*[@class='navigate-icon']";
        getDriver().findElement(By.xpath(xpath)).click();
        waitABit(2500);
    }

    private String dashboardLineXPathPrefix(String projectName, String modelName) {
        return String.format("//*[@class='breadcrumb' and text()='%s']/following-sibling::span[text()='%s']/ancestor::*[@class='dashboard-line']",
            projectName, modelName);
    }

    public void clickInAutotestProjectStageBreadcrumb(String projectName) {
        String xpath = dashboardLineXPathPrefix(projectName, "Model #1") + "//*[@class='stages-container']";
        getDriver().findElement(By.xpath(xpath)).click();
        waitABit(2500);
    }

    public void clickDashItemIcons(String projectName) {
        getDriver().findElement(By.xpath("//span[text()='" + projectName + "']/ancestor::vaadin-horizontal-layout/descendant::vaadin-button")).click();
    }

    public void clickArchiveBtn() {
        getDriver().findElement(By.xpath("//vaadin-context-menu-item[text()='Archive']")).click();
    }

    public void checkDashboardBeginScreenElements() {
        assertThat(getDriver().findElement(By.xpath("//span[@class='light-text-label']")).getText(), is("Welcome to"));
        assertThat(getDriver().findElement(By.xpath("//img[@class='navbar-logo']")).isDisplayed(), is(true));
        assertThat(getDriver().findElement(By.xpath("//img[@class='navbar-logo']")).getAttribute("src"), containsString("frontend/images/pathmind-logo.png"));
        assertThat(getDriver().findElement(By.xpath("//span[@class='section-title-label']")).getText(), is("Let's begin by opening the"));
        assertThat(getDriver().findElement(By.xpath("//a[@class='button-link']")).getText(), is("Getting Started Guide"));
        assertThat(getDriver().findElement(By.xpath("//a[@class='button-link']")).getAttribute("href"), is("https://help.pathmind.com/en/articles/4004788-getting-started"));
        assertThat(getDriver().findElement(By.xpath("//vaadin-horizontal-layout[@theme='spacing'][2]/span")).getText(), is("or skip ahead to"));
        assertThat(getDriver().findElement(By.xpath("//vaadin-horizontal-layout[@theme='spacing'][2]/a")).getText(), is("create your first project."));
        assertThat(getDriver().findElement(By.xpath("//vaadin-horizontal-layout[@theme='spacing'][2]/a")).getAttribute("href"), containsString("newProject"));
    }

    public void clickDashboardCreateYourFirstProjectBtn() {
        getDriver().findElement(By.xpath("//a[text()='create your first project.']")).click();
    }

    public void clickStageWriteRewardFunctionFromDashboard(String projectName) {
        getDriver().findElement(By.xpath("//span[text()='" + projectName + "']/following-sibling::*[@class='stages-container']/descendant::span[text()='Write reward function']")).click();
    }
}
