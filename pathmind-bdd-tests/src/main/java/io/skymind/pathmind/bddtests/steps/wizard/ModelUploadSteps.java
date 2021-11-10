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
    public void checkErrorMessageStartsWithInModelCheckPanel(String errorMessage) {
        modelUploadPage.checkErrorMessageStartsWithInModelCheckPanel(errorMessage);
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
    public void checkThatModelUploadLinkOpened() {
        modelUploadPage.checkThatModelUploadLinkOpened();
    }

    @Step
    public void checkWizardWarningLabelIsShown(String warningLabel, Boolean isShown) {
        modelUploadPage.checkWizardWarningLabelIsShown(warningLabel, isShown);
    }

    @Step
    public void wizardModelUploadCheckFolderUploadPage() {
        modelUploadPage.wizardModelUploadCheckFolderUploadPage();
    }

    @Step
    public void wizardModelUploadCheckArchiveUploadPage() {
        modelUploadPage.wizardModelUploadCheckArchiveUploadPage();
    }

    @Step
    public void wizardModelUploadSwitchModelTypeTo() {
        modelUploadPage.wizardModelUploadSwitchModelTypeTo();
    }

    @Step
    public void wizardModelUploadCheckPythonArchiveUploadPage() {
        modelUploadPage.wizardModelUploadCheckPythonArchiveUploadPage();
    }

    @Step
    public void wizardModelUploadInputEnv(String env) {
        modelUploadPage.wizardModelUploadInputEnv(env);
    }

    @Step
    public void clickPythonUploadModelNextBtn() {
        modelUploadPage.clickPythonUploadModelNextBtn();
    }
}
