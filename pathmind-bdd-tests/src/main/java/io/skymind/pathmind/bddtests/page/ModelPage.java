package io.skymind.pathmind.bddtests.page;

import com.google.common.collect.Ordering;
import io.skymind.pathmind.bddtests.Utils;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.core.pages.PageObject;
import net.thucydides.core.annotations.DefaultUrl;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DefaultUrl("page:home.page")
public class ModelPage extends PageObject {

    @FindBy(xpath = "//vaadin-grid//vaadin-button[@title='Unarchive']")
    private WebElement unarchiveBtnShadow;
    @FindBy(xpath = "//*[@class='breadcrumb']")
    private List<WebElement> breadcrumb;
    @FindBy(xpath = "//vaadin-grid-cell-content")
    private List<WebElement> experimentModelsNames;

    private Utils utils;

    public void clickTheModelName(String modelName) {
        getDriver().findElement(By.xpath("//vaadin-grid-cell-content[normalize-space(text())='" + modelName + "']")).click();
    }

    public void checkModelPageModelTitlePackageNameIs(String packageName) {
        assertThat(getDriver().findElement(By.xpath("//vaadin-vertical-layout/descendant::span[@class='section-title-label project-title-label'][2]")).getText().split("[()]")[1], is(packageName));
    }

    public void checkModelPageModelDetailsActionsIs(String actions) {
        assertThat(utils.getTextRootElement(getDriver().findElement(By.xpath("//span[text()='Actions']/ancestor::p"))), is(actions));
    }

    public void checkModelPageModelDetailsObservationsIs(int observations) {
        assertThat(getDriver().findElements(By.xpath("//*[@class='observations-panel-wrapper']//span[@class='observation-label']")).size(), is(observations));
    }

    public void checkModelPageModelDetailsRewardVariablesOrder() {
        List<String> variables = new ArrayList<>();

        for (WebElement webElement : getDriver().findElements(By.xpath("//div[@class='model-reward-variables']/descendant::span[not(@class)]"))) {
            variables.add(webElement.getText());
        }

        assertThat(Ordering.natural().isOrdered(variables), is(true));
    }

    public void checkModelPageModelDetailsRewardVariablesIs(String commaSeparatedVariableNames) {
        List<String> items = Arrays.asList(commaSeparatedVariableNames.split("\\s*,\\s*"));
        List<String> actual = new ArrayList<>();
        for (WebElement webElement : getDriver().findElements(By.xpath("//*[@class='reward-variable-name']"))) {
            actual.add(webElement.getText());
        }

        assertThat(actual, containsInRelativeOrder(items.toArray()));
    }

    public void checkThatModelNameExistInArchivedTab(String experiment) {
        assertThat(utils.getStringListRepeatIfStaleException(By.xpath("//vaadin-grid-cell-content")), hasItem(experiment));
    }

    public void checkModelPageModelDetailsRewardVariableNameIs(String variableNumber, String variableName) {
        assertThat(getDriver().findElement(By.xpath("//*[@class='model-reward-variables']/descendant::span[text()='" + variableNumber + "']/following-sibling::span")).getText(), is(variableName));
    }

    public void clickTheExperimentName(String experimentName) {
        waitABit(2000);
        WebElement experiment = getDriver().findElement(By.xpath("//vaadin-grid-cell-content[text()='" + experimentName + " " + "']"));
        waitFor(ExpectedConditions.elementToBeClickable(experiment));
        experiment.click();
        waitABit(2000);
    }

    public void clickExperimentArchiveButton() {
        waitABit(2000);
        getDriver().findElement(By.xpath("//*[@class='experiment-name']/following-sibling::vaadin-button")).click();
    }

    public void clickModelPageExperimentArchiveBtn(String experiment, String archive) {
        waitABit(2000);
        WebElement e = utils.expandRootElement(getDriver().findElement(By.xpath("//vaadin-grid-cell-content[text()='" + experiment + "']/following-sibling::vaadin-grid-cell-content[6]/descendant::vaadin-button[@title='" + archive + "']")));
        e.findElement(By.cssSelector("button")).click();
    }

    public void clickExperimentUnArchiveButton() {
        waitABit(2000);
        WebElement e = utils.expandRootElement(unarchiveBtnShadow);
        e.findElement(By.cssSelector("button")).click();
    }

    public void checkThatModelsPageOpened() {
        assertThat(getDriver().getCurrentUrl(), containsString("/model/"));
    }

    public void clickProjectPageNewExperimentButton() {
        getDriver().findElement(By.xpath("//vaadin-button[text()='New Experiment']")).click();
    }

    public void checkModelPageElements() {
        assertThat(getDriver().findElement(By.xpath("//vaadin-button[@class='action-button'][1]")).getAttribute("title"), is("Archive"));
        List<String> strings = new ArrayList<>();
        for (WebElement e : breadcrumb) {
            strings.add(e.getText());
        }
        assertThat(strings, hasItem("Projects"));
        assertThat(strings, hasItem("AutotestProject" + Serenity.sessionVariableCalled("randomNumber")));
        assertThat(strings, hasItem("Model #1 (coffeeshop)"));
    }

    public void checkExperimentModelStatusIsStarting(String experiment, String status) {
        assertThat(getDriver().findElement(By.xpath("//vaadin-grid-cell-content[text()='" + experiment + " ']/following-sibling::vaadin-grid-cell-content[2]/descendant::status-icon")).getAttribute("status-text"), is(status));
        assertThat(getDriver().findElement(By.xpath("//vaadin-grid-cell-content[text()='" + experiment + " ']/following-sibling::vaadin-grid-cell-content[2]/descendant::status-icon")).getAttribute("status"), is("loading"));
        assertThat(getDriver().findElement(By.xpath("//vaadin-grid-cell-content[text()='" + experiment + " ']/following-sibling::vaadin-grid-cell-content[2]/descendant::status-icon")).getAttribute("title"), is(status));
    }

    public void checkOnTheModelPageExperimentNotesIs(String experiment, String note) {
        waitABit(3000);
        assertThat(utils.getStringRepeatIfStaleException(By.xpath("//vaadin-grid-cell-content[text()='" + experiment + " ']/following-sibling::vaadin-grid-cell-content[5]")), is(note));
    }

    public void checkModelPageModelBreadcrumbPackageNameIs(String packageName) {
        assertThat(getDriver().findElement(By.xpath("//*[contains(text(),'Model') and @class='breadcrumb']")).getText(), is("Model #1 (" + packageName + ")"));
    }

    public void clickModelPageExperimentStarButton(String experiment) {
        waitABit(3500);
        WebElement favoriteStarShadow = utils.expandRootElement(getDriver().findElement(By.xpath("//vaadin-grid-cell-content[text()='" + experiment + "']/preceding-sibling::vaadin-grid-cell-content[1]/descendant::favorite-star")));
        waitFor(ExpectedConditions.elementToBeClickable(favoriteStarShadow.findElement(By.cssSelector("vaadin-button"))));
        favoriteStarShadow.findElement(By.cssSelector("vaadin-button")).click();
    }

    public void checkModelPageExperimentIsFavoriteTrue(String experiment, Boolean favoriteStatus) {
        waitABit(3500);
        WebElement favoriteStarShadow = utils.expandRootElement(getDriver().findElement(By.xpath("//vaadin-grid-cell-content[text()='" + experiment + "']/preceding-sibling::vaadin-grid-cell-content[1]/descendant::favorite-star")));
        waitFor(ExpectedConditions.elementToBeClickable(favoriteStarShadow.findElement(By.cssSelector("vaadin-button"))));
        if (favoriteStatus) {
            assertThat(favoriteStarShadow.findElement(By.cssSelector("iron-icon")).getAttribute("icon"), is("vaadin:star"));
        } else {
            assertThat(favoriteStarShadow.findElement(By.cssSelector("iron-icon")).getAttribute("icon"), is("vaadin:star-o"));
        }
    }

    public void clickModelPageModelArchiveButton() {
        getDriver().findElement(By.xpath("//span[@class='section-title-label']/following-sibling::vaadin-button")).click();
    }

    public void checkModelPageModelArchivedTagIsShown(Boolean archived) {
        if (archived) {
            waitFor(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class='section-subtitle-label']/following-sibling::tag-label")));
            assertThat(getDriver().findElements(By.xpath("//span[@class='section-subtitle-label']/following-sibling::tag-label")).size(), is(1));
            assertThat(getDriver().findElement(By.xpath("//span[@class='section-subtitle-label']/following-sibling::tag-label")).getText(), is("Archived"));

        } else {
            setImplicitTimeout(3, SECONDS);
            waitFor(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//span[@class='section-subtitle-label']/following-sibling::tag-label")));
            assertThat(getDriver().findElement(By.xpath("//span[@class='section-subtitle-label']/following-sibling::tag-label")).getAttribute("hidden"), is("true"));
            resetImplicitTimeout();
        }
    }

    public void checkModelTitleLabelTagIsArchived(String tag) {
        assertThat(getDriver().findElement(By.xpath("//*[@class='model-wrapper']/descendant::span[@class='section-subtitle-label']/following-sibling::tag-label")).getText(), is(tag));
    }

    public void checkSideBarModelsDateIs(String model, String date) {
        assertThat(utils.getModelNavbarItemByModelName(model, "a > div > p:nth-child(3)").getText(), is(date));
    }

    public void checkModelPageCreatedIs(String model, String date) {
        String projectCellNumber = getDriver().findElement(By.xpath("//vaadin-grid-cell-content[text()='" + model + " ']")).getAttribute("slot").split("vaadin-grid-cell-content-")[1];
        int cellNumber = Integer.parseInt(projectCellNumber) + 1;
        assertThat(getDriver().findElement(By.xpath("//vaadin-grid-cell-content[@slot='vaadin-grid-cell-content-" + cellNumber + "']/descendant::datetime-display")).getText(), is(date));
    }

    public void addModelNoteToTheProjectPage(String note) {
        WebElement notesShadow = utils.expandRootElement(getDriver().findElement(By.xpath("(//notes-field)[2]")));
        notesShadow.findElement(By.cssSelector("#textarea")).click();
        notesShadow.findElement(By.cssSelector("#textarea")).sendKeys(note);
        waitABit(3000);
    }

    public void checkModelNoteOnTheProjectPage(String note) {
        WebElement notesShadow = utils.expandRootElement(getDriver().findElement(By.xpath("(//notes-field)[2]")));
        assertThat(notesShadow.findElement(By.cssSelector("#textarea")).getAttribute("value"), is(note));
    }

    public void checkModelPageExperimentNameSelectedObservationsIs(String experiment, String observations) {
        assertThat(getDriver().findElement(By.xpath("//vaadin-grid-cell-content[text()='"+experiment+" ']/following-sibling::vaadin-grid-cell-content[3]")).getText().replaceAll(" ",""), is(observations));
    }
}
