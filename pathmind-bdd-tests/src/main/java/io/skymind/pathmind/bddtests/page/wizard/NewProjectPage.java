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

    @FindBy(xpath = "//vaadin-text-field[@required]")
    private WebElement projectNameInputFieldShadow;
    @FindBy(xpath = "//vaadin-button[text()='Create Project']")
    private WebElement projectNameCreateBtn;

    private final By byInput = By.cssSelector("input");

    public void inputNameOfTheNewProject(String projectName) {
        WebElement e = utils.expandRootElement(projectNameInputFieldShadow);
        WebElement projectNameInputField = e.findElement(byInput);
        projectNameInputField.click();
        projectNameInputField.sendKeys(projectName);
    }

    public void clickProjectNameCreateBtn() {
        waitABit(3000);
        projectNameCreateBtn.click();
    }

    public void checkCreateANewProjectPage() {
        assertThat(getDriver().findElement(By.xpath("//*[@class='light-text-label']")).getText(), containsString("Welcome to"));
        assertThat(getDriver().findElement(By.xpath("//img[@class='navbar-logo']")).isDisplayed(), is(true));
        assertThat(getDriver().findElement(By.cssSelector(".section-title-label")).getText(), containsString("Start a New Project!"));
        assertThat(getDriver().findElement(By.cssSelector(".section-subtitle-label")).getText(), containsString("Projects organize your Pathmind Experiments based on your AnyLogic model"));

        WebElement e = utils.expandRootElement(projectNameInputFieldShadow);
        WebElement searchInputField = e.findElement(By.cssSelector("label[part='label']"));
        assertThat(searchInputField.getText(), containsString("Give your project a name"));

        assertThat(getDriver().findElement(By.xpath("//vaadin-button")).getText(), containsString("Create Project"));
    }

    public void checkThatErrorShown(String error) {
        waitABit(2500);
        WebElement e = utils.expandRootElement(projectNameInputFieldShadow);
        e.findElement(By.cssSelector("div[part='error-message']"));
        assertThat(e.findElement(By.cssSelector("div[part='error-message']")).getText(), containsString(error));
    }

    public void checkThatNewProjectPageOpened() {
        assertThat(getDriver().findElement(By.xpath("//span[@class='section-title-label']")).getText(), is("Start a New Project!"));
    }
}
