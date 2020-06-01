package io.skymind.pathmind.bddtests.page;

import io.skymind.pathmind.bddtests.Utils;
import net.serenitybdd.core.pages.PageObject;
import net.thucydides.core.annotations.DefaultUrl;
import org.openqa.selenium.By;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

@DefaultUrl("page:home.page")
public class NotFoundPage extends PageObject {
    private static final String PAGE_NOT_FOUND_TEXT = "Page not found";
    private static final String ERROR_MESSAGE = "The page you requested could not be found. Please contact Pathmind for assistance.";

    private Utils utils;

    public void check404PageOpened() {
        assertThat(getDriver().findElement(By.xpath("//span[@class='breadcrumb']")).getText(), containsString(PAGE_NOT_FOUND_TEXT));
        assertThat(getDriver().findElement(By.xpath("//vaadin-vertical-layout[@theme='padding spacing']")).getText(), containsString(ERROR_MESSAGE));

    }
}
