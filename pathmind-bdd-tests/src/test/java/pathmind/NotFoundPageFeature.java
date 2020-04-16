package pathmind;

import cucumber.api.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        plugin = {"pretty", "html:target/cucumber"},
        glue = "pathmind.stepDefinitions",
        features = "src/test/resources/features/notFoundPage.feature"
)
public class NotFoundPageFeature {
}