package pathmind.CapabilityEnhancer;

import net.serenitybdd.core.webdriver.enhancers.BeforeAWebdriverScenario;
import net.thucydides.core.model.TestOutcome;
import net.thucydides.core.util.EnvironmentVariables;
import net.thucydides.core.webdriver.SupportedWebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public class MyCapabilityEnhancer implements BeforeAWebdriverScenario {

	@Override
	public DesiredCapabilities apply(EnvironmentVariables environmentVariables,
									 SupportedWebDriver driver,
									 TestOutcome testOutcome,
									 DesiredCapabilities capabilities) {
		capabilities.setCapability("name", testOutcome.getStoryTitle()
//				+ " - "
//				+ testOutcome.getTitle()
		);
		return capabilities;
	}
}