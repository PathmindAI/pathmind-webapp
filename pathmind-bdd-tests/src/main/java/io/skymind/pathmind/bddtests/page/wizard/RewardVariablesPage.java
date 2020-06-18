package io.skymind.pathmind.bddtests.page.wizard;

import net.serenitybdd.core.pages.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;

public class RewardVariablesPage extends PageObject {

    public void clickWizardRewardVariableNamesNextBtn() {
        getDriver().findElement(By.xpath("//span[text()='Reward Variable Names']/ancestor::*[@class='view-section']/descendant::vaadin-button[normalize-space(text())='Next'][2]")).click();
    }

    public void clickWizardRewardVariablesSaveDraftBtn() {
        getDriver().findElement(By.xpath("//span[text()='Reward Variable Names']/following-sibling::vaadin-button[text()='Save Draft']")).click();
    }

    public void checkThatThereIsAVariableNamed(String variableName) {
        List<String> variables = new ArrayList<>();
        for (WebElement webElement : getDriver().findElements(By.xpath("//vaadin-text-field"))) {
            variables.add(webElement.getAttribute("value"));
        }
        assertThat(variables, hasItem(variableName));
    }
}
