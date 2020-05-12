package io.skymind.pathmind.bddtests.page;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.skymind.pathmind.bddtests.Utils;
import net.serenitybdd.core.pages.PageObject;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class GenericPage extends PageObject {
    private Utils utils;

    public void checkThatButtonExists(String buttonText) {
        String xpath = String.format("//vaadin-button[text()='%s']", buttonText);
        assertThat(getDriver().findElements(By.xpath(xpath)).size(), greaterThan(0));
    }

    public void checkThatButtonDoesntExist(String buttonText) {
		setImplicitTimeout(5, SECONDS);
        String xpath = String.format("//vaadin-button[text()='%s']", buttonText);
		waitFor(ExpectedConditions.invisibilityOfAllElements(getDriver().findElements(By.xpath(xpath))));
		resetImplicitTimeout();
    }

    public void clickInButton(String buttonText) {
        String xpath = String.format("//vaadin-button[text()='%s']", buttonText);
        getDriver().findElement(By.xpath(xpath)).click();
    }

    public void checkThatNotificationIsShown(String notificationText) {
        waitABit(1000);
        String xpath = String.format("//vaadin-notification-container//span[text()='%s']", notificationText);
        assertThat(getDriver().findElements(By.xpath(xpath)).size(), greaterThan(0));
    }

    public void checkThatTheConfirmationDialogIsShown(String confirmationDialogHeader) {
        setImplicitTimeout(5, SECONDS);
        WebElement dialogShadow = getDriver().findElement(By.xpath("//vaadin-dialog-overlay"));
        waitFor(ExpectedConditions.visibilityOf(dialogShadow));
        WebElement overlay = utils.expandRootElement(dialogShadow);
        WebElement contentShadow = overlay.findElement(By.cssSelector("#content"));
		WebElement contentElements = utils.expandRootElement(contentShadow);
		WebElement header = contentElements.findElement(By.cssSelector(".header"));

        assertThat(header.getText(), is(confirmationDialogHeader));
        resetImplicitTimeout();
    }

    public void checkThatNoConfirmationDialogIsShown() {
        setImplicitTimeout(5, SECONDS);
        waitFor(ExpectedConditions.invisibilityOfAllElements(getDriver().findElements(By.xpath("//vaadin-dialog-overlay"))));
        resetImplicitTimeout();
    }

    public void inConfirmationDialogClickInButton(String buttonText) {
        setImplicitTimeout(5, SECONDS);
        WebElement dialogShadow = getDriver().findElement(By.xpath("//vaadin-dialog-overlay"));
        waitFor(ExpectedConditions.visibilityOf(dialogShadow));
        WebElement overlay = utils.expandRootElement(dialogShadow);
        WebElement contentShadow = overlay.findElement(By.cssSelector("#content"));
        WebElement contentElements = utils.expandRootElement(contentShadow);

        List<WebElement> buttons = contentElements.findElements(By.cssSelector("vaadin-button"));
        List<WebElement> nonShadowButtons = contentShadow.findElements(By.cssSelector("vaadin-button"));
        List<WebElement> allButtons = Stream.concat(buttons.stream(), nonShadowButtons.stream()).collect(Collectors.toList());
        Optional<WebElement> first = allButtons.stream()
                .filter(b -> b.isDisplayed() && b.getText().equals(buttonText))
                .findFirst();
        String errorMessage = String.format("Button '%s' doesn't exist. Available buttons: %s.",
                buttonText,
                StringUtils.join(allButtons.stream().map(WebElement::getText).collect(Collectors.joining(", ")))
        );
        assertThat(errorMessage, first.isPresent());
        first.get().click();
        resetImplicitTimeout();
    }
}
