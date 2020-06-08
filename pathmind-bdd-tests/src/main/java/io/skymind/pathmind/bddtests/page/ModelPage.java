package io.skymind.pathmind.bddtests.page;

import com.google.common.collect.Ordering;
import io.skymind.pathmind.bddtests.Utils;
import net.serenitybdd.core.pages.PageObject;
import net.thucydides.core.annotations.DefaultUrl;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DefaultUrl("page:home.page")
public class ModelPage extends PageObject {

    private Utils utils;

    public void clickTheModelName(String modelName) {
        getDriver().findElement(By.xpath("//vaadin-grid-cell-content[normalize-space(text())='" + modelName + "']")).click();
    }

    public void checkModelPageModelDetailsPackageNameIs(String packageName) {
        assertThat(utils.getTextRootElement(getDriver().findElement(By.xpath("//span[text()='Package Name']/ancestor::p"))), is(packageName));
    }

    public void checkModelPageModelDetailsActionsIs(String actions) {
        assertThat(utils.getTextRootElement(getDriver().findElement(By.xpath("//span[text()='Actions']/ancestor::p"))), is(actions));
    }

    public void checkModelPageModelDetailsObservationsIs(String observations) {
        assertThat(utils.getTextRootElement(getDriver().findElement(By.xpath("//span[text()='Observations']/ancestor::p"))), is(observations));
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
        for (WebElement webElement : getDriver().findElements(By.xpath("//div[@class='model-reward-variables']/descendant::span[@class]"))) {
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
}