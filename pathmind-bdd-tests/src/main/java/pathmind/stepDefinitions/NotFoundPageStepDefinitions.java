package pathmind.stepDefinitions;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.thucydides.core.annotations.Steps;
import pathmind.steps.NotFoundPageSteps;

public class NotFoundPageStepDefinitions {
    @Steps
    private NotFoundPageSteps notFoundPageSteps;

    @When("^Open 404 page$")
    public void openIncorrectPathPage() {
        notFoundPageSteps.openIncorrectPathPage();
    }

    @Then("^Check that 404 page opened$")
    public void check404PageOpened(){
        notFoundPageSteps.check404PageOpened();
    }
}
