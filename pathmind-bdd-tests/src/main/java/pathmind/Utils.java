package pathmind;

import net.serenitybdd.core.environment.EnvironmentSpecificConfiguration;
import net.serenitybdd.core.pages.PageObject;
import net.thucydides.core.util.EnvironmentVariables;
import net.thucydides.core.util.SystemEnvironmentVariables;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

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

}
