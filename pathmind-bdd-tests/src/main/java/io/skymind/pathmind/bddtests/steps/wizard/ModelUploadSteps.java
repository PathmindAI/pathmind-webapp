package io.skymind.pathmind.bddtests.steps.wizard;

import io.skymind.pathmind.bddtests.page.wizard.ModelUploadPage;
import net.thucydides.core.annotations.Step;

public class ModelUploadSteps {

    private ModelUploadPage modelUploadPage;

    @Step
    public void uploadModelFile(String model) {
        modelUploadPage.uploadModelFile(model);
    }

    @Step
    public void checkThatModelUploadPageOpened() {
        modelUploadPage.checkThatModelUploadPageOpened();
    }

    @Step
    public void checkErrorMessageInModelCheckPanel(String errorMessage) {
        modelUploadPage.checkErrorMessageInModelCheckPanel(errorMessage);
    }

    @Step
    public void clickAlpUploadStepNextBtn() {
        modelUploadPage.clickAlpUploadStepNextBtn();
    }

    @Step
    public void uploadALPFile(String alpFile) {
        modelUploadPage.uploadALPFile(alpFile);
    }

    @Step
    public void checkThatWizardUploadAlpFilePageIsOpened() {
        modelUploadPage.checkThatWizardUploadAlpFilePageIsOpened();
    }

    @Step
    public void checkWizardAlpUploadStep() {
        modelUploadPage.checkWizardAlpUploadStep();
    }
}
