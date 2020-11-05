package io.skymind.pathmind.bddtests.steps;

import io.skymind.pathmind.bddtests.ApiService;
import net.thucydides.core.annotations.Step;
import org.hamcrest.Matchers;

public class ApiSteps {

    private ApiService apiService;

    @Step
    public void triggerApiNewVersionNotification() {
        apiService.sendLatestVersionNotification();
    }

    @Step
    public void checkThatPathmindAPIReturnProjectWithName(String projectName) {
        apiService.getUserProjects().body("name", Matchers.hasItems(projectName));
    }
}
