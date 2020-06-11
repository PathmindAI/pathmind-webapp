package io.skymind.pathmind.bddtests.steps.wizard;

import io.skymind.pathmind.bddtests.page.wizard.ModelDetailsPage;
import net.thucydides.core.annotations.Step;

public class ModelDetailsSteps {

    private ModelDetailsPage modelDetailsPage;

    @Step
    public void inputModelDetails(String notes) {
        modelDetailsPage.inputModelDetailsNotes(notes);
    }

    @Step
    public void clickWizardModelDetailsNextBtn() {
        modelDetailsPage.clickWizardModelDetailsNextBtn();
    }

    @Step
    public void checkThatModelDetailsPageIsOpened() {
        modelDetailsPage.checkThatModelDetailsPageIsOpened();
    }

    @Step
    public void checkThatModelSuccessfullyUploaded() {
        modelDetailsPage.checkThatModelSuccessfullyUploaded();
    }
}
