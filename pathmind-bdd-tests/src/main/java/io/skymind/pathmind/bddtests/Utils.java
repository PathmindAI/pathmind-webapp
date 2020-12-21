package io.skymind.pathmind.bddtests;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import net.serenitybdd.core.environment.EnvironmentSpecificConfiguration;
import net.serenitybdd.core.pages.PageObject;
import net.thucydides.core.util.EnvironmentVariables;
import net.thucydides.core.util.SystemEnvironmentVariables;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

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
        while (attempts < 5) {
            try {
                getDriver().findElement(by).click();
                break;
            } catch (StaleElementReferenceException | ElementClickInterceptedException ex) {
                getDriver().navigate().refresh();
                waitABit(5000);
            }
            attempts++;
        }
    }

    public List<String> getStringListRepeatIfStaleException(By by) {
        List<String> strings = new ArrayList<>();
        int attempts = 0;
        while (attempts < 3) {
            try {
                for (WebElement e : getDriver().findElements(by)) {
                    strings.add(e.getText().replaceAll("\n", " "));
                }
                break;
            } catch (org.openqa.selenium.StaleElementReferenceException ex) {
                waitABit(2000);
            }
            attempts++;
        }
        return strings;
    }

    public List<String> getStringListFromShadowRootRepeatIfStaleException(By rootElement, By by) {
        List<String> strings = new ArrayList<>();
        int attempts = 0;
        while (attempts < 3) {
            try {
                for (WebElement webElement : getDriver().findElements(rootElement)) {
                    WebElement experimentNavbarItemShadow = expandRootElement(webElement);
                    WebElement e = experimentNavbarItemShadow.findElement(by);
                    strings.add(e.getText().replaceAll("\n", " "));
                }
                break;
            } catch (org.openqa.selenium.StaleElementReferenceException ex) {
                waitABit(2000);
            }
            attempts++;
        }
        return strings;
    }

    public String getStringRepeatIfStaleException(By by) {
        String string = null;
        int attempts = 0;
        while (attempts < 3) {
            try {
                string = getDriver().findElement(by).getText();
                break;
            } catch (org.openqa.selenium.StaleElementReferenceException ex) {
                waitABit(2000);
            }
            attempts++;
        }
        return string;
    }

    public void moveToElementRepeatIfStaleException(By by) {
        int attempts = 0;
        while (attempts < 3) {
            try {
                Actions actions = new Actions(getDriver());
                actions.moveToElement(getDriver().findElement(by));
                actions.perform();
                break;
            } catch (org.openqa.selenium.StaleElementReferenceException ex) {
                waitABit(2000);
            }
            attempts++;
        }
    }

    public void moveToElementRepeatIfStaleException(WebElement webElement) {
        int attempts = 0;
        while (attempts < 3) {
            try {
                Actions actions = new Actions(getDriver());
                actions.moveToElement(webElement);
                actions.perform();
                break;
            } catch (org.openqa.selenium.StaleElementReferenceException ex) {
                waitABit(2000);
            }
            attempts++;
        }
    }

    public String getTextRootElement(WebElement element) {
        String text = element.getText().trim();
        for (WebElement child : element.findElements(By.xpath("./*"))) {
            text = text.replaceFirst(child.getText(), "").trim();
        }
        return text;
    }

    public void waitForLoadingBar() {
        try {
            waitFor(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='v-loading-indicator first' and @style='display: none;']")));
        }catch (org.openqa.selenium.NoSuchElementException ex){
            ex.printStackTrace();
        }
    }

    public void generateBigModelWithName(String name) throws IOException {
        RandomAccessFile f = new RandomAccessFile("models/problematic_models/" + name, "rw");
        f.setLength(1024 * 1024 * 210);
    }

    public void sendKeysCarefully(String text, WebElement locator) {
        for (int i = 0; i < text.length(); i++) {
            String myChar = String.valueOf(text.charAt(i));
            locator.sendKeys(myChar);
        }
    }

    public WebElement getExperimentNavbarItemByExperimentName(String experimentName, String cssSelector) {
        for (WebElement webElement : getDriver().findElements(By.xpath("//experiment-navbar-item"))) {
            WebElement experimentNavbarItemShadow = expandRootElement(webElement);
            if (experimentNavbarItemShadow.findElement(By.cssSelector(".experiment-name p:first-child")).getText().split("\n")[0].equals(experimentName)) {
                if (cssSelector == null) {
                    return webElement;
                }
                return experimentNavbarItemShadow.findElement(By.cssSelector(cssSelector));
            }
        }
        return null;
    }

    public WebElement getModelNavbarItemByModelName(String experimentName, String cssSelector) {
        for (WebElement webElement : getDriver().findElements(By.xpath("//models-navbar-item"))) {
            WebElement experimentNavbarItemShadow = expandRootElement(webElement);
            if (experimentNavbarItemShadow.findElement(By.cssSelector(".model-name p:nth-child(2)")).getText().contains(experimentName)) {
                if (cssSelector == null) {
                    return webElement;
                }
                return experimentNavbarItemShadow.findElement(By.cssSelector(cssSelector));
            }
        }
        return null;
    }
}
