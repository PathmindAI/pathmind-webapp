package io.skymind.pathmind.bddtests.stepdefinitions;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.skymind.pathmind.bddtests.steps.ApiSteps;
import net.serenitybdd.core.Serenity;
import net.thucydides.core.annotations.Steps;

public class ApiStepDefinitions {

    @Steps
    private ApiSteps apiSteps;

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
}
