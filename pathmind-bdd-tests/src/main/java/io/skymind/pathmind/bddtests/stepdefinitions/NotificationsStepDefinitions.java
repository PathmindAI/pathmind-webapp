package io.skymind.pathmind.bddtests.stepdefinitions;

import cucumber.api.java.en.Then;
import io.skymind.pathmind.bddtests.steps.NotificationsSteps;
import net.thucydides.core.annotations.Steps;

public class NotificationsStepDefinitions {

    @Steps
    private NotificationsSteps notificationsSteps;

    @Then("^Check that new version notification is shown$")
    public void checkThatNewVersionNotificationIsShown() {
        notificationsSteps.checkThatNewVersionNotificationIsShown();
    }

    @Then("^Check that new version notification text is (.*)$")
    public void checkThatNewVersionNotificationTextIs(String text) {
        notificationsSteps.checkThatNewVersionNotificationTextIs(text);
    }

    @Then("^Click in notification Sign out button$")
    public void clickInNotificationSignOutButton() {
        notificationsSteps.clickInNotificationSignOutButton();
    }
}
