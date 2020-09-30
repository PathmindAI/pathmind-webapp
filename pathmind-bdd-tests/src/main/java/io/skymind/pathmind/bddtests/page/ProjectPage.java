package io.skymind.pathmind.bddtests.page;

import io.skymind.pathmind.bddtests.Utils;
import net.serenitybdd.core.pages.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

public class ProjectPage extends PageObject {

    private Utils utils;

    @FindBy(xpath = "//vaadin-button[@theme='primary']")
    private WebElement projectPageUploadBtnShadow;
    @FindBy(xpath = "//vaadin-grid")
    private WebElement projectPageModelsTable;

    public void checkThatProjectPageOpened(String projectName) {
        assertThat(getDriver().findElement(By.xpath("//a[@class='breadcrumb'][2]")).getText(), containsString(projectName));
    }

    public void checkNumberOfModelsWithDraftTag(int numberOfProjects) {
        setImplicitTimeout(5, SECONDS);
        String xpath = "//vaadin-grid-cell-content/tag-label[not(@hidden='true')]";
        assertThat(getDriver().findElements(By.xpath(xpath)).size(), is(numberOfProjects));
        resetImplicitTimeout();
    }

    public void clickUploadModelBtnFromProjectPage() {
        waitABit(2000);
        WebElement e = utils.expandRootElement(projectPageUploadBtnShadow);
        e.findElement(By.cssSelector("#button")).click();
    }

    public void projectPageCheckThatModelsCountIs(int modelsCount) {
        waitABit(2000);
        assertThat(getDriver().findElements(By.cssSelector("models-navbar-item")).size(), is(modelsCount));
    }

    public void clickModelArchiveButton(String model) {
        waitABit(4000);
        WebElement archiveBtn = getDriver().findElement(By.xpath("//vaadin-grid-cell-content[text()='" + model + " " + "']/following-sibling::vaadin-grid-cell-content[5]/descendant::vaadin-button"));
        archiveBtn.click();
    }

    public void clickModelsTab() {
        getDriver().findElement(By.xpath("//vaadin-tab[text()='Models']")).click();
    }

    public void checkThatProjectNameDetailsOnProjectPage(String name) {
        waitABit(3500);
        assertThat(getDriver().findElement(By.xpath("//span[@class='section-title-label project-title-label']")).getText(), is(name));
    }

    public void checkThatProjectNameBreadcrumbOnProjectPage(String name) {
        assertThat(getDriver().findElement(By.xpath("//vaadin-horizontal-layout[@class='page-title']/descendant::a[@class='breadcrumb' and contains(@href,'project/')]")).getText(), is(name));
    }

    public void checkProjectPageModelPackageNameIs(String modelId, String packageName) {
        waitABit(5000);
        List<WebElement> e = getDriver().findElements(By.xpath("//models-navbar-item"));
        for (WebElement webElement : e) {
            String modelNumber = webElement.getText().split("#")[1].split(" ")[0];
            String modelName = webElement.getText().split("\\(")[1].split("\\)")[0];
            if (modelNumber.equals(modelId)) {
                waitFor(ExpectedConditions.visibilityOf(webElement));
                assertThat(modelName, is(packageName));
            }
        }
    }

    public void checkProjectPageModelNotExistInList(String modelId) {
        setImplicitTimeout(5000, SECONDS);
        waitABit(5000);
        List<WebElement> e = getDriver().findElements(By.xpath("//models-navbar-item"));
        for (WebElement webElement : e) {
            String modelNumber = webElement.getText().split("#")[1].split(" ")[0];
            if (modelNumber.equals(modelId)) {
                waitFor(ExpectedConditions.invisibilityOf(webElement));
            }
        }
        resetImplicitTimeout();
    }

    public void checkThatProjectPageIsOpened() {
        assertThat(getDriver().getTitle(), is("Pathmind | Project"));
        assertThat(getDriver().getCurrentUrl(), containsString("/project/"));
    }

    public void archiveModelWithPackageNameFromLeftSidebar(String modelId, String packageName) {
        System.out.println("MODEL " + getDriver().findElement(By.xpath("//models-navbar-item")));

        List<WebElement> e = getDriver().findElements(By.xpath("//models-navbar-item"));
        for (WebElement webElement : e) {
            String modelNumber = webElement.getText().split("#")[1].split(" ")[0];
            String modelName = webElement.getText().split("\\(")[1].split("\\)")[0];
            if (modelNumber.equals(modelId) | modelName.equals(packageName)) {
                WebElement shadow = utils.expandRootElement(webElement);
                shadow.findElement(By.cssSelector("vaadin-button:not([hidden])")).click();
            }
        }
    }

    public void changeModelsSidebarListTo(String modelsList) {
        getDriver().findElement(By.xpath("//vaadin-select[@theme='models-nav-bar-select small']")).click();
        getDriver().findElement(By.xpath("//vaadin-list-box/vaadin-item[text()='" + modelsList + "']")).click();
    }
}
