package io.skymind.pathmind.bddtests.dashboard;

import cucumber.api.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        plugin = {"pretty", "html:target/cucumber"},
        glue = "io.skymind.pathmind.bddtests.stepdefinitions",
        features = "src/test/resources/features/dashboard/dashboardBreadcrumbs.feature"
)
public class DashboardBreadcrumbsFeature {
}
