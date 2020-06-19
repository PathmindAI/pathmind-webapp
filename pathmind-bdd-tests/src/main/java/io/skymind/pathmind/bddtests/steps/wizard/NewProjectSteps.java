package io.skymind.pathmind.bddtests.steps.wizard;

import io.skymind.pathmind.bddtests.page.wizard.NewProjectPage;
import net.thucydides.core.annotations.Step;

public class NewProjectSteps {

    private NewProjectPage newProjectPage;

    @Step
    public void inputNameOfTheNewProject(String projectName) {
        newProjectPage.inputNameOfTheNewProject(projectName);
    }

    @Step
    public void clickProjectNameCreateBtn() {
        newProjectPage.clickProjectNameCreateBtn();
    }

    @Step
    public void checkCreateANewProjectPage() {
        newProjectPage.checkCreateANewProjectPage();
    }

    @Step
    public void checkThatErrorShown(String error) {
        newProjectPage.checkThatErrorShown(error);
    }

    @Step
    public void checkThatNewProjectPageOpened() {
        newProjectPage.checkThatNewProjectPageOpened();
    }
}
