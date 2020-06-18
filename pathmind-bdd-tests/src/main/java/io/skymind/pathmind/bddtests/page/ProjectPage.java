package io.skymind.pathmind.bddtests.page;

import io.skymind.pathmind.bddtests.Utils;
import net.serenitybdd.core.pages.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

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
        assertThat(getDriver().findElement(By.xpath("//span[@class='breadcrumb']")).getText(), containsString(projectName));
    }

    public void checkNumberOfModelsWithDraftTag(int numberOfProjects) {
        setImplicitTimeout(5, SECONDS);
        String xpath = "//vaadin-grid-cell-content/span[@class='tag' and text()='Draft']";
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
        WebElement e = utils.expandRootElement(projectPageModelsTable);
        assertThat(e.findElements(By.cssSelector("#items tr[part='row']")).size(), is(modelsCount));
    }

    public void clickModelArchiveButton(String model) {
        waitABit(4000);
        WebElement archiveBtn = getDriver().findElement(By.xpath("//vaadin-grid-cell-content[text()='" + model + " " + "']/following-sibling::vaadin-grid-cell-content[4]/descendant::vaadin-button"));
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
        assertThat(getDriver().findElement(By.xpath("//vaadin-horizontal-layout[@class='page-title']/descendant::span[@class='breadcrumb']")).getText(), is(name));
    }
}
