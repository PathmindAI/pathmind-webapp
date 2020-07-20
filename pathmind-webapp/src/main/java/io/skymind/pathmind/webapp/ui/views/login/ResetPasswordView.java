package io.skymind.pathmind.webapp.ui.views.login;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.*;
import com.vaadin.flow.templatemodel.TemplateModel;
import io.skymind.pathmind.shared.data.PathmindUser;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.webapp.security.UserService;
import io.skymind.pathmind.services.notificationservice.EmailNotificationService;
import io.skymind.pathmind.webapp.ui.utils.NotificationUtils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Tag("reset-password-view")
@JsModule("./src/account/reset-password-view.js")
@Route(value = Routes.RESET_PASSWORD_URL)
@Slf4j
public class ResetPasswordView extends PolymerTemplate<ResetPasswordView.Model>
	implements PublicView, HasUrlParameter<String>, AfterNavigationObserver
{

	private static final String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
	private static final String SEND_CONFIRMATION = "Reset password email was sent.";
	private static final String LINK_IS_NOT_VALID = "Link is no longer valid. Please try to recover password again.";
	private static final String CHANGED_CONFIRMATION = "Password was successfully changed";

	@Id("email")
	private EmailField emailField;

	@Id("sendBtn")
	private Button sendBtn;

	@Id("changePassword")
	private Button changePassword;

	@Id("cancelBtn")
	private Button cancelBtn;

	@Id("newPassword")
	private PasswordField newPassword;

	@Id("confirmNewPassword")
	private PasswordField confirmNewPassword;

	@Id("newPassNotes")
	private VerticalLayout passwordValidationNotes;

	@Id("prePart")
	private VerticalLayout prePart;

	@Id("postPart")
	private VerticalLayout postPart;

	@Autowired
	private UserService userService;

	@Autowired
	private EmailNotificationService emailNotificationService;

	@Value("${pathmind.reset.password.link.valid}")
	private int resetTokenValidHours;

	private String token = null;


	public ResetPasswordView(@Value("${pathmind.contact-support.address}") String contactLink)
	{
		getModel().setContactLink(contactLink);
	}

	@Override
	public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
		resetLayouts();

		if (token == null) {
			initPreStep();
		} else {
			initPostStep();
		}
	}

	private void resetLayouts() {
		prePart.setSpacing(false);
		prePart.setPadding(false);

		postPart.setSpacing(false);
		postPart.setPadding(false);

		passwordValidationNotes.setSpacing(false);
		passwordValidationNotes.setPadding(false);
	}

	private void initPreStep() {
		prePart.setVisible(true);
		postPart.setVisible(false);

		emailField.setInvalid(false);
		emailField.setValueChangeMode(ValueChangeMode.ON_CHANGE);

		cancelBtn.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(LoginView.class)));
		sendBtn.addClickListener(e -> {
			if (isValid(emailField.getValue())) {
				startResetProcess(emailField.getValue());
			} else {
				emailField.setInvalid(true);
			}
		});
	}

	private void initPostStep() {
		prePart.setVisible(false);
		postPart.setVisible(true);

		try {
			PathmindUser user = userService.findByToken(token);
			if (user == null || user.getPasswordResetSendAt() == null ||
					LocalDateTime.now().minus(resetTokenValidHours, ChronoUnit.HOURS).isAfter(user.getPasswordResetSendAt())) {
				linkIsNotValid();
				return;
			}

			changePassword.addClickListener(e -> {
				UserService.PasswordValidationResults validationResults = userService
						.validatePassword(newPassword.getValue(), confirmNewPassword.getValue());

				if (validationResults.isOk()) {
					userService.changePassword(user, newPassword.getValue());
					user.setPasswordResetSendAt(null);
					userService.update(user);
					NotificationUtils.showSuccess(CHANGED_CONFIRMATION);
					getUI().ifPresent(ui -> ui.navigate(LoginView.class));
				} else {
					newPassword.setInvalid(true);
					passwordValidationNotes.removeAll();
					validationResults.getPasswordValidationErrors().forEach(message -> passwordValidationNotes.add(new Span(message)));
					confirmNewPassword.setInvalid(!validationResults.getConfirmPasswordValidationError().isEmpty());
					confirmNewPassword.setErrorMessage(validationResults.getConfirmPasswordValidationError());
				}
			});
		} catch(IllegalArgumentException e) {
			linkIsNotValid();
		}
	}

	private void linkIsNotValid() {
		getModel().setMessage(LINK_IS_NOT_VALID);
		token = null;
		initPreStep();
	}

	private void startResetProcess(String email) {
		PathmindUser user = userService.findByEmailIgnoreCase(email);
		NotificationUtils.showSuccess(SEND_CONFIRMATION);
		getModel().setMessage("");

		if (user == null) {
			log.info("Attempt to restore password of not existing user, with email: " + email);
			return;
		}

		emailNotificationService.sendResetPasswordEmail(user);
	}

	static boolean isValid(String email) {
		return email.matches(EMAIL_REGEX);
	}

	@Override
	public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String param) {
		token = param;
	}

	public interface Model extends TemplateModel {
		void setMessage(String message);
		void setContactLink(String contactLink);
	}
}
