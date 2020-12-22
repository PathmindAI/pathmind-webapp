package io.skymind.pathmind.bddtests.api;

import cucumber.api.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
    plugin = {"pretty", "html:target/cucumber"},
    glue = "io.skymind.pathmind.bddtests.stepdefinitions",
    features = "src/test/resources/features/api/api.feature",
    tags = {"@api"}
)
public class APIFeature {
}
