package io.skymind.pathmind.bddtests.stepdefinitions;

import cucumber.api.java.en.Then;
import io.skymind.pathmind.bddtests.steps.HelpPageSteps;
import net.thucydides.core.annotations.Steps;

public class HelpPageStepDefinitions {

    @Steps
    private HelpPageSteps helpPageSteps;

    @Then("^Check Converting models to support Tuples page elements$")
    public void checkConvertingModelsToSupportTuplesPageElements() {
        helpPageSteps.checkConvertingModelsToSupportTuplesPageElements();
    }
}
