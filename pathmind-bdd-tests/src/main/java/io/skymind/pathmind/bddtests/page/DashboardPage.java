package io.skymind.pathmind.bddtests.page;

import java.util.List;

import io.skymind.pathmind.bddtests.Utils;
import net.serenitybdd.core.pages.PageObject;
import net.thucydides.core.annotations.DefaultUrl;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

@DefaultUrl("page:home.page")
public class DashboardPage extends PageObject {

    private Utils utils;

    public void clickProjectFromDashboard(String randomNumber) {
        getDriver().findElement(By.xpath("//*[@class='breadcrumb' and text()='" + randomNumber + "']")).click();
        waitABit(2500);
    }

    public void clickModelBreadcrumbFromDashboard(String projectName) {
        utils.clickElementRepeatIfStaleException(By.xpath("//*[@class='breadcrumb' and text()='" + projectName + "']/following-sibling::a[contains(text(),'Model #1')]"));
        waitABit(2500);
    }

    public void clickExperimentBreadcrumbFromDashboard(String projectName) {
        waitFor(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@style='display: none;']")));
        getDriver().findElement(By.xpath("//a[text()='" + projectName + "']/ancestor::vaadin-grid-cell-content")).click();
        waitABit(2500);
    }

    public void checkStageStatus(String projectName, String stage, String stageStatus) {
        waitABit(5000);
        try {
            WebElement stageElement = getDriver().findElement(By.xpath("//*[@class='breadcrumb' and text()='" + projectName + "']/parent::vaadin-horizontal-layout/parent::vaadin-vertical-layout/descendant::span[text()='" + stage + "']"));
            assertThat(stageElement.getAttribute("class"), containsString(stageStatus));
        } catch (Exception e) {
            waitABit(5000);
        }
    }

    public void checkExperimentNotesNotExist(String projectName) {
        List<WebElement> notesElementList = getDriver().findElements(By.xpath("//*[@class='breadcrumb' and text()='" + projectName + "']/parent::vaadin-horizontal-layout/parent::vaadin-vertical-layout/following-sibling::*[@class='dashboard-item-notes']/span[text()='Experiment notes']"));
        assertThat(notesElementList.size(), is(0));
    }

    public void checkExperimentNotes(String projectName, String experimentNotes) {
        try {
            waitABit(5000);
            WebElement notesElement = getDriver().findElement(By.xpath("//*[@class='breadcrumb' and text()='" + projectName + "']/parent::vaadin-horizontal-layout/parent::vaadin-vertical-layout/following-sibling::*[@class='dashboard-item-notes']//p"));
            JavascriptExecutor executor = (JavascriptExecutor) getDriver();
            executor.executeScript("arguments[0].scrollIntoView()", notesElement);
            assertThat(notesElement.getText(), containsString(experimentNotes));
        } catch (Exception e) {
            waitABit(5000);
        }
    }

    public void clickDashboardItem(String projectName) {
        String xpath = dashboardLineXPathPrefix(projectName, "Model #1");
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
        WebElement itemIcon = getDriver().findElement(By.xpath("//span[text()='" + projectName + "']/ancestor::vaadin-horizontal-layout/descendant::vaadin-button"));
        waitFor(ExpectedConditions.elementToBeClickable(itemIcon));
        JavascriptExecutor executor = (JavascriptExecutor) getDriver();
        executor.executeScript("arguments[0].scrollIntoView()", itemIcon);
        executor.executeScript("arguments[0].click();", itemIcon);
    }

    public void clickArchiveBtn() {
        getDriver().findElement(By.xpath("//vaadin-context-menu-item[text()='Archive']")).click();
    }

    public void checkDashboardBeginScreenElements() {
        WebElement emptyDashboardElement = getDriver().findElement(By.xpath("//empty-dashboard-placeholder"));
        assertThat(emptyDashboardElement.findElement(By.cssSelector(".welcome-text")).getText(), is("Welcome to"));
        assertThat(emptyDashboardElement.findElement(By.cssSelector(".logo")).isDisplayed(), is(true));
        assertThat(emptyDashboardElement.findElement(By.cssSelector(".logo")).getAttribute("src"), containsString("frontend/images/pathmind-logo.svg"));
        assertThat(emptyDashboardElement.findElement(By.cssSelector("h3")).getText(), is("Using AI may be easier than you think:"));
        assertThat(emptyDashboardElement.findElement(By.cssSelector("ul")).getText(), is("Upload a zip file of the simulation model to Pathmind. Download zip file\nWrite a reward function (It's simple, just copy and past this: reward = after.goalReached - 0.1; ).\nOnce training is complete, click on “Export Policy”.\nLoad the trained AI into AnyLogic to see it perform."));
        assertThat(emptyDashboardElement.findElement(By.cssSelector("i")).getText(), is("(For more detailed information, please see our tutorial.)"));
        assertThat(emptyDashboardElement.findElement(By.cssSelector("li:nth-child(1) a")).getAttribute("href"), is("https://s3.amazonaws.com/public-pathmind.com/SimpleStochasticPathmindDemo.zip"));
        assertThat(emptyDashboardElement.findElement(By.cssSelector("i a")).getAttribute("href"), is("http://help.pathmind.com/en/articles/4540076-getting-started-with-simple-stochastic"));
        assertThat(emptyDashboardElement.findElement(By.cssSelector("vaadin-horizontal-layout:nth-of-type(1) a")).getText(), is("Create Your First Project Now"));
        assertThat(emptyDashboardElement.findElement(By.cssSelector("vaadin-horizontal-layout:nth-of-type(1) a")).getAttribute("href"), containsString("newProject"));
    }

    public void clickDashboardCreateYourFirstProjectBtn() {
        WebElement element = getDriver().findElement(By.xpath("//empty-dashboard-placeholder"));
        element.findElement(By.cssSelector(".button-link")).click();
    }

    public void clickStageWriteRewardFunctionFromDashboard(String projectName) {
        waitABit(5000);
        utils.clickElementRepeatIfStaleException(By.xpath("//span[text()='" + projectName + "']/following-sibling::*[@class='stages-container']/descendant::span[text()='Write reward function']"));
    }

    public void checkDashboardModelBreadcrumb(String projectName, String packageName) {
        assertThat(getDriver().findElement(By.xpath("//*[@class='breadcrumb' and text()='" + projectName + "']/following-sibling::a[contains(text(),'Model #1')]")).getText(), is("Model #1 (" + packageName + ")"));
    }

    public void checkDashboardPageProjectIsFavoriteTrue(String projectName, String experimentName, Boolean favoriteStatus) {
        waitABit(3500);
        WebElement favoriteStarShadow = utils.expandRootElement(getDriver().findElement(By.xpath("//span[@class='breadcrumb' and text()='" + experimentName + "']/preceding-sibling::a[@class='breadcrumb' and text()='" + projectName + "']/parent::vaadin-horizontal-layout/following-sibling::favorite-star")));
        waitFor(ExpectedConditions.elementToBeClickable(favoriteStarShadow.findElement(By.cssSelector("vaadin-button"))));
        if (favoriteStatus) {
            assertThat(favoriteStarShadow.findElement(By.cssSelector("iron-icon")).getAttribute("icon"), is("vaadin:star"));
        } else {
            assertThat(favoriteStarShadow.findElement(By.cssSelector("iron-icon")).getAttribute("icon"), is("vaadin:star-o"));
        }
    }

    public void clickDashboardPageFavoriteButton(String projectName, String experimentName) {
        waitABit(3500);
        WebElement favoriteStarShadow = utils.expandRootElement(getDriver().findElement(By.xpath("//span[@class='breadcrumb' and text()='" + experimentName + "']/preceding-sibling::a[@class='breadcrumb' and text()='" + projectName + "']/parent::vaadin-horizontal-layout/following-sibling::favorite-star")));
        waitFor(ExpectedConditions.elementToBeClickable(favoriteStarShadow.findElement(By.cssSelector("vaadin-button"))));
        favoriteStarShadow.findElement(By.cssSelector("vaadin-button")).click();
    }
}
