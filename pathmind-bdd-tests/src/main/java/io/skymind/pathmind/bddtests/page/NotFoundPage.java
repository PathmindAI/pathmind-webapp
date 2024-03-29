package io.skymind.pathmind.bddtests.page;

import java.util.ArrayList;

import io.skymind.pathmind.bddtests.Utils;
import net.serenitybdd.core.pages.PageObject;
import net.thucydides.core.annotations.DefaultUrl;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertTrue;

@DefaultUrl("page:home.page")
public class NotFoundPage extends PageObject {
    private static final String PAGE_NOT_FOUND_TITLE = "Page not found";
    private static final String PAGE_NOT_FOUND_ERROR_MESSAGE = "The page you requested could not be found. Please contact Pathmind for assistance.";
    private static final String OOPS_TITLE = "Oops!";
    private static final String OOPS_ERROR_MESSAGE = "An unexpected error occurred.";
    private static final String INVALID_DATA_ERROR_TITLE = "Invalid Data Error";
    private static final String INVALID_DATA_ERROR_ERROR_MESSAGE = "You don't have permission to access this experiment:";
    private static final String STATUS_MESSAGE = "You can check the current Pathmind system status at status.pathmind.com";
    private static final String STATUS_URL = "https://status.pathmind.com/";

    private Utils utils;

    @FindBy(xpath = "//vaadin-vertical-layout[@theme='padding spacing']/span")
    private WebElement errorMessage;
    @FindBy(xpath = "//h3")
    private WebElement pageTitle;
    @FindBy(xpath = "//vaadin-button[text()='signing out']")
    private WebElement signOutButton;

    public void check404PageOpened() {
        assertThat(pageTitle.getText(), is(PAGE_NOT_FOUND_TITLE));
        assertThat(errorMessage.getText(), is(PAGE_NOT_FOUND_ERROR_MESSAGE));
    }

    public void checkStatusMessageText() {
        assertThat(getDriver().findElement(By.xpath("//vaadin-vertical-layout[@theme='padding spacing']/span[last()]")).getText(), is(STATUS_MESSAGE));
        assertThat(getDriver().findElement(By.xpath("//vaadin-vertical-layout[@theme='padding spacing']/span[last()]/a")).getAttribute("href"), is(STATUS_URL));
    }

    public void checkThatOopsPageOpened() {
        assertThat(pageTitle.getText(), is(OOPS_TITLE));
        assertThat(errorMessage.getText(), containsString(OOPS_ERROR_MESSAGE));
        assertTrue(signOutButton.isDisplayed());
    }

    public void checkThatInvalidDataErrorPageOpened() {
        assertThat(pageTitle.getText(), is(INVALID_DATA_ERROR_TITLE));
        assertThat(errorMessage.getText().toLowerCase(), anyOf(containsString(INVALID_DATA_ERROR_ERROR_MESSAGE.toLowerCase()), containsString("attempted to access project:")));
        assertThat(getDriver().findElement(By.xpath("//span[2]")).getText(), containsString("This link is invalid. Please contact Pathmind if you believe this is an error"));
        assertThat(getDriver().findElement(By.xpath("//span[3]")).getText(), containsString("You can check the current Pathmind system status at "));
    }

    public void checkThatStatusPageOpened(String url) {
        ArrayList<String> tabs = new ArrayList<String>(getDriver().getWindowHandles());
        getDriver().switchTo().window(tabs.get(1));
        assertThat(getDriver().getCurrentUrl(), equalTo(url));
        assertThat(getDriver().getTitle(), containsString("Pathmind Status"));
    }
}
