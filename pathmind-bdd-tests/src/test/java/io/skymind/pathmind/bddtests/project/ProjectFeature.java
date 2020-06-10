package io.skymind.pathmind.bddtests.project;

import cucumber.api.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
    plugin = {"pretty", "html:target/cucumber"},
    glue = "io.skymind.pathmind.bddtests.stepdefinitions",
    features = "src/test/resources/features/project/project.feature"
)
public class ProjectFeature {
}
