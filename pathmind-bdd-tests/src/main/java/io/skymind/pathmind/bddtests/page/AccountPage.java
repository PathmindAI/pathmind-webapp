package io.skymind.pathmind.bddtests.page;

import io.skymind.pathmind.bddtests.Utils;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.core.pages.PageObject;
import net.thucydides.core.annotations.DefaultUrl;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.Calendar;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DefaultUrl("page:home.page")
public class AccountPage extends PageObject {

    private Utils utils;

    @FindBy(xpath = "//account-view-content")
    private WebElement accountViewShadow;
    @FindBy(id = "editInfoBtn")
    private WebElement editInfoBtnShadow;
    @FindBy(id = "changePasswordBtn")
    private WebElement changePasswordBtnShadow;
    @FindBy(id = "upgradeBtn")
    private WebElement upgradeBtnShadow;
    @FindBy(id = "editPaymentBtn")
    private WebElement editPaymentBtnShadow;
    @FindBy(xpath = "//account-edit-view-content")
    private WebElement accountEditViewShadow;
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
//        assertThat(e.findElement(By.id("upgradeBtn")).isDisplayed(), is(true));
//        assertThat(e.findElement(By.id("upgradeBtn")).getText(), containsString("Upgrade"));
        assertThat(getDriver().findElement(By.id("editPaymentBtn")).isDisplayed(), is(true));
        assertThat(getDriver().findElement(By.id("editPaymentBtn")).getText(), containsString("Edit"));
    }

    public void clickAccountEditBtn() {
        getDriver().findElement(By.id("editInfoBtn")).click();
    }

    public void inputNewEmail(String email) {
        WebElement e = utils.expandRootElement(accountEditViewShadow);
        WebElement inputShadow = utils.expandRootElement(e.findElement(By.id("email")));
        WebElement input = inputShadow.findElement(By.cssSelector("input"));
        input.click();
        input.clear();
        input.sendKeys(email);
    }

    public void clickAccountEditUpdateBtn() {
        WebElement e = utils.expandRootElement(accountEditViewShadow);
        WebElement updateBtnShadow = utils.expandRootElement(e.findElement(By.id("updateBtn")));
        updateBtnShadow.findElement(By.cssSelector("button")).click();
    }

    public void checkUserEmailIsCorrect(String email) {
//        WebElement e = utils.expandRootElement(accountViewShadow);
        assertThat(getDriver().findElement(By.cssSelector(".data:nth-of-type(2)")).getText(), is(email));
    }

    public void inputAccountPageFirstName(String firstName) {
        JavascriptExecutor jse = (JavascriptExecutor) getDriver();
        WebElement e = utils.expandRootElement(accountEditViewShadow);
        WebElement inputFieldShadow = e.findElement(By.id("firstName"));
        inputFieldShadow.click();
        inputFieldShadow.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        jse.executeScript("arguments[0].value='" + firstName + "';", inputFieldShadow);
    }

    public void inputAccountPageLastName(String lastName) {
        JavascriptExecutor jse = (JavascriptExecutor) getDriver();
        WebElement e = utils.expandRootElement(accountEditViewShadow);
        WebElement inputFieldShadow = e.findElement(By.id("lastName"));
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
        WebElement e = utils.expandRootElement(getDriver().findElement(By.xpath("//segment-integrator/preceding-sibling::*[1]")));
        assertThat(e.findElement(By.cssSelector("app-footer > vaadin-horizontal-layout > ul > li:nth-child(1) > a")).getAttribute("href"), containsString("https://pathmind.com/privacy"));
        assertThat(e.findElement(By.cssSelector("app-footer > vaadin-horizontal-layout > ul > li:nth-child(2) > a")).getAttribute("href"), containsString("https://pathmind.com/subscription-agreement"));
        assertThat(e.findElement(By.cssSelector(".support")).isDisplayed(), is(true));
        assertThat(e.findElement(By.cssSelector(".support")).getText(), containsString("Support"));
        assertThat(e.findElement(By.cssSelector(".support")).getAttribute("href"), containsString("mailto:support@pathmind.com"));
        assertThat(e.findElement(By.cssSelector(".copyright")).getText(), containsString("© " + Calendar.getInstance().get(Calendar.YEAR) + " Pathmind"));
    }

    public void clickAccountFooterBtn(String btn) {
        WebElement e = utils.expandRootElement(getDriver().findElement(By.xpath("//segment-integrator/preceding-sibling::*[1]")));
//        e.findElement(By.xpath("app-footer > vaadin-horizontal-layout > ul > li:nth-child(1) > a")).click();
        for(WebElement element : e.findElements(By.cssSelector("app-footer > vaadin-horizontal-layout > ul > li > a"))){
            if (element.getText().contains(btn)){
                element.click();
                break;
            }
        }
    }
}