package io.skymind.pathmind.bddtests.page;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import io.skymind.pathmind.bddtests.Utils;
import net.serenitybdd.core.pages.PageObject;
import net.thucydides.core.annotations.DefaultUrl;

@DefaultUrl("page:home.page")
public class ProjectsPage extends PageObject {

    private Utils utils;

    @FindBy(xpath = "//vaadin-text-area[1]")
    private WebElement experimentNotesField;

    private final By byInput = By.cssSelector("input");
    private final By byTextarea = By.cssSelector("textarea");

    public void clickCreateNewProjectBtn() {
        waitABit(2000);
        getDriver().findElement(By.xpath("//vaadin-button[text()='New Project']")).click();
    }

    public void checkThatProjectExistInProjectsList(String projectName) {
        utils.moveToElementRepeatIfStaleException(By.xpath("//span[text()='" + projectName + "']/ancestor::vaadin-grid-cell-content"));
        assertThat(utils.getStringListRepeatIfStaleException(By.xpath("//*[@class='project-name-column']/descendant::span")), hasItem(projectName));
    }

    public void inputExperimentNotes(String notes) {
        experimentNotesField.click();
        experimentNotesField.sendKeys(notes);
    }

    public void clickProjectsArchiveButton(String projectName) {
        waitABit(2000);
        getDriver().findElement(By.xpath("//span[text()='" + projectName + "']/ancestor::vaadin-grid-cell-content/following-sibling::vaadin-grid-cell-content[4]/descendant::vaadin-button")).click();
    }

    public void clickProjectsTab() {
        getDriver().findElement(By.xpath("//vaadin-tab[text()='Active']")).click();
    }

    public void checkThatProjectNotExistInProjectList(String projectName) {
        assertThat(utils.getStringListRepeatIfStaleException(By.xpath("//*[@class='project-name-column']/descendant::span")), not(hasItem(projectName)));
    }

    public void openProjectOnProjectsPage(String projectName) {
        waitABit(2500);
        WebElement project = getDriver().findElement(By.xpath("//span[text()='" + projectName + "']/ancestor::vaadin-grid-cell-content"));
        waitFor(ExpectedConditions.elementToBeClickable(project));
        JavascriptExecutor executor = (JavascriptExecutor) getDriver();
        executor.executeScript("arguments[0].click();", project);
    }

    public void clickEditProjectIconFromProjectsPage(String projectName) {
        waitABit(3000);
        WebElement project = getDriver().findElement(By.xpath("//span[text()='" + projectName + "']/ancestor::vaadin-grid-cell-content"));
        Actions actions = new Actions(getDriver());
        actions.moveToElement(project);
        actions.perform();
        WebElement editProjectBtn = getDriver().findElement(By.xpath("//span[text()='" + projectName + "']/ancestor::vaadin-horizontal-layout/descendant::iron-icon[@icon='vaadin:edit']"));
        actions.moveToElement(editProjectBtn);
        actions.click();
        actions.perform();
        waitFor(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class='section-title-label' and text()='Rename project']")));
    }

    public void checkPageTitleIsProjects(String title) {
        assertThat(getDriver().findElement(By.xpath("//span[contains(@class,'section-title-label')]")).getText(), is(title));
    }
}