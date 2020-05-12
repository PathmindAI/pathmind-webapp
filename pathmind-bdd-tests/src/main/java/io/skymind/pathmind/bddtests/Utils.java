package io.skymind.pathmind.bddtests;

import net.serenitybdd.core.environment.EnvironmentSpecificConfiguration;
import net.serenitybdd.core.pages.PageObject;
import net.thucydides.core.util.EnvironmentVariables;
import net.thucydides.core.util.SystemEnvironmentVariables;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import java.util.ArrayList;
import java.util.List;

public class Utils extends PageObject {
    private static final EnvironmentVariables VARIABLES = SystemEnvironmentVariables.createEnvironmentVariables();
    private static final String PATHMIND_URL = EnvironmentSpecificConfiguration.from(VARIABLES).getProperty("base.url");

    public WebElement expandRootElement(WebElement element) {
        return (WebElement) ((JavascriptExecutor) getDriver()).executeScript("return arguments[0].shadowRoot", element);
    }

    public void deleteAllCookies() {
        getDriver().manage().deleteAllCookies();
    }

    public void openPage(String url) {
        getDriver().navigate().to(PATHMIND_URL + url);
    }
    public void clickElementRepeatIfStaleException(By by) {
        int attempts = 0;
        while(attempts < 3) {
            try {
                getDriver().findElement(by).click();
                break;
            } catch(org.openqa.selenium.StaleElementReferenceException ex) {
                waitABit(2000);
            }
            attempts++;
        }
    }
    public List<String> getStringListRepeatIfStaleException(By by) {
        List<String> strings = new ArrayList<>();
        int attempts = 0;
        while(attempts < 3) {
            try {
                for(WebElement e : getDriver().findElements(by)){
                    strings.add(e.getText());
                }
                break;
            } catch(org.openqa.selenium.StaleElementReferenceException ex) {
                waitABit(2000);
            }
            attempts++;
        }
        return strings;
    }
    public String getStringRepeatIfStaleException(By by) {
        String string = null;
        int attempts = 0;
        while(attempts < 3) {
            try {
                string = getDriver().findElement(by).getText();
                break;
            } catch(org.openqa.selenium.StaleElementReferenceException ex) {
                waitABit(2000);
            }
            attempts++;
        }
        return string;
    }
}
