package io.skymind.pathmind.bddtests.page;

import net.serenitybdd.core.pages.PageObject;
import org.openqa.selenium.By;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class Notifications extends PageObject {

    public void checkThatNewVersionNotificationIsShown() {
        assertThat(getDriver().findElement(By.id("vaadin-notification-card")).isDisplayed(), is(true));
    }

    public void checkThatNewVersionNotificationTextIs(String text) {
        assertThat(getDriver().findElement(By.className("closeable-notification-text-label")).getText(), is(text));
    }

    public void clickInNotificationSignOutButton() {
        getDriver().findElement(By.xpath("//*[@id='vaadin-notification-card']/descendant::vaadin-button")).click();
    }
}
