package io.skymind.pathmind.bddtests.stepdefinitions.wizard;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.skymind.pathmind.bddtests.steps.wizard.ModelDetailsSteps;
import net.thucydides.core.annotations.Steps;

public class ModelDetailsStepDefinitions {

    @Steps
    private ModelDetailsSteps modelDetailsSteps;

    @When("^Input wizard model details (.*)$")
    public void inputModelDetails(String notes) {
        modelDetailsSteps.inputModelDetails(notes);
    }

    @When("^Click wizard model details next btn$")
    public void clickWizardModelDetailsNextBtn() {
        modelDetailsSteps.clickWizardModelDetailsNextBtn();
    }

    @Then("^Check that model details page is opened$")
    public void checkThatModelDetailsPageIsOpened() {
        modelDetailsSteps.checkThatModelDetailsPageIsOpened();
    }

    @When("^Check that model successfully uploaded$")
    public void checkThatModelSuccessfullyUploaded() {
        modelDetailsSteps.checkThatModelSuccessfullyUploaded();
    }
}
