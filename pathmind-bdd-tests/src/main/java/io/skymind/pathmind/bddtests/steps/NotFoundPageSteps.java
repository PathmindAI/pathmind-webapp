package io.skymind.pathmind.bddtests.steps;

import net.thucydides.core.annotations.Step;
import io.skymind.pathmind.bddtests.page.NotFoundPage;

public class NotFoundPageSteps {
    private NotFoundPage notFoundPage;

    @Step
    public void check404PageOpened(){
        notFoundPage.check404PageOpened();
    }
}
