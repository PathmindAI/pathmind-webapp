package io.skymind.pathmind.webapp.ui.views.login;

import com.vaadin.flow.data.validator.StringLengthValidator;
import io.skymind.pathmind.webapp.ui.binders.PathmindUserBinders;
import io.skymind.pathmind.webapp.ui.converter.TrimmedStringConverter;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.TemplateModel;

import io.skymind.pathmind.shared.data.PathmindUser;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.webapp.security.UserService;
import io.skymind.pathmind.services.notificationservice.EmailNotificationService;

@Tag("sign-up-view")
@CssImport(value = "./styles/views/sign-up-view.css", id = "sign-up-view-styles")
@JsModule("./src/account/sign-up-view.js")
@Route(value = Routes.SIGN_UP_URL)
public class SignUpView extends PolymerTemplate<SignUpView.Model> implements PublicView
{
	@Id("lastName")
	private TextField lastName;

	@Id("firstName")
	private TextField firstName;

	@Id("email")
	private TextField email;

	@Id("cancelSignInBtn")
	private Button cancelSignInBtn;

	@Id("forgotPasswordBtn")
	private Button forgotPasswordBtn;

	@Id("signUp")
	private Button signUp;

	@Id("signIn")
	private Button signIn;

	@Id("newPassword")
	private PasswordField newPassword;

	@Id("confirmNewPassword")
	private PasswordField confirmNewPassword;

	@Id("newPassNotes")
	private VerticalLayout passwordValidationNotes;

	@Id("emailPart")
	private VerticalLayout emailPart;

	@Id("passwordPart")
	private VerticalLayout passwordPart;

	@Id("policyText")
	private Div policyText;

	private final UserService userService;
	private final EmailNotificationService emailNotificationService;
	private final SegmentIntegrator segmentIntegrator;

	private PathmindUser user;
	private Binder<PathmindUser> binder;

	public SignUpView(UserService userService, EmailNotificationService emailNotificationService, SegmentIntegrator segmentIntegrator, 
	        @Value("${pathmind.contact-support.address}") String contactLink) {
	    this.userService = userService;
	    this.emailNotificationService = emailNotificationService;
	    this.segmentIntegrator = segmentIntegrator;
		getModel().setContactLink(contactLink);
		user = new PathmindUser();
		initView();
		initBinder();
	}
	
	@Override
	protected void onAttach(AttachEvent attachEvent) {
		getElement().appendChild(segmentIntegrator.getElement());
	}

	private void initView() {
		emailPart.setSpacing(false);
		emailPart.setPadding(false);
		passwordPart.setSpacing(false);
		passwordPart.setPadding(false);
		passwordValidationNotes.setSpacing(false);
		passwordValidationNotes.setPadding(false);

		showPassword(false);

		cancelSignInBtn.addClickListener(e -> showPassword(false));

		forgotPasswordBtn.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(ResetPasswordView.class)));

		signUp.addClickListener(e -> {
			if (binder.validate().isOk()) {
			    showPassword(true);
			} 
		});

		signIn.addClickListener(e -> {
			UserService.PasswordValidationResults validationResults = userService
					.validatePassword(newPassword.getValue(), confirmNewPassword.getValue());

			if (validationResults.isOk()) {
				user.setPassword(newPassword.getValue());
				user = userService.signup(user);
                emailNotificationService.sendVerificationEmail(user, user.getEmail(), true);
                segmentIntegrator.userRegistered();
                getUI().ifPresent(ui -> ui.navigate(VerificationEmailSentView.class));
			} else {
				newPassword.setInvalid(true);
				passwordValidationNotes.removeAll();
				validationResults.getPasswordValidationErrors().forEach(message -> passwordValidationNotes.add(new Span(message)));
				confirmNewPassword.setInvalid(!validationResults.getConfirmPasswordValidationError().isEmpty());
				confirmNewPassword.setErrorMessage(validationResults.getConfirmPasswordValidationError());
			}
		});
	}

	private void showPassword(boolean showPasswordPart) {
		getModel().setTitle(showPasswordPart ? "Create Password" : "Sign up for a 30-day Free Trial!");
		emailPart.setVisible(!showPasswordPart);
		passwordPart.setVisible(showPasswordPart);
		policyText.setVisible(showPasswordPart);
	}

	private void initBinder() {
		binder = new Binder<>(PathmindUser.class);
		binder.addStatusChangeListener(evt -> processValidationStatusChange(evt.hasValidationErrors()));
		PathmindUserBinders.bindEmail(userService, binder, email);
		PathmindUserBinders.bindFirstName(binder, firstName);
		PathmindUserBinders.bindLastName(binder, lastName);

		binder.setBean(user);
	}

	private void processValidationStatusChange(boolean hasValidationErrors) {
	    getModel().setIsEmailUsed(hasValidationErrors && userService.findByEmailIgnoreCase(email.getValue()) != null); 
    }

    public interface Model extends TemplateModel {
		void setTitle(String title);
		void setIsEmailUsed(Boolean isEmailUsed);
		void setContactLink(String contactLink);
	}
}
