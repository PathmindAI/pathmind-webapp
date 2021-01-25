package io.skymind.pathmind.bddtests.stepdefinitions;

import cucumber.api.java.en.When;
import io.skymind.pathmind.bddtests.steps.DemoProjectsSteps;
import net.thucydides.core.annotations.Steps;

public class DemoProjectsStepDefinitions {

    @Steps
    private DemoProjectsSteps demoProjectsSteps;

    @When("^Close demo projects pop-up$")
    public void closeDemoProjectsPopUp() {
        demoProjectsSteps.closeDemoProjectsPopUp();
    }

    @When("^Check that demo projects popup is shown '(.*)'$")
    public void checkThatDemoProjectsPopupIsShown(boolean shown) {
        demoProjectsSteps.checkThatDemoProjectsPopupIsShown(shown);
    }

    @When("^Check demo popup elements$")
    public void checkDemoPopupElements() {
        demoProjectsSteps.checkDemoPopupElements();
    }

    @When("^Click demo popup '(.*)' get started btn$")
    public void clickDemoPopupGetStartedBtn(String model) {
        demoProjectsSteps.clickDemoPopupGetStartedBtn(model);
    }
}
