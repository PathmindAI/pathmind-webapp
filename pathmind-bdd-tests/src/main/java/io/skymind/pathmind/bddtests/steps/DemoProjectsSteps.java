package io.skymind.pathmind.bddtests.steps;

import io.skymind.pathmind.bddtests.page.DemoProjectsView;
import net.thucydides.core.annotations.Step;

public class DemoProjectsSteps {

    private DemoProjectsView demoProjectsView;

    @Step
    public void closeDemoProjectsPopUp() {
        demoProjectsView.closeDemoProjectsPopUp();
    }

    @Step
    public void checkThatDemoProjectsPopupIsShown(boolean shown) {
        demoProjectsView.checkThatDemoProjectsPopupIsShown(shown);
    }

    @Step
    public void checkDemoPopupElements() {
        demoProjectsView.checkDemoPopupElements();
    }

    @Step
    public void clickDemoPopupGetStartedBtn(String model) {
        demoProjectsView.clickDemoPopupGetStartedBtn(model);
    }
}