package io.skymind.pathmind.bddtests.steps;

import java.util.List;

import io.skymind.pathmind.bddtests.page.HomePage;
import io.skymind.pathmind.bddtests.page.LoginPage;
import net.serenitybdd.core.environment.EnvironmentSpecificConfiguration;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.util.EnvironmentVariables;
import net.thucydides.core.util.SystemEnvironmentVariables;

import static net.thucydides.core.webdriver.ThucydidesWebDriverSupport.getDriver;

public class LoginPageSteps {

    private static final EnvironmentVariables VARIABLES = SystemEnvironmentVariables.createEnvironmentVariables();
    private static final String PATHMIND_URL = EnvironmentSpecificConfiguration.from(VARIABLES).getProperty("base.url");
    private LoginPage loginPage;
    private HomePage homePage;

    @Step
    public void openPathmindUrl() {
        loginPage.open();
    }

    @Step
    public void loginWithCredential(String email, String password) {
        if (getDriver().getCurrentUrl().contains("sign-up")) {
            loginPage.openPage(PATHMIND_URL + "sign-in");
        }
        if (getDriver().getCurrentUrl().contains("sign-in")) {
            loginPage.inputEmail(email);
            loginPage.inputPassword(password);
            loginPage.clickLoginBtn();
        }
    }

    @Step
    public void checkThatUserSuccessfullyLoggedIn(String name) {
        homePage.checkNavAccLinkVisible(name);
    }

    @Step
    public void checkThatWarningMessageIsShown() {
        loginPage.checkThatWarningMessageIsShown();
    }

    @Step
    public void deleteAllCookies() {
        loginPage.deleteAllCookies();
    }

    @Step
    public void openPage(String url) {
        loginPage.openPage(url);

    }

    @Step
    public void newUserInputFirstName(String firstName) {
        loginPage.newUserInputFirstName(firstName);
    }

    @Step
    public void newUserInputLastName(String lastName) {
        loginPage.newUserInputLastName(lastName);
    }

    @Step
    public void newUserInputEmail(String email) {
        loginPage.newUserInputEmail(email);
    }

    @Step
    public void fillNewUserPassword(String password) {
        loginPage.fillNewUserPassword(password);
    }

    @Step
    public void fillNewUserConfirmationPassword(String password) {
        loginPage.fillNewUserConfirmationPassword(password);
    }

    @Step
    public void createNewUserClickSignInButton() {
        loginPage.createNewUserClickSignInButton();
    }

    @Step
    public void checkThatError(String errorText) {
        loginPage.checkThatError(errorText);
    }

    @Step
    public void checkThatResendBtnIsShown() {
        loginPage.checkThatResendBtnIsShown();
    }

    @Step
    public void checkCreateNewUserPageElements() {
        loginPage.checkCreateNewUserPageElements();
    }

    @Step
    public void checkThatLoginPageOpened() {
        loginPage.checkThatLoginPageOpened();
    }

    @Step
    public void checkNewUserPageEmailAlertBtn() {
        loginPage.checkNewUserPageEmailAlertBtn();
    }

    @Step
    public void createNewUserCheckThatErrorMessageShown(List<String> errorMsg) {
        loginPage.createNewUserCheckThatErrorMessageShown(errorMsg);
    }

    @Step
    public void createNewUserCheckThatErrorMessageForEmailFieldShown(String errorMsg) {
        loginPage.createNewUserCheckThatErrorMessageForEmailFieldShown(errorMsg);
    }

    @Step
    public void createNewUserCheckThatForgotPasswordBtnExist() {
        loginPage.createNewUserCheckThatForgotPasswordBtnExist();
    }

    @Step
    public void createNewUserClickResetPasswordBtn() {
        loginPage.createNewUserClickResetPasswordBtn();
    }

    @Step
    public void checkPasswordRecoveryPageElements() {
        loginPage.checkPasswordRecoveryPageElements();
    }

    @Step
    public void checkLoginPageElements() {
        loginPage.checkLoginPageElements();
    }

    @Step
    public void checkConsoleError(String error) {
        loginPage.checkConsoleError(error);
    }

    @Step
    public void checkThatEarlyAccessErrorMessageIsShownForField(String error, String field) {
        loginPage.checkThatEarlyAccessErrorMessageIsShownForField(error, field);
    }

    @Step
    public void checkNewPasswordPageOpened() {
        loginPage.checkNewPasswordPageOpened();
    }

    @Step
    public void checkThatPopupIsShown(String popUp) {
        loginPage.checkThatPopupIsShown(popUp);
    }

    @Step
    public void waitForSignInPageAntiFlickerScript() {
        loginPage.waitForSignInPageAntiFlickerScript();
    }

    @Step
    public void clickSignUpWhatWeOfferButton() {
        loginPage.clickSignUpWhatWeOfferButton();
    }

    @Step
    public void clickSignUpAboutUsButton() {
        loginPage.clickSignUpAboutUsButton();
    }

    @Step
    public void checkHeaderUsername(String name) {
        loginPage.checkHeaderUsername(name);
    }

    @Step
    public void checkThatVerificationEmailPageOpened() {
        loginPage.checkThatVerificationEmailPageOpened();
    }
}
