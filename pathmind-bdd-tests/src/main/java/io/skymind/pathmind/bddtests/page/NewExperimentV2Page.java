package io.skymind.pathmind.bddtests.page;

import io.skymind.pathmind.bddtests.Utils;
import net.serenitybdd.core.pages.PageObject;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;

import static java.time.temporal.ChronoUnit.SECONDS;

public class NewExperimentV2Page extends PageObject {

    private Utils utils;

    @FindBy(xpath = "//*[@class='reward-terms-wrapper']/descendant::sortable-row-wrapper[last()]/descendant::vaadin-select[1]")
    private WebElement rewardVariableLocator;

    public void clickRewardTermBtn() {
        getDriver().findElement(By.xpath("//vaadin-button[text()='Reward Term']")).click();
        waitABit(2500);
    }

    public void chooseRewardVar(String rewardVariable, String goal, String weight) {
        getDriver().findElement(By.xpath("//*[@class='reward-terms-wrapper']/descendant::sortable-row-wrapper[last()]/descendant::vaadin-select[1]")).click();
        waitABit(2500);
        getDriver().findElement(By.xpath("//vaadin-item[@label='" + rewardVariable + "']")).click();
        getDriver().findElement(By.xpath("//*[@class='reward-terms-wrapper']/descendant::sortable-row-wrapper[last()]/descendant::vaadin-select[2]")).click();
        waitABit(2500);
        getDriver().findElement(By.xpath("//vaadin-item[@label='" + goal + "']")).click();
        getDriver().findElement(By.xpath("//*[@class='reward-terms-wrapper']/descendant::sortable-row-wrapper[last()]/descendant::vaadin-number-field")).click();
        WebElement e = utils.expandRootElement(getDriver().findElement(By.xpath("//*[@class='reward-terms-wrapper']/descendant::sortable-row-wrapper[last()]/descendant::vaadin-number-field")));
        e.findElement(By.cssSelector("input")).clear();
        e.findElement(By.cssSelector("input")).sendKeys(weight);
    }

    public void switchToRewardTermsBeta() {
        getDriver().findElement(By.xpath("//toggle-button")).click();
    }

    public void enableBetaFeature() {
        setImplicitTimeout(3, SECONDS);
        if(getDriver().findElements(By.xpath("//*[@class='beta-feature-banner']")).size() != 0){
            String currentUrl = getDriver().getCurrentUrl();
            getDriver().findElement(By.xpath("//*[@class='beta-feature-banner']")).click();
            getDriver().findElement(By.xpath("//vaadin-button[text()='Enable']")).click();
            WebElement e = utils.expandRootElement(getDriver().findElement(By.cssSelector("confirm-popup")));
            e.findElement(By.cssSelector("#confirm")).click();
            getDriver().navigate().to(currentUrl);
        }
        resetImplicitTimeout();
    }
}
