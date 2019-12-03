package io.skymind.pathmind.ui.views.login;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.InitialPageSettings;
import com.vaadin.flow.server.PageConfigurator;
import com.vaadin.flow.templatemodel.TemplateModel;
import io.skymind.pathmind.data.PathmindUser;
import io.skymind.pathmind.security.Routes;
import io.skymind.pathmind.services.UserService;
import io.skymind.pathmind.services.notificationservice.EmailNotificationService;
import io.skymind.pathmind.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.ui.utils.VaadinUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Tag("sign-up-view")
@CssImport(value = "./styles/views/sign-up-view.css", id = "sign-up-view-styles")
@JsModule("./src/account/sign-up-view.js")
@Route(value = Routes.SIGN_UP_URL)
public class SignUpView extends PolymerTemplate<SignUpView.Model> implements PageConfigurator
{
	private static final String EMAIL_IS_USED = "This email is already used.";

	@Id("lastName")
	private TextField lastName;

	@Id("firstName")
	private TextField firstName;

	@Id("email")
	private TextField email;

	@Id("cancelSignUpBtn")
	private Button cancelSignUpBtn;

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

	@Autowired
	private UserService userService;

	@Autowired
	private EmailNotificationService emailNotificationService;
	
	@Autowired
	private SegmentIntegrator segmentIntegrator;

	private PathmindUser user;
	private Binder<PathmindUser> binder;

	public SignUpView(@Value("${pathmind.contact-support.address}") String contactLink)
	{
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

		cancelSignUpBtn.addClickListener(e -> UI.getCurrent().navigate(LoginView.class));
		cancelSignInBtn.addClickListener(e -> showPassword(false));

		forgotPasswordBtn.addClickListener(e ->UI.getCurrent().navigate(ResetPasswordView.class));

		signUp.addClickListener(e -> {
			if (binder.validate().isOk()) {
				if (userService.findByEmailIgnoreCase(email.getValue()) != null) {
					getModel().setMessage(EMAIL_IS_USED);
					email.setInvalid(true);
				} else {
					showPassword(true);
				}
			}
		});

		signIn.addClickListener(e -> {
			List<String> validationResults = userService.validatePassword(newPassword.getValue(), confirmNewPassword.getValue());

			if (validationResults.isEmpty()) {
				user.setPassword(newPassword.getValue());
				user = userService.signup(user);
                emailNotificationService.sendVerificationEmail(user);
                segmentIntegrator.userRegistered();
				Notification.show("You successfully signed up.", 3000, Notification.Position.TOP_END);
				UI.getCurrent().navigate(LoginView.class);
			} else {
				newPassword.setInvalid(true);
				passwordValidationNotes.removeAll();
				validationResults.forEach(message -> passwordValidationNotes.add(new Span(message)));
			}
		});
	}

	private void showPassword(boolean showPasswordPart) {
		getModel().setTitle(showPasswordPart ? "Create a new password" : "Get Started!");
		emailPart.setVisible(!showPasswordPart);
		passwordPart.setVisible(showPasswordPart);
	}

	private void initBinder() {
		binder = new Binder<>(PathmindUser.class);

		binder.forField(email).asRequired("Email is required").withValidator(new EmailValidator(
				"This doesn't look like a valid email address"))
				.bind(PathmindUser::getEmail, PathmindUser::setEmail);
		binder.forField(firstName).bind(PathmindUser::getFirstname, PathmindUser::setFirstname);
		binder.forField(lastName).bind(PathmindUser::getLastname, PathmindUser::setLastname);
		binder.setBean(user);
	}
	
	@Override
	public void configurePage(InitialPageSettings settings) {
		VaadinUtils.setupFavIcon(settings);
	}

	public interface Model extends TemplateModel {
		void setTitle(String title);
		void setMessage(String message);
		void setContactLink(String contactLink);
	}
}
