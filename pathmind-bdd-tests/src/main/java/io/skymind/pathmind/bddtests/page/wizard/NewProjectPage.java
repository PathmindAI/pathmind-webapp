package io.skymind.pathmind.bddtests.page.wizard;

import io.skymind.pathmind.bddtests.Utils;
import net.serenitybdd.core.pages.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

public class NewProjectPage extends PageObject {

    private Utils utils;

    @FindBy(xpath = "//new-project-view")
    private WebElement newProjectViewShadow;

    private final By byInput = By.cssSelector("input");

    public void inputNameOfTheNewProject(String projectName) {
        WebElement e = utils.expandRootElement(newProjectViewShadow);
        WebElement e2 = utils.expandRootElement(e.findElement(By.cssSelector("#projectName")));
        WebElement projectNameInputField = e2.findElement(byInput);
        projectNameInputField.click();
        projectNameInputField.sendKeys(projectName);
    }

    public void clickProjectNameCreateBtn() {
        waitABit(3000);
        WebElement e = utils.expandRootElement(newProjectViewShadow);
        e.findElement(By.cssSelector("#createProject")).click();
    }

    public void checkCreateANewProjectPage() {
        WebElement e = utils.expandRootElement(newProjectViewShadow);
        assertThat(e.findElement(By.cssSelector(".welcomte-text")).getText(), containsString("Welcome to"));
        assertThat(e.findElement(By.cssSelector(".logo")).isDisplayed(), is(true));
        assertThat(e.findElement(By.cssSelector("h3")).getText(), containsString("Start a New Project!"));
        assertThat(e.findElement(By.cssSelector("h3 + p")).getText(), containsString("Projects organize your Pathmind Experiments based on your AnyLogic model"));
        
        WebElement e2 = utils.expandRootElement(e.findElement(By.cssSelector("#projectName")));
        WebElement inputField = e2.findElement(By.cssSelector("label[part='label']"));
        assertThat(inputField.getText(), containsString("Give your project a name"));

        assertThat(e.findElement(By.cssSelector("#createProject")).getText(), containsString("Create Project"));
    }

    public void checkThatErrorShown(String error) {
        waitABit(2500);
        WebElement e = utils.expandRootElement(newProjectViewShadow);
        WebElement e2 = utils.expandRootElement(e.findElement(By.cssSelector("#projectName")));
        e2.findElement(By.cssSelector("div[part='error-message']"));
        assertThat(e2.findElement(By.cssSelector("div[part='error-message']")).getText(), containsString(error));
    }

    public void checkThatNewProjectPageOpened() {
        waitABit(2500);
        assertThat(getDriver().getTitle(), containsString("Pathmind | New Project"));
        WebElement e = utils.expandRootElement(newProjectViewShadow);
        assertThat(e.findElement(By.cssSelector("h3")).getText(), is("Start a New Project!"));
    }

    public void checkWizardModelUploadBreadcrumbIsShown() {
        assertThat(getDriver().findElement(By.xpath("//span[@class='breadcrumb']")).getText(), is("Upload Model"));
    }
}
