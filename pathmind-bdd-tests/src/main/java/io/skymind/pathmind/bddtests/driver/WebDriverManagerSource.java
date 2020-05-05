package io.skymind.pathmind.bddtests.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import net.thucydides.core.util.EnvironmentVariables;
import net.thucydides.core.util.SystemEnvironmentVariables;
import net.thucydides.core.webdriver.DriverSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;

public class WebDriverManagerSource implements DriverSource {

    @Override
    public WebDriver newDriver() {
        EnvironmentVariables variables = SystemEnvironmentVariables.createEnvironmentVariables();
        String _headless = variables.getProperty("headless");
        WebDriver _driver;
        String _browserName = SystemEnvironmentVariables.createEnvironmentVariables().getProperty("browser", DriverType.CHROME.toString()).toUpperCase();
        String chromeVersion = variables.getProperty("chrome.version");
        DriverType _driverType = DriverType.valueOf(_browserName);
        switch (_driverType) {
            case CHROME:
                WebDriverManager.chromedriver().version(chromeVersion).setup();
                ChromeOptions options = new ChromeOptions();
                if (_headless.equals("true")){
                    options.addArguments("--headless");
                }
                options.addArguments("--remote-debugging-port=9222");
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-dev-shm-usage");
				options.addArguments("--verbose");
                _driver = new ChromeDriver(options);
                break;
            case FIREFOX:
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (_headless.equals("true")){
                    firefoxOptions.addArguments("--headless");
                }
                _driver = new FirefoxDriver(firefoxOptions);
                break;
            case IE:
                WebDriverManager.iedriver().setup();
                _driver = new InternetExplorerDriver();
                break;
            default:
                WebDriverManager.chromedriver().setup();
                _driver = new ChromeDriver();
                break;
        }
        return _driver;
    }

    @Override
    public boolean takesScreenshots() {
        return true;
    }
}
