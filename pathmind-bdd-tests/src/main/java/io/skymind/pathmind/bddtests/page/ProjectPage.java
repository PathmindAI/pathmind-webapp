package io.skymind.pathmind.bddtests.page;

import io.skymind.pathmind.bddtests.Utils;
import net.serenitybdd.core.pages.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

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
        WebElement archiveBtn = getDriver().findElement(By.xpath("//vaadin-grid-cell-content[text()='" + model + " " + "']/following-sibling::vaadin-grid-cell-content[6]/descendant::vaadin-button"));
        archiveBtn.click();
    }

    public void clickModelsTab() {
        getDriver().findElement(By.xpath("//vaadin-tab[text()='Models']")).click();
    }

    public void checkThatProjectNameDetailsOnProjectPage(String name) {
        waitABit(3500);
        assertThat(getDriver().findElement(By.xpath("//span[@class='section-title-label']")).getText(), is(name));
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
        waitFor(ExpectedConditions.urlContains("/project/"));
    }

    public void archiveModelWithPackageNameFromLeftSidebar(String modelId, String packageName) {
        List<WebElement> e = getDriver().findElements(By.xpath("//models-navbar-item"));
        for (WebElement webElement : e) {
            String modelNumber = webElement.getText().split("#")[1].split(" ")[0];
            String modelName = webElement.getText().split("\\(")[1].split("\\)")[0];
            if (modelNumber.equals(modelId) | modelName.equals(packageName)) {
                WebElement shadow = utils.expandRootElement(webElement);
                WebElement button = utils.expandRootElement(shadow.findElement(By.cssSelector("vaadin-button:not([hidden])")));
                button.findElement(By.cssSelector("button")).click();
                waitABit(3000);
                break;
            }
        }
    }

    public void clickModelFromLeftSidebar(String modelId) {
        List<WebElement> e = getDriver().findElements(By.xpath("//models-navbar-item"));
        for (WebElement webElement : e) {
            String modelNumber = webElement.getText().split("#")[1].split(" ")[0];
            if (modelNumber.equals(modelId)) {
                webElement.click();
                waitABit(3500);
                break;
            }
        }
    }

    public void changeModelsSidebarListTo(String modelsList) {
        utils.clickElementRepeatIfStaleException(By.xpath("//vaadin-select[@theme='models-nav-bar-select small']"));
        utils.clickElementRepeatIfStaleException(By.xpath("//vaadin-list-box/vaadin-item[text()='" + modelsList + "']"));
    }

    public void checkThatModelsSidebarModelContainsDraftTagFalse(String model, Boolean draft) {
        setImplicitTimeout(5, SECONDS);
        List<WebElement> e = getDriver().findElements(By.xpath("//models-navbar-item"));
        for (WebElement webElement : e) {
            String modelNumber = webElement.getText().split("#")[1].split(" ")[0];
            if (modelNumber.equals(model)) {
                WebElement tag = utils.expandRootElement(webElement);
                if (draft) {
                    assertThat(tag.findElements(By.cssSelector("tag-label:not([hidden])")).size(), is(1));
                } else {
                    assertThat(tag.findElements(By.cssSelector("tag-label[hidden]")).size(), is(1));
                }
            }
        }
    }

    public void checkProjectTitleLabelTagIsArchived(String tag) {
        assertThat(getDriver().findElement(By.xpath("(//*[@class='page-content-header'])[1]/descendant::span[@class='section-subtitle-label']/following-sibling::tag-label")).getText(), is(tag));
    }

    public void checkThatProjectPageTitleIs(String title) {
        assertThat(getDriver().getTitle(), is(title));
    }

    public void clickProjectPageMetricDropdown(String dropdown, String value) {
        String className;
        if (dropdown.equals("metric")) {
            className = "metric-selection-row";
        } else {
            className = "column-selection-row";
        }
        WebElement e = utils.expandRootElement(getDriver().findElement(By.xpath("//*[@class='" + className + "']/multiselect-combo-box")));
        WebElement multiSelect = utils.expandRootElement(e.findElement(By.cssSelector("multiselect-combo-box-input")));
        multiSelect.findElement(By.cssSelector("#toggleButton")).click();

        WebElement overlay = utils.expandRootElement(getDriver().findElement(By.xpath("//vaadin-combo-box-overlay")));
        WebElement content = utils.expandRootElement(overlay.findElement(By.cssSelector("#content")));

        List<WebElement> item = content.findElements(By.cssSelector("vaadin-combo-box-item"));
        for (WebElement webElement : item) {
            WebElement itemText = utils.expandRootElement(webElement);
            System.out.println(itemText.findElement(By.cssSelector("#content")).getText());
            if (itemText.findElement(By.cssSelector("#content")).getText().equals(value)) {
                itemText.findElement(By.cssSelector("#content")).click();
                break;
            }
        }
        getDriver().findElement(By.xpath("//body")).sendKeys(Keys.ESCAPE);
    }

    public void checkProjectPageDropdown(String dropdown, String value) {
        String className;
        if (dropdown.equals("metric")) {
            className = "metric-selection-row";
        } else {
            className = "column-selection-row";
        }
        WebElement e = utils.expandRootElement(getDriver().findElement(By.xpath("//*[@class='" + className + "']/multiselect-combo-box")));
        WebElement multiSelect = utils.expandRootElement(e.findElement(By.cssSelector("multiselect-combo-box-input")));
        List<String> actual = new ArrayList<>();
        waitABit(4000);
        for (WebElement webElement : multiSelect.findElements(By.cssSelector("div[part='token-label']"))) {
            actual.add(webElement.getText());        }
        assertThat(actual, hasItem(value));
    }
}
