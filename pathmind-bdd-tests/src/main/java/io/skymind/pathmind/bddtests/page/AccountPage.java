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
        for(WebElement element : getDriver().findElements(By.xpath("//app-footer/descendant::ul/li/a"))){
            if (element.getText().contains(btn)){
                element.click();
                break;
            }
        }
    }

    public void checkSubscriptionPlansPage() {
        assertThat(getDriver().findElement(By.xpath("//h1")).getText(), is("Subscription Plans"));
        assertThat(getDriver().findElement(By.xpath("//vaadin-vertical-layout[@class='inner-content'][1]/descendant::h2")).getText(), is("Basic"));
        assertThat(getDriver().findElement(By.xpath("//vaadin-vertical-layout[@class='inner-content'][1]/descendant::span[@class='details']")).getText(), is("For Students and Hobbyists"));
        assertThat(getDriver().findElement(By.xpath("//vaadin-vertical-layout[@class='inner-content'][1]/descendant::span[@class='price']")).getText(), is("Free"));
        assertThat(getDriver().findElement(By.xpath("//vaadin-vertical-layout[@class='inner-content'][1]/descendant::span[@class='additional-info']")).getText(), is(" "));
        assertThat(getDriver().findElement(By.xpath("//vaadin-vertical-layout[@class='inner-content'][1]/descendant::ul")).getText(), is("25 Experiments Per Month\nUnlimited Policy Export"));
        assertThat(getDriver().findElement(By.xpath("//vaadin-vertical-layout[@class='inner-content'][1]/descendant::vaadin-button")).getText(), is("Current Plan"));

        assertThat(getDriver().findElement(By.xpath("//vaadin-vertical-layout[@class='inner-content'][2]/descendant::h2")).getText(), is("Professional"));
        assertThat(getDriver().findElement(By.xpath("//vaadin-vertical-layout[@class='inner-content'][2]/descendant::span[@class='details']")).getText(), is("For Professional Simulation Engineers"));
        assertThat(getDriver().findElement(By.xpath("//vaadin-vertical-layout[@class='inner-content'][2]/descendant::span[@class='popular-tag']")).getText(), is("POPULAR"));
        assertThat(getDriver().findElement(By.xpath("//vaadin-vertical-layout[@class='inner-content'][2]/descendant::span[@class='price']")).getText(), is("$500"));
        assertThat(getDriver().findElement(By.xpath("//vaadin-vertical-layout[@class='inner-content'][2]/descendant::span[@class='additional-info']")).getText(), is("For yearly subscription per seat"));
        assertThat(getDriver().findElement(By.xpath("//vaadin-vertical-layout[@class='inner-content'][2]/descendant::ul")).getText(), is("200 Experiments Per Month\nUnlimited Policy Export\nTechnical Support Included"));
        assertThat(getDriver().findElement(By.xpath("//vaadin-vertical-layout[@class='inner-content'][2]/descendant::vaadin-button")).getText(), is("Choose Pro"));

        assertThat(getDriver().findElement(By.xpath("//vaadin-vertical-layout[@class='inner-content'][3]/descendant::h2")).getText(), is("Enterprise"));
        assertThat(getDriver().findElement(By.xpath("//vaadin-vertical-layout[@class='inner-content'][3]/descendant::span[@class='details']")).getText(), is("For Consultancies & Corporate Teams"));
        assertThat(getDriver().findElement(By.xpath("//vaadin-vertical-layout[@class='inner-content'][3]/descendant::span[@class='price']")).getText(), is("$1,000"));
        assertThat(getDriver().findElement(By.xpath("//vaadin-vertical-layout[@class='inner-content'][3]/descendant::span[@class='additional-info']")).getText(), is("For yearly subscription per seat"));
        assertThat(getDriver().findElement(By.xpath("//vaadin-vertical-layout[@class='inner-content'][3]/descendant::ul")).getText(), is("2,000 Experiments Per Month\nUnlimited Policy Export\nTechnical Support Included\nPolicy Serving Enabled\nRL Advisory and Training"));
        assertThat(getDriver().findElement(By.xpath("//vaadin-vertical-layout[@class='inner-content'][3]/descendant::vaadin-button")).getText(), is("Contact Us"));
        assertThat(getDriver().findElement(By.xpath("//vaadin-vertical-layout[@class='inner-content'][3]/descendant::a")).getAttribute("href"), is("mailto:support@pathmind.com"));

        assertThat(getDriver().findElement(By.xpath("//p[@class='caption']")).getText(), is("Applicable taxes not included"));
    }

    public void checkSubscriptionPlansUpgradePage() {
        assertThat(getDriver().findElement(By.xpath("//div[@class='title']")).getText(), is("Upgrade to Professional"));
        assertThat(getDriver().findElement(By.xpath("//p[@class='sub-title']")).getText(), containsString("Please fill in the information below. All fields are required."));
        assertThat(getDriver().findElement(By.id("name")).getText(), is("Name on card"));
        assertThat(getDriver().findElement(By.id("address")).getText(), containsString("Billing Address"));
        assertThat(getDriver().findElement(By.id("city")).getText(), containsString("City"));
        assertThat(getDriver().findElement(By.id("state")).getText(), containsString("State"));
        assertThat(getDriver().findElement(By.id("zip")).getText(), containsString("Zip"));
        assertThat(getDriver().findElement(By.id("card-element")).getText(), containsString(""));
    }

    public void fillPaymentFormWithStripeTestCard() {
        WebElement nameOnCardShadow = utils.expandRootElement(getDriver().findElement(By.id("name")));
        nameOnCardShadow.findElement(By.cssSelector("input")).sendKeys("Test Name");
        WebElement billingAddressShadow = utils.expandRootElement(getDriver().findElement(By.id("address")));
        billingAddressShadow.findElement(By.cssSelector("input")).sendKeys("Jl. Pantai Kedonganan, Kedonganan, Kuta, Kabupaten Badung, Bali");
        WebElement cityShadow = utils.expandRootElement(getDriver().findElement(By.id("city")));
        cityShadow.findElement(By.cssSelector("input")).sendKeys("Kuta");
        WebElement stateShadow = utils.expandRootElement(getDriver().findElement(By.id("state")));
        stateShadow.findElement(By.cssSelector("input")).sendKeys("Kedonganan");
        WebElement zipShadow = utils.expandRootElement(getDriver().findElement(By.id("zip")));
        zipShadow.findElement(By.cssSelector("input")).sendKeys("80361");
        getDriver().switchTo().frame(getDriver().findElement(By.xpath("//iframe[@title='Secure card payment input frame']")));
        getDriver().findElement(By.cssSelector("div[class='CardNumberField-input-wrapper'] input")).sendKeys("4242424242424242");
        getDriver().findElement(By.cssSelector("span[class='CardField-expiry CardField-child'] input")).sendKeys("1222");
        getDriver().findElement(By.cssSelector("span[class='CardField-cvc CardField-child'] input")).sendKeys("212");
        getDriver().switchTo().defaultContent();
    }

    public void paymentPageClickUpgradeBtn() {
        getDriver().findElement(By.id("signUp")).click();
        waitABit(5000);
    }

    public void checkAccountSubscriptionIs(String subscription) {
        assertThat(getDriver().findElement(By.xpath("//vaadin-horizontal-layout[@class='subscription-wrapper']/descendant::div[@class='data']")).getText(), is(subscription));
    }
}
