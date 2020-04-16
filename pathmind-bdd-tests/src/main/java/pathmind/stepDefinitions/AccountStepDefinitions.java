package pathmind.stepDefinitions;

import cucumber.api.java.en.Then;
import net.thucydides.core.annotations.Steps;
import pathmind.steps.AccountPageSteps;

public class AccountStepDefinitions {

    @Steps
    private AccountPageSteps accountPageSteps;

    @Then("^Check that user account page opened$")
    public void checkThatUserAccountPageOpened() {
        accountPageSteps.checkThatAccountPageOpened();
    }
}
