package io.skymind.pathmind.bddtests.page;

import io.skymind.pathmind.bddtests.Utils;
import net.serenitybdd.core.pages.PageObject;
import net.thucydides.core.annotations.DefaultUrl;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.support.FindBy;

import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DefaultUrl("page:home.page")
public class LoginPage extends PageObject {

    private Utils utils;

    @FindBy(css = "#vaadinLoginUsername")
    private WebElement emailField;
    @FindBy(css = "#vaadinLoginPassword")
    private WebElement passwordField;
    @FindBy(xpath = "//vaadin-button[@part='vaadin-login-submit']")
    private WebElement lognBtn;
    @FindBy(id = "forgotPasswordButton")
    private WebElement forgotPassBtn;
    @FindBy(xpath = "//vaadin-login-form-wrapper[@part='vaadin-login-native-form-wrapper']")
    private WebElement loginFormWraper;
    @FindBy(xpath = "//sign-up-view")
    private WebElement signUpShadow;
    @FindBy(xpath = "//vaadin-button[@title='Send verification email again.']")
    private WebElement resendBtnShadow;
    @FindBy(xpath = "//reset-password-view")
    private WebElement resetPassViewShadow;

    public void inputEmail(String email) {
        emailField.click();
        emailField.sendKeys(email);
    }

    public void inputPassword(String password) {
        passwordField.click();
        passwordField.sendKeys(password);
    }

    public void clickLoginBtn() {
        lognBtn.click();
    }

    public void checkThatWarningMessageIsShown() {
        assertThat(getDriver().findElement(By.xpath("//div[@class='error-message']")).getText(), containsText("Incorrect username or password"));
    }

    public void deleteAllCookies() {
        getDriver().manage().deleteAllCookies();
    }

    public void openPage(String url) {
        getDriver().navigate().to(url);
    }

    public void newUserInputFirstName(String firstName) {
        WebElement signUpView = utils.expandRootElement(signUpShadow);
        WebElement inputFieldShadow = signUpView.findElement(By.id("firstName"));
        WebElement field = utils.expandRootElement(inputFieldShadow);
        WebElement firstNameInputField = field.findElement(By.cssSelector("input"));
        firstNameInputField.click();
        firstNameInputField.sendKeys(firstName);
    }

    public void newUserInputLastName(String lastName) {
        WebElement signUpView = utils.expandRootElement(signUpShadow);
        WebElement inputFieldShadow = signUpView.findElement(By.id("lastName"));
        WebElement field = utils.expandRootElement(inputFieldShadow);
        WebElement inputField = field.findElement(By.cssSelector("input"));
        inputField.click();
        inputField.sendKeys(lastName);
    }

    public void newUserInputEmail(String email) {
        WebElement signUpView = utils.expandRootElement(signUpShadow);
        WebElement inputFieldShadow = signUpView.findElement(By.id("email"));
        WebElement field = utils.expandRootElement(inputFieldShadow);
        WebElement inputField = field.findElement(By.cssSelector("input"));
        inputField.click();
        inputField.sendKeys(email);
    }

    public void clickSignUpButton() {
        WebElement signUpView = utils.expandRootElement(signUpShadow);
        WebElement signUpBtnShadow = signUpView.findElement(By.id("signUp"));
        WebElement field = utils.expandRootElement(signUpBtnShadow);
        WebElement signUpBtn = field.findElement(By.id("button"));
        signUpBtn.click();
        waitABit(2000);
    }

    public void fillNewUserPassword(String password) {
        WebElement signUpView = utils.expandRootElement(signUpShadow);
        WebElement inputFieldShadow = signUpView.findElement(By.id("newPassword"));
        WebElement field = utils.expandRootElement(inputFieldShadow);
        WebElement inputField = field.findElement(By.cssSelector("input"));
        inputField.click();
        inputField.sendKeys(password);
    }
    public void fillNewUserConfirmationPassword(String password){
        WebElement signUpView = utils.expandRootElement(signUpShadow);
        WebElement confirmInputFieldShadow = signUpView.findElement(By.id("confirmNewPassword"));
        WebElement configrmField = utils.expandRootElement(confirmInputFieldShadow);
        WebElement confirmPasswordField = configrmField.findElement(By.cssSelector("input"));
        confirmPasswordField.click();
        confirmPasswordField.sendKeys(password);
    }

    public void createNewUserClickSignInButton() {
        WebElement signUpView = utils.expandRootElement(signUpShadow);
        WebElement signUpBtnShadow = signUpView.findElement(By.id("signIn"));
        WebElement field = utils.expandRootElement(signUpBtnShadow);
        WebElement signInBtn = field.findElement(By.id("button"));
        signInBtn.click();
    }

    public void checkThatError(String errorText) {
        assertThat(getDriver().findElement(By.xpath("//vaadin-horizontal-layout[@class='email-not-verified-cont error-message']")).getText(), containsString(errorText));
    }

    public void checkThatResendBtnIsShown() {
        assertThat(resendBtnShadow.getText(), containsText("Resend"));
    }

    public void checkCreateNewUserPageElements() {
        WebElement signUpView = utils.expandRootElement(signUpShadow);

        assertThat(signUpView.findElement(By.cssSelector("h3")).getText(), containsString("Sign up for a 30-day Free Trial!"));
        assertThat(signUpView.findElement(By.cssSelector(".do-not-share-url")).getText(), containsString("Please do not share this URL"));

        WebElement firstNameInputShadow = signUpView.findElement(By.id("firstName"));
        WebElement firstNameInput = utils.expandRootElement(firstNameInputShadow);
        assertThat(firstNameInput.findElement(By.cssSelector("div.vaadin-text-field-container label")).getText(), containsString("First Name"));
        assertThat(firstNameInput.findElements(By.cssSelector("input")).size(), is(1));

        WebElement secondNameInputShadow = signUpView.findElement(By.id("lastName"));
        WebElement secondNameInput = utils.expandRootElement(secondNameInputShadow);
        assertThat(secondNameInput.findElement(By.cssSelector("div.vaadin-text-field-container label")).getText(), containsString("Last Name"));
        assertThat(secondNameInput.findElements(By.cssSelector("input")).size(), is(1));

        WebElement emailInputShadow = signUpView.findElement(By.id("email"));
        WebElement emailInput = utils.expandRootElement(emailInputShadow);
        assertThat(emailInput.findElement(By.cssSelector("div.vaadin-text-field-container label")).getText(), containsString("Email"));
        assertThat(signUpView.findElements(By.cssSelector("vaadin-text-field[id='email'][required]")).size(), is(1));
        assertThat(emailInput.findElements(By.cssSelector("input")).size(), is(1));

        WebElement signUpBtnShadow = signUpView.findElement(By.id("signUp"));
        WebElement field = utils.expandRootElement(signUpBtnShadow);
        assertThat(signUpBtnShadow.getText(), containsString("Sign up"));
        assertThat(field.findElements(By.id("button")).size(), is(1));

        assertThat(signUpView.findElements(By.cssSelector(".support")).size(), is(1));
        assertThat(signUpView.findElement(By.cssSelector(".support")).getText(), containsString("Contact Support"));
        assertThat(signUpView.findElement(By.cssSelector(".support")).getAttribute("href"), containsString("mailto:support@pathmind.com"));
    }

    public void clickCreateNewUserCancelBtn() {
        WebElement signUpView = utils.expandRootElement(signUpShadow);
        signUpView.findElement(By.id("cancelSignUpBtn")).click();
    }

    public void checkThatLoginPageOpened() {
        assertThat(getDriver().getTitle(), containsString("Pathmind | Sign - in"));
        assertThat(getDriver().getCurrentUrl(), containsString("/sign-in"));
    }

    public void checkNewUserPageEmailAlertBtn() {
        WebElement signUpView = utils.expandRootElement(signUpShadow);
        WebElement emailInputShadow = signUpView.findElement(By.id("email"));
        WebElement emailInput = utils.expandRootElement(emailInputShadow);
        assertThat(emailInput.findElement(By.cssSelector("div.vaadin-text-field-container div[part='error-message']")).getText(), containsString("This doesn't look like a valid email address"));
    }

    public void createNewUserCheckThatErrorMessageShown(List<String> errorMsg) {
        WebElement signUpView = utils.expandRootElement(signUpShadow);

        List<WebElement> listE = signUpView.findElements(By.cssSelector("#newPassNotes span"));
        List<String> errorsList = new ArrayList<>();

        for (WebElement webElement : listE) {
            errorsList.add(webElement.getText());
        }
        for (String error : errorMsg) {
            assertThat(errorsList, hasItem(equalTo(error)));
        }
    }

    public void createNewUserCheckThatErrorMessageForEmailFieldShown(String errorMsg) {
        WebElement signUpView = utils.expandRootElement(signUpShadow);
		WebElement emailView =  utils.expandRootElement(signUpView.findElement(By.cssSelector("#email")));
        assertThat(emailView.findElement(By.cssSelector("#vaadin-text-field-error-2")).getText(), containsString(errorMsg));
    }

    public void createNewUserCheckThatForgotPasswordBtnExist() {
        WebElement signUpView = utils.expandRootElement(signUpShadow);
        assertThat(signUpView.findElement(By.id("forgotPasswordBtn")).isDisplayed(), is(true));
        assertThat(signUpView.findElement(By.id("forgotPasswordBtn")).getText(), containsString("Want to reset password?"));
    }

    public void createNewUserClickResetPasswordBtn() {
        WebElement signUpView = utils.expandRootElement(signUpShadow);
        signUpView.findElement(By.id("forgotPasswordBtn")).click();
    }

    public void checkPasswordRecoveryPageElements() {
        WebElement resetPassView = utils.expandRootElement(resetPassViewShadow);
        assertThat(resetPassView.findElement(By.cssSelector("h3")).getText(), containsString("Reset Your Password"));
        assertThat(resetPassView.findElement(By.cssSelector("p")).getText(), containsString("Enter your work email and we'll send you a link to set a new password."));
        WebElement emailView = utils.expandRootElement(resetPassView.findElement(By.id("email")));
        assertThat(emailView.findElement(By.cssSelector("label")).getText(), containsString("Email"));
        assertThat(emailView.findElement(By.cssSelector("input")).isDisplayed(), is(true));
        WebElement sendBtnView = utils.expandRootElement(resetPassView.findElement(By.id("sendBtn")));
        assertThat(sendBtnView.findElement(By.cssSelector(".vaadin-button-container")).isDisplayed(), is(true));
        WebElement cancelBtnView = utils.expandRootElement(resetPassView.findElement(By.cssSelector("#cancelBtn")));
        assertThat(cancelBtnView.findElement(By.cssSelector(".vaadin-button-container")).isDisplayed(), is(true));
        assertThat(resetPassView.findElement(By.cssSelector(".support")).getText(), containsString("Contact Support"));
        assertThat(resetPassView.findElement(By.cssSelector(".support")).getAttribute("href"), containsString("mailto:support@pathmind.com"));
    }

    public void checkLoginPageElements() {
        assertThat(getDriver().findElement(By.cssSelector(".logo")).isDisplayed(), is(true));
        assertThat(getDriver().findElement(By.cssSelector(".welcome-text")).isDisplayed(), is(true));
        assertThat(getDriver().findElement(By.cssSelector(".welcome-text")).getText(), containsString("Welcome to"));
        assertThat(getDriver().findElement(By.cssSelector("h3")).isDisplayed(), is(true));
        assertThat(getDriver().findElement(By.cssSelector("h3")).getText(), containsString("Sign In"));
        WebElement email = utils.expandRootElement(emailField);
        assertThat(email.findElement(By.cssSelector("div[part='input-field']")).isDisplayed(), is(true));
        assertThat(email.findElement(By.cssSelector("div label")).getText(), containsString("Email"));
        WebElement password = utils.expandRootElement(passwordField);
        assertThat(password.findElement(By.cssSelector("div[part='input-field']")).isDisplayed(), is(true));
        assertThat(password.findElement(By.cssSelector("div label")).getText(), containsString("Password"));
        WebElement login = utils.expandRootElement(lognBtn);
        assertThat(login.findElement(By.cssSelector(".vaadin-button-container")).isDisplayed(), is(true));
        assertThat(lognBtn.getText(), containsString("Sign In"));
        WebElement forgotBtnView = utils.expandRootElement(loginFormWraper);
        WebElement forgotPassBtn = utils.expandRootElement(forgotBtnView.findElement(By.cssSelector("#forgotPasswordButton")));
        assertThat(forgotPassBtn.findElement(By.cssSelector(".vaadin-button-container")).isDisplayed(), is(true));
        assertThat(forgotBtnView.findElement(By.cssSelector("#forgotPasswordButton")).getText(), containsString("Forgot your password?"));
        assertThat(getDriver().findElement(By.cssSelector(".policy")).getText(), containsString("By clicking Sign In, you agree to Pathmind's Terms of Use and Privacy Policy."));
        assertThat(getDriver().findElement(By.xpath("//a[text()='Terms of Use']")).getAttribute("href"), containsString("https://pathmind.com/subscription-agreement"));
        assertThat(getDriver().findElement(By.xpath("//a[text()='Privacy Policy']")).getAttribute("href"), containsString("https://pathmind.com/privacy"));
    }

	public void checkConsoleError(String error) {
		LogEntries logs = getDriver().manage().logs().get(LogType.BROWSER);
		for (LogEntry logEntry : logs) {
			System.out.println(logEntry.getMessage());
			assertThat(logEntry.getMessage(), not(containsString(error)));
		}
	}
}
