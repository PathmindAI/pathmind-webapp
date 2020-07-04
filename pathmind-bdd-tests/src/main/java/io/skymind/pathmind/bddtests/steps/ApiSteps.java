package io.skymind.pathmind.bddtests.steps;

import io.skymind.pathmind.bddtests.ApiService;
import net.thucydides.core.annotations.Step;

public class ApiSteps {

    private ApiService apiService;

    @Step
    public void triggerApiNewVersionNotification() {
        apiService.sendLatestVersionNotification();
    }
}
