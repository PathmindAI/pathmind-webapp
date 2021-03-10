package io.skymind.pathmind.bddtests.stepdefinitions;

import cucumber.api.java.en.When;
import io.skymind.pathmind.bddtests.steps.DemoProjectsSteps;
import net.thucydides.core.annotations.Steps;

public class DemoProjectsStepDefinitions {

    @Steps
    private DemoProjectsSteps demoProjectsSteps;

    @When("^Check demo list elements$")
    public void checkDemoListElements() {
        demoProjectsSteps.checkDemoListElements();
    }

    @When("^Click demo list '(.*)'$")
    public void clickDemoListItem(String model) {
        demoProjectsSteps.clickDemoListItem(model);
    }
}
