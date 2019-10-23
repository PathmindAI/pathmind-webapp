package io.skymind.pathmind.ui.views.account;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
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
import com.vaadin.flow.templatemodel.TemplateModel;
import io.skymind.pathmind.data.PathmindUser;
import io.skymind.pathmind.services.UserService;
import io.skymind.pathmind.ui.views.LoginView;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Tag("sign-up-view")
@JsModule("./src/account/sign-up-view.js")
@Route(value="sign-up")
public class SignUpView extends PolymerTemplate<SignUpView.Model>
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

	private PathmindUser user;
	private Binder<PathmindUser> binder;

	@Autowired
	private UserService userService;

	@Autowired
	public SignUpView()
	{
		user = new PathmindUser();
		initView();
		initBinder();
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
				userService.signup(user);
                Notification.show("You successfully signed up", 3000, Notification.Position.TOP_END);
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

		binder.forField(email).asRequired().withValidator(new EmailValidator(
				"This doesn't look like a valid email address"))
				.bind(PathmindUser::getEmail, PathmindUser::setEmail);
		binder.forField(firstName).bind(PathmindUser::getFirstname, PathmindUser::setFirstname);
		binder.forField(lastName).bind(PathmindUser::getLastname, PathmindUser::setLastname);
		binder.setBean(user);
	}

	public interface Model extends TemplateModel {
		void setTitle(String title);
		void setMessage(String message);
	}
}
