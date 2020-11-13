package io.skymind.pathmind.bddtests.steps;

import io.skymind.pathmind.bddtests.page.NotFoundPage;
import net.thucydides.core.annotations.Step;

public class NotFoundPageSteps {
    private NotFoundPage notFoundPage;

    @Step
    public void check404PageOpened() {
        notFoundPage.check404PageOpened();
        notFoundPage.checkStatusMessageText();
    }

    @Step
    public void checkThatOopsPageOpened() {
        notFoundPage.checkThatOopsPageOpened();
        notFoundPage.checkStatusMessageText();
    }

    @Step
    public void checkThatInvalidDataErrorPageOpened() {
        notFoundPage.checkThatInvalidDataErrorPageOpened();
        notFoundPage.checkStatusMessageText();
    }

    @Step
    public void checkThatStatusPageOpened(String url) {
        notFoundPage.checkThatStatusPageOpened(url);
    }
}
