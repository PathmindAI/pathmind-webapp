package io.skymind.pathmind.bddtests.page;

import java.util.Calendar;

import io.skymind.pathmind.bddtests.Utils;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.core.pages.PageObject;
import net.thucydides.core.annotations.DefaultUrl;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@DefaultUrl("page:home.page")
public class AccountPage extends PageObject {

    private Utils utils;

    @FindBy(xpath = "//account-edit-view-content")
    private WebElement accountEditView;
    @FindBy(id = "accessToken")
    private WebElement accessToken;

    public void checkThatAccountPageOpened() {
        waitABit(2500);
        assertThat(getDriver().getTitle(), containsString("Pathmind | Account"));
        assertThat(getDriver().findElement(By.cssSelector(".section-title-label")).getText(), containsString("Account"));
        assertThat(getDriver().findElement(By.cssSelector(".inner-content vaadin-horizontal-layout:nth-child(1) .info div:nth-child(1)")).getText(), containsString("User Email"));
        assertThat(getDriver().findElement(By.cssSelector(".inner-content vaadin-horizontal-layout:nth-child(1) .info div:nth-child(3)")).getText(), containsString("First Name"));
        assertThat(getDriver().findElement(By.cssSelector(".inner-content vaadin-horizontal-layout:nth-child(1) .info div:nth-child(5)")).getText(), containsString("Last Name"));
        assertThat(getDriver().findElement(By.cssSelector(".inner-content vaadin-horizontal-layout:nth-child(2) .info div:nth-child(1)")).getText(), containsString("Password"));
        assertThat(getDriver().findElement(By.cssSelector(".inner-content vaadin-horizontal-layout:nth-child(3) .info div:nth-child(1)")).getText(), containsString("Access Token"));
        assertThat(getDriver().findElement(By.cssSelector(".inner-content vaadin-horizontal-layout:nth-child(4) .info div:nth-child(1)")).getText(), containsString("Current Subscription"));
        assertThat(getDriver().findElement(By.cssSelector(".inner-content vaadin-horizontal-layout:nth-child(4) .info div:nth-child(2)")).getText(), containsString("Early Access"));
        assertThat(getDriver().findElement(By.cssSelector(".inner-content vaadin-horizontal-layout:nth-child(5) .info div:nth-child(1)")).getText(), containsString("Payment"));
        assertThat(getDriver().findElement(By.cssSelector(".inner-content vaadin-horizontal-layout:nth-child(5) .info div:nth-child(2)")).getText(), containsString("Billing Information"));
        assertThat(getDriver().findElement(By.id("editInfoBtn")).isDisplayed(), is(true));
        assertThat(getDriver().findElement(By.id("editInfoBtn")).getText(), containsString("Edit"));
        assertThat(getDriver().findElement(By.id("changePasswordBtn")).isDisplayed(), is(true));
        assertThat(getDriver().findElement(By.id("changePasswordBtn")).getText(), containsString("Change"));
        assertThat(getDriver().findElement(By.id("editPaymentBtn")).isDisplayed(), is(true));
        assertThat(getDriver().findElement(By.id("editPaymentBtn")).getText(), containsString("Edit"));
    }

    public void clickAccountEditBtn() {
        getDriver().findElement(By.id("editInfoBtn")).click();
    }

    public void inputNewEmail(String email) {
        WebElement inputShadow = utils.expandRootElement(accountEditView.findElement(By.id("email")));
        WebElement input = inputShadow.findElement(By.cssSelector("input"));
        input.click();
        input.clear();
        input.sendKeys(email);
    }

    public void clickAccountEditUpdateBtn() {
        WebElement updateBtnShadow = utils.expandRootElement(accountEditView.findElement(By.id("updateBtn")));
        updateBtnShadow.findElement(By.cssSelector("button")).click();
    }

    public void checkUserEmailIsCorrect(String email) {
        assertThat(getDriver().findElement(By.cssSelector(".data:nth-of-type(2)")).getText(), is(email));
    }

    public void inputAccountPageFirstName(String firstName) {
        JavascriptExecutor jse = (JavascriptExecutor) getDriver();
        WebElement inputFieldShadow = accountEditView.findElement(By.id("firstName"));
        inputFieldShadow.click();
        inputFieldShadow.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        jse.executeScript("arguments[0].value='" + firstName + "';", inputFieldShadow);
    }

    public void inputAccountPageLastName(String lastName) {
        JavascriptExecutor jse = (JavascriptExecutor) getDriver();
        WebElement inputFieldShadow = accountEditView.findElement(By.id("lastName"));
        inputFieldShadow.click();
        inputFieldShadow.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        jse.executeScript("arguments[0].value='" + lastName + "';", inputFieldShadow);
    }

    public void saveAccountPageApiKeyToTheEnvironmentVariable() {
        Serenity.setSessionVariable("apiKey").to(accessToken.getText());
    }

    public void clickAccountPageApiCopyBtnAndPasteToTheSearchField() {
        getDriver().findElement(By.id("apiCopyBtn")).click();
        WebElement e = utils.expandRootElement(getDriver().findElement(By.cssSelector(".search-box_text-field")));
        WebElement input = e.findElement(By.cssSelector("input"));
        input.click();
        input.sendKeys(Keys.CONTROL + "V");
        waitABit(2500);
        assertThat(accessToken.getText(), is(input.getAttribute("value")));
    }

    public void clickAccessTokenRotateBtnAndCheckThatTokenChanged() {
        String beforeRefreshToken = accessToken.getText();
        getDriver().findElement(By.id("small-menu")).click();
        getDriver().findElement(By.xpath("//vaadin-item[normalize-space(text()='Rotate')]")).click();
        waitABit(2500);
        assertThat(beforeRefreshToken, is(not(accessToken.getText())));
    }

    public void accountPageAccessTokenCheckTokenExpires(String expiresDays) {
        assertThat(getDriver().findElement(By.id("apiExpiryDate")).getText(), is(expiresDays));
    }

    public void checkAccountPageFooterComponents() {
        assertThat(getDriver().findElement(By.xpath("(//app-footer/descendant::ul/li/a)[1]")).getAttribute("href"), containsString("https://pathmind.com/privacy"));
        assertThat(getDriver().findElement(By.xpath("(//app-footer/descendant::ul/li/a)[2]")).getAttribute("href"), containsString("https://pathmind.com/subscription-agreement"));
        assertThat(getDriver().findElement(By.cssSelector(".support")).isDisplayed(), is(true));
        assertThat(getDriver().findElement(By.cssSelector(".support")).getText(), containsString("Support"));
        assertThat(getDriver().findElement(By.cssSelector(".support")).getAttribute("href"), containsString("mailto:support@pathmind.com"));
        assertThat(getDriver().findElement(By.cssSelector(".copyright")).getText(), containsString("© " + Calendar.getInstance().get(Calendar.YEAR) + " Pathmind"));
    }

    public void clickAccountFooterBtn(String btn) {
        for (WebElement element : getDriver().findElements(By.xpath("//app-footer/descendant::ul/li/a"))) {
            if (element.getText().contains(btn)) {
                element.click();
                break;
            }
        }
    }
}
