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
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.text.DateFormatSymbols;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    @FindBy(css = ".section-title-label")
    private WebElement titleLabelLocator;
    @FindBy(id = "editInfoBtn")
    private WebElement accountInfoEditBtn;
    @FindBy(id = "changePasswordBtn")
    private WebElement accountPasswordEditBtn;
    @FindBy(css = ".support")
    private WebElement footerSupportBtn;
    @FindBy(xpath = "//confirm-popup")
    private WebElement popupShadow;
    @FindBy(xpath = "//div[@class='data subscription-hint']")
    private WebElement subscriptionHint;
    private By inputLocator = By.cssSelector("input");

    private static final String ACCOUNT_PAGE_TITLE = "Pathmind | Account";
    private static final String ACCOUNT_PAGE_TITLE_LABEL = "Account";

    public void checkThatAccountPageOpened() {
        waitFor(ExpectedConditions.textToBePresentInElement(titleLabelLocator, ACCOUNT_PAGE_TITLE_LABEL));
        assertThat(getDriver().getTitle(), containsString(ACCOUNT_PAGE_TITLE));
        assertThat(titleLabelLocator.getText(), containsString(ACCOUNT_PAGE_TITLE_LABEL));
        assertThat(getDriver().findElement(By.cssSelector(".inner-content vaadin-horizontal-layout:nth-child(1) .info div:nth-child(1)")).getText(), containsString("User Email"));
        assertThat(getDriver().findElement(By.cssSelector(".inner-content vaadin-horizontal-layout:nth-child(1) .info div:nth-child(3)")).getText(), containsString("First Name"));
        assertThat(getDriver().findElement(By.cssSelector(".inner-content vaadin-horizontal-layout:nth-child(1) .info div:nth-child(5)")).getText(), containsString("Last Name"));
        assertThat(getDriver().findElement(By.cssSelector(".inner-content vaadin-horizontal-layout:nth-child(2) .info div:nth-child(1)")).getText(), containsString("Password"));
        assertThat(getDriver().findElement(By.cssSelector(".inner-content vaadin-horizontal-layout:nth-child(3) .info div:nth-child(1)")).getText(), containsString("Access Token"));
        assertThat(getDriver().findElement(By.cssSelector(".inner-content vaadin-horizontal-layout:nth-child(4) .info div:nth-child(1)")).getText(), containsString("Current Subscription"));
        assertThat(getDriver().findElement(By.cssSelector(".inner-content vaadin-horizontal-layout:nth-child(4) .info div:nth-child(2)")).getText(), containsString("Basic"));
        assertThat(accountInfoEditBtn.isDisplayed(), is(true));
        assertThat(accountInfoEditBtn.getText(), containsString("Edit"));
        assertThat(accountPasswordEditBtn.isDisplayed(), is(true));
        assertThat(accountPasswordEditBtn.getText(), containsString("Change"));
    }

    public void clickAccountEditBtn() {
        accountInfoEditBtn.click();
    }

    public void inputNewEmail(String email) {
        WebElement inputShadow = utils.expandRootElement(accountEditView.findElement(By.id("email")));
        WebElement input = inputShadow.findElement(inputLocator);
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
        WebElement input = e.findElement(inputLocator);
        input.click();
        input.sendKeys(Keys.CONTROL + "V");
        waitABit(2500);
        waitFor(ExpectedConditions.attributeToBe(input, "value", accessToken.getText()));
        assertThat(accessToken.getText(), is(input.getAttribute("value")));
    }

    public void clickAccessTokenRotateBtnAndCheckThatTokenChanged() {
        String beforeRefreshToken = accessToken.getText();
        getDriver().findElement(By.id("rotateApiKeyBtn")).click();
        WebElement e = utils.expandRootElement(getDriver().findElement(By.cssSelector("confirm-popup")));
        e.findElement(By.cssSelector("#confirm")).click();
        waitABit(2500);
        assertThat(beforeRefreshToken, is(not(accessToken.getText())));
    }

    public void accountPageAccessTokenCheckTokenExpires(String expiresDays) {
        assertThat(getDriver().findElement(By.id("apiExpiryDate")).getText(), is(expiresDays));
    }

    public void checkAccountPageFooterComponents() {
        assertThat(getDriver().findElement(By.xpath("(//app-footer/descendant::ul/li/a)[1]")).getAttribute("href"), containsString("https://pathmind.com/privacy"));
        assertThat(getDriver().findElement(By.xpath("(//app-footer/descendant::ul/li/a)[2]")).getAttribute("href"), containsString("https://pathmind.com/subscription-agreement"));
        assertThat(footerSupportBtn.isDisplayed(), is(true));
        assertThat(footerSupportBtn.getText(), containsString("Support"));
        assertThat(footerSupportBtn.getAttribute("href"), containsString("mailto:support@pathmind.com"));
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

    public void checkSubscriptionPlansPage() {
        assertThat(getDriver().findElement(By.xpath("//h1")).getText(), is("Subscription Plans"));
        assertThat(getDriver().findElement(By.xpath("//div[@class='header']/span")).getText(), is("Billed monthly per user"));
        assertThat(getDriver().findElement(By.xpath("//vaadin-vertical-layout[@class='inner-content'][1]/descendant::h2")).getText(), is("Basic"));
        assertThat(getDriver().findElement(By.xpath("//vaadin-vertical-layout[@class='inner-content'][1]/descendant::span[@class='details']")).getText(), is("For Students and Hobbyists"));
        assertThat(getDriver().findElement(By.xpath("//vaadin-vertical-layout[@class='inner-content'][1]/descendant::span[@class='price']")).getText(), is("Free"));
        assertThat(getDriver().findElement(By.xpath("//vaadin-vertical-layout[@class='inner-content'][1]/descendant::ul")).getText(), is("One Concurrent Experiment\nUnlimited Policy Export"));
        assertThat(getDriver().findElement(By.xpath("//vaadin-vertical-layout[@class='inner-content'][1]/descendant::vaadin-button")).getText(), is("Current plan"));

        assertThat(getDriver().findElement(By.xpath("//vaadin-vertical-layout[@class='inner-content'][2]/descendant::h2")).getText(), is("Educational"));
        assertThat(getDriver().findElement(By.xpath("//vaadin-vertical-layout[@class='inner-content'][2]/descendant::span[@class='details']")).getText(), is("For Students & Academics*"));
        assertThat(getDriver().findElement(By.xpath("//vaadin-vertical-layout[@class='inner-content'][2]/descendant::span[@class='price']")).getText(), is("$99"));
        assertThat(getDriver().findElement(By.xpath("//vaadin-vertical-layout[@class='inner-content'][2]/descendant::ul")).getText(), is("Unlimited Concurrent Experiments\nUnlimited Policy Export\nTechnical Support Included"));
        assertThat(getDriver().findElement(By.xpath("//vaadin-vertical-layout[@class='inner-content'][2]/descendant::i")).getText(), is("*Promo code required. Contact us for more info."));
        assertThat(getDriver().findElement(By.xpath("//vaadin-vertical-layout[@class='inner-content'][2]/descendant::vaadin-button")).getText(), is("Get in touch"));

        assertThat(getDriver().findElement(By.xpath("//vaadin-vertical-layout[@class='inner-content'][3]/descendant::h2")).getText(), is("Professional"));
        assertThat(getDriver().findElement(By.xpath("//vaadin-vertical-layout[@class='inner-content'][3]/descendant::span[@class='details']")).getText(), is("For Professional Simulation Engineers"));
        assertThat(getDriver().findElement(By.xpath("//vaadin-vertical-layout[@class='inner-content'][3]/descendant::span[@class='popular-tag']")).getText(), is("POPULAR"));
        assertThat(getDriver().findElement(By.xpath("//vaadin-vertical-layout[@class='inner-content'][3]/descendant::span[@class='price']")).getText(), is("$499"));
        assertThat(getDriver().findElement(By.xpath("//vaadin-vertical-layout[@class='inner-content'][3]/descendant::ul")).getText(), is("Unlimited Concurrent Experiments\nUnlimited Policy Export\nTechnical Support Included"));
        assertThat(getDriver().findElement(By.xpath("//vaadin-vertical-layout[@class='inner-content'][3]/descendant::vaadin-button")).getText(), is("Upgrade now"));

        assertThat(getDriver().findElement(By.xpath("//vaadin-vertical-layout[@class='inner-content'][4]/descendant::h2")).getText(), is("Enterprise"));
        assertThat(getDriver().findElement(By.xpath("//vaadin-vertical-layout[@class='inner-content'][4]/descendant::span[@class='details']")).getText(), is("For Consultancies & Corporate Teams"));
        assertThat(getDriver().findElement(By.xpath("//vaadin-vertical-layout[@class='inner-content'][4]/descendant::span[@class='price']")).getText(), is("$999"));
        assertThat(getDriver().findElement(By.xpath("//vaadin-vertical-layout[@class='inner-content'][4]/descendant::ul")).getText(), is("Unlimited Concurrent Experiments\nUnlimited Policy Export\nTechnical Support Included\nPolicy Serving Enabled\nRL Advisory and Training"));
        assertThat(getDriver().findElement(By.xpath("//vaadin-vertical-layout[@class='inner-content'][4]/descendant::vaadin-button")).getText(), is("Get in touch"));
        assertThat(getDriver().findElement(By.xpath("//vaadin-vertical-layout[@class='inner-content'][4]/descendant::a")).getAttribute("href"), is("mailto:support@pathmind.com"));
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
        getDriver().findElement(By.id("cardNumber")).sendKeys("4242424242424242");
        getDriver().findElement(By.id("cardExpiry")).sendKeys("1222");
        getDriver().findElement(By.id("cardCvc")).sendKeys("212");
        getDriver().findElement(By.id("billingName")).sendKeys("Test Name");
    }

    public void paymentPageClickUpgradeBtn() {
        getDriver().findElement(By.xpath("//div[@class='SubmitButton-IconContainer']")).click();
    }

    public void checkAccountSubscriptionIs(String subscription) {
        assertThat(getDriver().findElement(By.xpath("//vaadin-horizontal-layout[@class='subscription-wrapper']/descendant::div[@class='data']")).getText(), is(subscription));
    }

    public void checkUpgradedToProfessionalPageIsShown() {
        assertThat(getDriver().findElement(By.xpath("//h2")).getText(), is("Upgraded to Professional!"));
        assertThat(getDriver().findElement(By.xpath("//*[@class='inner-content']//div")).getText(), is("A confirmation email will be sent after payment is processed."));
    }

    public void checkCancelSubscriptionPopUp() {
        waitFor(ExpectedConditions.visibilityOf(popupShadow));
        WebElement popupShadowRoot = utils.expandRootElement(popupShadow);
        WebElement header = popupShadowRoot.findElement(By.cssSelector("h3"));
        assertThat(header.getText(), is("Cancel Your Subscription?"));
        assertThat(popupShadowRoot.findElement(By.className("message")).getText(), containsString("Cancellation will be effective at the end of your current billing period on"));
    }

    public void clickPopUpDialogYesCancel() {
        waitFor(ExpectedConditions.visibilityOf(popupShadow));
        WebElement popupShadowRoot = utils.expandRootElement(popupShadow);
        popupShadowRoot.findElement(By.cssSelector("#confirm")).click();
    }

    public void checkAccountSubscriptionHint() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("d");
        DateTimeFormatter dtfMonth = DateTimeFormatter.ofPattern("MM");
        LocalDateTime now = LocalDateTime.now();
        String[] shortMonths = new DateFormatSymbols().getShortMonths();
        waitFor(ExpectedConditions.textToBePresentInElement(subscriptionHint, "Subscription will be cancelled on "));
        assertThat(subscriptionHint.getText(), containsString("Subscription will be cancelled on " + shortMonths[Integer.parseInt(dtfMonth.format(now))] + " " + dtf.format(now)));
    }
}
