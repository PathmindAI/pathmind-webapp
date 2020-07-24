package io.skymind.pathmind.bddtests;

import net.serenitybdd.core.environment.EnvironmentSpecificConfiguration;
import net.serenitybdd.core.pages.PageObject;
import net.thucydides.core.util.EnvironmentVariables;
import net.thucydides.core.util.SystemEnvironmentVariables;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
            } catch (org.openqa.selenium.StaleElementReferenceException ex) {
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

    public String getTextRootElement(WebElement element) {
        String text = element.getText().trim();
        for (WebElement child : element.findElements(By.xpath("./*"))) {
            text = text.replaceFirst(child.getText(), "").trim();
        }
        return text;
    }

    public void waitForLoadingBar() {
        try {
            waitFor(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@style='display: block;' and @class='v-loading-indicator first']")));
            waitFor(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@style='display: none;' and @class='v-loading-indicator first']")));
        } catch (org.openqa.selenium.NoSuchElementException ex) {
            ex.printStackTrace();
        }
    }

    public void generateBigModelWithName(String name) throws IOException {
        List<String> srcFiles = Arrays.asList("models/problematic_models/warehouse_model_files/model.jar", "models/problematic_models/warehouse_model_files/database/db.rar", "models/problematic_models/warehouse_model_files/database/db.rar", "models/problematic_models/warehouse_model_files/database/db.rar", "models/problematic_models/warehouse_model_files/database/db.rar");
        FileOutputStream fos = new FileOutputStream("models/problematic_models/" + name);
        ZipOutputStream zipOut = new ZipOutputStream(fos);
        for (String srcFile : srcFiles) {
            File fileToZip = new File(srcFile);
            FileInputStream fis = new FileInputStream(fileToZip);
            ZipEntry zipEntry = new ZipEntry(new Date().getTime() + fileToZip.getName());
            zipOut.putNextEntry(zipEntry);

            byte[] bytes = new byte[1024];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
            fis.close();
        }
        zipOut.close();
        fos.close();
    }
}
