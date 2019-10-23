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
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.*;
import com.vaadin.flow.templatemodel.TemplateModel;
import io.skymind.pathmind.data.PathmindUser;
import io.skymind.pathmind.services.UserService;
import io.skymind.pathmind.services.notificationservice.NotificationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import io.skymind.pathmind.ui.views.LoginView;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Tag("reset-password-view")
@JsModule("./src/account/reset-password-view.js")
@Route(value="reset-password")
public class ResetPasswordView extends PolymerTemplate<ResetPasswordView.Model>
	implements HasUrlParameter<String>, AfterNavigationObserver
{
	private static Logger log = LogManager.getLogger(ResetPasswordView.class);

	private static final String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
	private static final String SEND_CONFIRMATION = "Reset password email was send.";
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
	private NotificationService notificationService;

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

		cancelBtn.addClickListener(e -> UI.getCurrent().navigate(LoginView.class));
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
				List<String> validationResults = userService.validatePassword(newPassword.getValue(), confirmNewPassword.getValue());

				if (validationResults.isEmpty()) {
					userService.changePassword(user, newPassword.getValue());
					user.setPasswordResetSendAt(null);
					userService.update(user);
					Notification.show(CHANGED_CONFIRMATION, 3000, Notification.Position.TOP_END);
					UI.getCurrent().navigate(LoginView.class);
				} else {
					newPassword.setInvalid(true);
					passwordValidationNotes.removeAll();
					validationResults.forEach(message -> passwordValidationNotes.add(new Span(message)));
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
		Notification.show(SEND_CONFIRMATION, 3000, Notification.Position.TOP_END);
		getModel().setMessage("");

		if (user == null) {
			log.info("Attempt to restore password of not existing user, with email: " + email);
			return;
		}

		if (user.getEmailVerifiedAt() != null || user.getEmailVerificationToken() == null ) {
			user.setEmailVerificationToken(UUID.randomUUID());
		}

		user.setPasswordResetSendAt(LocalDateTime.now());
		userService.update(user);
		String link = new RouterLink(user.getName(), ResetPasswordView.class).getHref();
		link += "/" + user.getEmailVerificationToken();

		notificationService.sendResetPasswordEmail(user);
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
