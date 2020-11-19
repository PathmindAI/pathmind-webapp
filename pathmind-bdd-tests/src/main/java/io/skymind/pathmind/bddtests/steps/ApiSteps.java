package io.skymind.pathmind.bddtests.steps;

import io.skymind.pathmind.bddtests.ApiService;
import net.thucydides.core.annotations.Step;
import org.hamcrest.Matchers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

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

    @Step
    public void checkAPIProjectsIdArchivedTrue(String id, String archived) {
        assertThat(apiService.getProjectByProjectName(id).get("is_archived").getAsString(), is(archived));
    }
}
