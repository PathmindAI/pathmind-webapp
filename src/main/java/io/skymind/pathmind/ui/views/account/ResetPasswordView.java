package io.skymind.pathmind.ui.views.account;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.*;
import com.vaadin.flow.templatemodel.TemplateModel;
import io.skymind.pathmind.data.PathmindUser;
import io.skymind.pathmind.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.UUID;

@Tag("reset-password-view")
@JsModule("./src/account/reset-password-view.js")
@Route(value="reset-password")
public class ResetPasswordView extends PolymerTemplate<ResetPasswordView.Model>
	implements HasUrlParameter<String>, AfterNavigationObserver
{
	private static final String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
	private static final String CONFIRMATION_MESSAGE = "Reset password email was send.";

	@Id("email")
	private EmailField emailField;

	@Id("sendBtn")
	private Button sendBtn;

	@Id("cancelBtn")
	private Button cancelBtn;

	@Autowired
	private UserService userService;

	private String token = null;

	public ResetPasswordView() {
	}

	@Override
	public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
		emailField.setInvalid(false);
		emailField.setValueChangeMode(ValueChangeMode.ON_CHANGE);
		emailField.addValueChangeListener(e -> {
			boolean valid = isValid(emailField.getValue());
			emailField.setInvalid(!valid);
			sendBtn.setEnabled(valid);
		});

		sendBtn.setEnabled(emailField.isInvalid());
		sendBtn.addClickListener(e -> {
			if (!emailField.isInvalid()) {
				startResetProcess(emailField.getValue());
			}
		});
	}


	private void startResetProcess(String value) {
		PathmindUser user = userService.findByEmailIgnoreCase(value);
		getModel().setMessage(CONFIRMATION_MESSAGE);

		if (user == null) {
			return;
		}

		if (user.getEmailVerifiedAt() != null || user.getEmailVerificationToken() == null ) {
			user.setEmailVerificationToken(UUID.randomUUID());
		}

//		TODO: uncomment after jooq generete new version
//		user.setPasswordResetSendAt(LocalDateTime.now());
		userService.update(user);
		String link = new RouterLink(user.getName(), ResetPasswordView.class).getHref();
		link += "/" + user.getEmailVerificationToken();

//		TODO send email
		System.out.println("send email to " + emailField.getValue() + " link: " + link);
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
	}
}
