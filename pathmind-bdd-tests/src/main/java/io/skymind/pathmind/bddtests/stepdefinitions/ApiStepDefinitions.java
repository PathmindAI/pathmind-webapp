package io.skymind.pathmind.bddtests.stepdefinitions;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.skymind.pathmind.bddtests.EmailApi;
import io.skymind.pathmind.bddtests.steps.ApiSteps;
import io.skymind.pathmind.bddtests.steps.User;
import net.serenitybdd.core.Serenity;
import net.thucydides.core.annotations.Steps;

public class ApiStepDefinitions {

    @Steps
    private ApiSteps apiSteps;
    private EmailApi emailApi;

    @When("^Trigger API new version notification$")
    public void triggerApiNewVersionNotification() {
        apiSteps.triggerApiNewVersionNotification();
    }

    @Then("^Check that pathmind API return project with name (.*)$")
    public void checkThatPathmindAPIReturnProjectWithName(String projectName) {
        apiSteps.checkThatPathmindAPIReturnProjectWithName(projectName + Serenity.sessionVariableCalled("randomNumber"));
    }

    @Then("^Check API /projects '(.*)' archived '(.*)'$")
    public void checkAPIProjectsIdArchivedTrue(String projectName, String archived) {
        apiSteps.checkAPIProjectsIdArchivedTrue(projectName + Serenity.sessionVariableCalled("randomNumber"), archived);
    }

    @Given("^API create new user with '(.*)', '(.*)', '(.*)' and status code '(.*)'$")
    public void apiCreateNewUserWith(String firstName, String lastName, String password, int status) {
        User user = new User(firstName, lastName, emailApi.getEmail(), password);
        apiSteps.apiCreateNewUserWith(user)
            .then()
            .log().all().statusCode(status);
    }
}
