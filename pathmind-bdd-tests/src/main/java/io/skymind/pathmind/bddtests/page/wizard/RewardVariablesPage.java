package io.skymind.pathmind.bddtests.page.wizard;

import java.util.ArrayList;
import java.util.List;

import io.skymind.pathmind.bddtests.Utils;
import net.serenitybdd.core.pages.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

public class RewardVariablesPage extends PageObject {

    private Utils utils;

    public void clickWizardRewardVariableNamesNextBtn() {
        utils.moveToElementRepeatIfStaleException(By.xpath("//p[contains(text(),'Set the goals for')]/parent::vaadin-vertical-layout/descendant::vaadin-button"));
        getDriver().findElement(By.xpath("//p[contains(text(),'Set the goals for')]/parent::vaadin-vertical-layout/descendant::vaadin-button")).click();
    }

    public void checkThatThereIsAVariableNamed(String variableName) {
        List<String> variables = new ArrayList<>();
        for (WebElement webElement : getDriver().findElements(By.xpath("//*[@class='reward-variables-table']/descendant::*[@class='reward-variable-name']"))) {
            variables.add(webElement.getText());
        }
        assertThat(variables, hasItem(variableName));
    }

    public void inputRewardVariableGoalValue(String rewardVariable, String goalSign) {
        getDriver().findElement(By.xpath("//span[text()='" + rewardVariable + "']/parent::vaadin-horizontal-layout/descendant::vaadin-select")).click();
        getDriver().findElement(By.xpath("//vaadin-item[@label='" + goalSign + "']")).click();
        getDriver().findElement(By.xpath("//vaadin-item[@label='" + goalSign + "']"));
    }

    public void checkWizardRewardVariableErrorIsShown(String variable, String error) {
        WebElement e = utils.expandRootElement(getDriver().findElement(By.xpath("//span[text()='" + variable + "']/parent::vaadin-horizontal-layout/descendant::vaadin-number-field")));
        assertThat(e.findElement(By.cssSelector("div[part='error-message']")).getText(), is(error));
    }

    public void checkWizardNextButtonIsDisabled() {
        assertThat(getDriver().findElement(By.xpath("//span[text()='Goals']/parent::vaadin-horizontal-layout/parent::vaadin-vertical-layout/descendant::vaadin-button")).getAttribute("aria-disabled"), is("true"));
    }
}
