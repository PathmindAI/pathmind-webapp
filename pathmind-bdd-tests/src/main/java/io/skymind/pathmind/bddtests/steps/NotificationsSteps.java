package io.skymind.pathmind.bddtests.steps;

import io.skymind.pathmind.bddtests.page.Notifications;
import net.thucydides.core.annotations.Step;

public class NotificationsSteps {

    private Notifications notifications;

    @Step
    public void checkThatNewVersionNotificationIsShown() {
        notifications.checkThatNewVersionNotificationIsShown();
    }

    @Step
    public void checkThatNewVersionNotificationTextIs(String text) {
        notifications.checkThatNewVersionNotificationTextIs(text);
    }
    @Step
    public void clickInNotificationSignOutButton() {
        notifications.clickInNotificationSignOutButton();
    }
}
