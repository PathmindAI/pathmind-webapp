package io.skymind.pathmind.bddtests.page;

import io.skymind.pathmind.bddtests.Utils;
import net.serenitybdd.core.pages.PageObject;
import net.thucydides.core.annotations.DefaultUrl;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DefaultUrl("page:home.page")
public class NotFoundPage extends PageObject {
    private static final String PAGE_NOT_FOUND_TITLE = "Page not found";
    private static final String PAGE_NOT_FOUND_ERROR_MESSAGE = "The page you requested could not be found. Please contact Pathmind for assistance.";
    private static final String OOPS_TITLE = "Oops";
    private static final String OOPS_ERROR_MESSAGE = "An unexpected error occurred. Please contact Pathmind for assistance";
    private static final String INVALID_DATA_ERROR_TITLE = "Invalid data error";
    private static final String INVALID_DATA_ERROR_ERROR_MESSAGE = "This link is invalid. Please contact Pathmind if you believe this is an error";
    private static final String STATUS_MESSAGE = "You can check the current Pathmind system status at status.pathmind.com";
    private static final String STATUS_URL = "https://status.pathmind.com/";

    private Utils utils;

    @FindBy(xpath = "//vaadin-vertical-layout[@theme='padding spacing']/span")
    private WebElement errorMessage;
    @FindBy(xpath = "//span[@class='breadcrumb']")
    private WebElement pageTitle;

    public void check404PageOpened() {
        assertThat(pageTitle.getText(), is(PAGE_NOT_FOUND_TITLE));
        assertThat(errorMessage.getText(), is(PAGE_NOT_FOUND_ERROR_MESSAGE));
    }

    public void checkStatusMessageText() {
        assertThat(getDriver().findElement(By.xpath("//vaadin-vertical-layout[@theme='padding spacing']/p")).getText(), is(STATUS_MESSAGE));
        assertThat(getDriver().findElement(By.xpath("//vaadin-vertical-layout[@theme='padding spacing']/p/a")).getAttribute("href"), is(STATUS_URL));
    }

    public void checkThatOopsPageOpened() {
        assertThat(pageTitle.getText(), is(OOPS_TITLE));
        assertThat(errorMessage.getText(), containsString(OOPS_ERROR_MESSAGE));
    }

    public void checkThatInvalidDataErrorPageOpened() {
        assertThat(pageTitle.getText(), is(INVALID_DATA_ERROR_TITLE));
        assertThat(errorMessage.getText(), containsString(INVALID_DATA_ERROR_ERROR_MESSAGE));
    }

    public void checkThatStatusPageOpened(String url) {
        ArrayList<String> tabs = new ArrayList<String>(getDriver().getWindowHandles());
        getDriver().switchTo().window(tabs.get(1));
        assertThat(getDriver().getCurrentUrl(), equalTo(url));
        assertThat(getDriver().getTitle(), containsString("Pathmind Status"));
    }
}
