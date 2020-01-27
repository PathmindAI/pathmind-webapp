package pathmind;

import net.serenitybdd.core.pages.PageObject;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

public class Utils extends PageObject {
    public WebElement expandRootElement(WebElement element) {
        WebElement ele = (WebElement) ((JavascriptExecutor) getDriver()).executeScript("return arguments[0].shadowRoot", element);
        return ele;
    }
}
