package io.skymind.pathmind.bddtests.stepdefinitions.wizard;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.skymind.pathmind.bddtests.steps.wizard.ModelDetailsSteps;
import io.skymind.pathmind.bddtests.steps.wizard.ModelUploadSteps;
import net.thucydides.core.annotations.Steps;

public class ModelUploadStepDefinitions {

    @Steps
    ModelUploadSteps modelUploadSteps;
    @Steps
    ModelDetailsSteps modelDetailsSteps;

    @When("^Upload model (.*)$")
    public void uploadModelFile(String model) {
        modelUploadSteps.uploadModelFile(model);
    }

    @Then("^Check that model upload page opened$")
    public void checkThatModelUploadPageOpened() {
        modelUploadSteps.checkThatModelUploadPageOpened();
    }

    @Then("^Check that error message in model check panel is \"(.*)\"$")
    public void checkErrorMessage(String errorMessage) {
        modelUploadSteps.checkErrorMessageInModelCheckPanel(errorMessage);
    }

    @When("^Upload ALP file '(.*)'$")
    public void uploadALPFile(String alpFile) {
        modelUploadSteps.uploadALPFile(alpFile);
    }

    @When("^Click wizard upload ALP next btn$")
    public void clickWizardUploadALPNextBtn() {
        modelUploadSteps.clickAlpUploadStepNextBtn();
    }

    @Then("^Check that wizard upload alp file page is opened$")
    public void checkThatWizardUploadAlpFilePageIsOpened() {
        modelUploadSteps.checkThatWizardUploadAlpFilePageIsOpened();
    }
}
