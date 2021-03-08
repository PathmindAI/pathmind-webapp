package io.skymind.pathmind.bddtests.steps;

import io.skymind.pathmind.bddtests.page.DemoProjectsView;
import net.thucydides.core.annotations.Step;

public class DemoProjectsSteps {

    private DemoProjectsView demoProjectsView;

    @Step
    public void checkDemoListElements() {
        demoProjectsView.checkDemoListElements();
    }

    @Step
    public void clickDemoListItem(String model) {
        demoProjectsView.clickDemoListItem(model);
    }
}
