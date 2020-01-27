package pathmind.stepDefinitions;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.core.environment.EnvironmentSpecificConfiguration;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.util.EnvironmentVariables;
import net.thucydides.core.util.SystemEnvironmentVariables;
import pathmind.EmailApi;
import pathmind.steps.LoginPageSteps;
import java.util.List;

public class LoginPageStepDefinitions {

    private static EnvironmentVariables variables = SystemEnvironmentVariables.createEnvironmentVariables();
    private static String pathmindUsername = variables.getProperty("pathmind.username");
    private static String pathmindPassword = variables.getProperty("pathmind.password");
    private static String pathmindUrl =  EnvironmentSpecificConfiguration.from(variables).getProperty("base.url");

    @Steps
    private LoginPageSteps loginPageSteps;
    @Steps
    private EmailApi emailApi;

    @Given("^Login to the pathmind$")
    public void loginToThePathmind() {
        loginPageSteps.openPathmindUrl();
        loginPageSteps.loginWithCredential(pathmindUsername, pathmindPassword);
    }

    @Given("^Open pathmind page$")
    public void openHomePage(){
        loginPageSteps.openPathmindUrl();
    }

    @When("^Login with credentials (.*), (.*)$")
    public void loginWithCredentials(String email, String password) {
        loginPageSteps.loginWithCredential(email, password);
    }

    @Then("^Check that user (.*) successfully logged in$")
    public void checkThatUserSuccessfullyLoggedIn(String name) {
        loginPageSteps.checkThatUserSuccessfullyLoggedIn(name);
    }

    @Then("^Check that login form warning message is shown$")
    public void checkThatWarningMessageIsShown() {
        loginPageSteps.checkThatWarningMessageIsShown();
    }

    @Then("^Delete all cookies$")
    public void deleteAllCookies() {
        loginPageSteps.deleteAllCookies();
    }

    @When("^Open page (.*)$")
    public void openPage(String path) {
        loginPageSteps.openPage(pathmindUrl + path);
    }

    @When("^Fill new user form with (.*), (.*)$")
    public void fillFormWith(String firstName, String lastName) {
        loginPageSteps.newUserInputFirstName(firstName);
        loginPageSteps.newUserInputLastName(lastName);
        loginPageSteps.newUserInputEmail(emailApi.getEmail());
    }
    @When("^Fill new user form with first name (.*)$")
    public void fillFormWithFirstName(String firstName) {
        loginPageSteps.newUserInputFirstName(firstName);
    }
    @When("^Fill new user form with last name (.*)$")
    public void fillFormWithLastName(String lastName) {
        loginPageSteps.newUserInputLastName(lastName);
    }
    @When("^Fill new user form with email from API$")
    public void fillFormWithEmailFromApi() {
        loginPageSteps.newUserInputEmail(emailApi.getEmail());
    }
    @When("^Fill new user form with email (.*)$")
    public void fillFormWithEmail(String email) {
        loginPageSteps.newUserInputEmail(email);
    }

    @When("^Create new user click sign up button$")
    public void clickSignUpButton() {
        loginPageSteps.clickSignUpButton();
    }

    @When("^Fill new user password (.*)$")
    public void fillNewUserPassword(String password) {
        loginPageSteps.fillNewUserPassword(password);
    }
    @When("^Fill new user confirmation password (.*)$")
    public void fillNewUserConfirmationPassword(String password){
        loginPageSteps.fillNewUserConfirmationPassword(password);
    }

    @When("^Create new user click sign in button$")
    public void createNewUserClickSignInButton() {
        loginPageSteps.createNewUserClickSignInButton();
    }

    @When("^Get email and verify user email$")
    public void getEmailAndVerifyUserEmail() {
        loginPageSteps.openPage(pathmindUrl + "email-verification/" + emailApi.fetchEmail());
    }

    @Then("^Login with new user email and (.*)$")
    public void loginWithNewUserEmailAnd(String password) {
        loginPageSteps.loginWithCredential(Serenity.sessionVariableCalled("email"), password);
    }

    @Then("^Check that Create new user error (.*) shown$")
    public void checkThatError(String errorText) {
        loginPageSteps.checkThatError(errorText);
    }

    @Then("^Check that Create new user Resend btn is shown$")
    public void checkThatResendBtnIsShown() {
        loginPageSteps.checkThatResendBtnIsShown();
    }

    @When("^Check create new user page elements$")
    public void checkCreateNewUserPageElements() {
        loginPageSteps.checkCreateNewUserPageElements();
    }

    @When("^Click create new user cancel btn$")
    public void clickCreateNewUserCancelBtn() {
        loginPageSteps.clickCreateNewUserCancelBtn();
    }

    @Then("^Check that login page opened$")
    public void checkThatLoginPageOpened() {
        loginPageSteps.checkThatLoginPageOpened();
    }

    @Then("^Check new user page email alert message$")
    public void checkNewUserPageEmailAlertBtn() {
        loginPageSteps.checkNewUserPageEmailAlertBtn();
    }

    @Then("^Create new user check that error message shown (.*)$")
    public void createNewUserCheckThatErrorMessageShown(List<String> errorMsg) {
        loginPageSteps.createNewUserCheckThatErrorMessageShown(errorMsg);
    }

    @Then("^Create new user check that error message for email field shown (.*)$")
    public void createNewUserCheckThatErrorMessageForEmailFieldShown(String errorMsg) {
        loginPageSteps.createNewUserCheckThatErrorMessageForEmailFieldShown(errorMsg);

    }

    @Then("^Create new user check that forgot password btn exist$")
    public void createNewUserCheckThatForgotPasswordBtnExist() {
        loginPageSteps.createNewUserCheckThatForgotPasswordBtnExist();
    }

    @When("^Create new user click reset password btn$")
    public void createNewUserClickResetPasswordBtn() {
        loginPageSteps.createNewUserClickResetPasswordBtn();
    }

    @Then("^Check password recovery page elements$")
    public void checkPasswordRecoveryPageElements() {
        loginPageSteps.checkPasswordRecoveryPageElements();
    }

    @Then("^Check login page elements$")
    public void checkLoginPageElements() {
        loginPageSteps.checkLoginPageElements();
    }
}
