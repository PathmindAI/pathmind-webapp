package io.skymind.pathmind.bddtests.steps;

import io.restassured.response.Response;
import io.skymind.pathmind.bddtests.ApiService;
import net.thucydides.core.annotations.Step;
import org.hamcrest.Matchers;

import static net.serenitybdd.rest.SerenityRest.given;
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

    @Step
    public Response apiCreateNewUserWith(User user) {

        return given()
            .baseUri("http://127.0.0.1:8081")
            .basePath("/signup")
            .contentType("application/json")
            .body(user)
            .post()
            .then()
            .extract().response();
    }
}
