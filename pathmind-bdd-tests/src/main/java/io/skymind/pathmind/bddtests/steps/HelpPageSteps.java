package io.skymind.pathmind.bddtests.steps;

import io.skymind.pathmind.bddtests.page.HelpPage;
import net.thucydides.core.annotations.Step;

public class HelpPageSteps {

    private HelpPage helpPage;

    @Step
    public void checkConvertingModelsToSupportTuplesPageElements() {
        helpPage.checkConvertingModelsToSupportTuplesPageElements();
    }
}
