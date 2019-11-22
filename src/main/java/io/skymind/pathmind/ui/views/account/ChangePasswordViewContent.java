package io.skymind.pathmind.ui.views.account;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.templatemodel.TemplateModel;
import io.skymind.pathmind.data.PathmindUser;
import io.skymind.pathmind.security.CurrentUser;
import io.skymind.pathmind.services.UserService;
import io.skymind.pathmind.ui.utils.NotificationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import java.util.List;

@Tag("change-password-view-content")
@JsModule("./src/account/change-password-view-content.js")
@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ChangePasswordViewContent extends PolymerTemplate<ChangePasswordViewContent.Model> {
	@Id("currentPassword")
	private PasswordField currentPassword;

	@Id("newPassword")
	private PasswordField newPassword;

	@Id("confirmNewPassword")
	private PasswordField confirmNewPassword;

	@Id("newPassNotes")
	private VerticalLayout passwordValidationNotes;

	@Id("currentPassNotes")
	private VerticalLayout currentPasswordValidationNotes;

	@Id("cancelBtn")
	private Button cancelBtn;

	@Id("updateBtn")
	private Button updateBtn;

	private PathmindUser user;

	@Autowired
	private UserService userService;

	@Autowired
	public ChangePasswordViewContent(CurrentUser currentUser, @Value("${pathmind.contact-support.address}") String contactLink) {
		getModel().setContactLink(contactLink);
		user = currentUser.getUser();

		passwordValidationNotes.setPadding(false);
		passwordValidationNotes.setSpacing(false);

		cancelBtn.addClickShortcut(Key.ESCAPE);


		cancelBtn.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(AccountView.class)));
		updateBtn.addClickListener(e -> {
			if (validate())  {
				if (userService.changePassword(user, newPassword.getValue())) {
					NotificationUtils.showNotification("Password was successfully changed.", NotificationVariant.LUMO_SUCCESS);
					getUI().ifPresent(ui -> ui.navigate(AccountView.class));
				} else {
					NotificationUtils.showNotification("There was an error during changing password, please try again",
							NotificationVariant.LUMO_ERROR);
				}
			}
		});
	}

	private void validateCurrentPassword() {
		if (!userService.isCurrentPassword(user, currentPassword.getValue())) {
			currentPasswordValidationNotes.removeAll();
			currentPasswordValidationNotes.add(new Span("Password is incorrect"));
			currentPassword.setInvalid(true);
		}
	}

	private void validateNewPassword() {
		List<String> validationResults = userService.validatePassword(newPassword.getValue(), confirmNewPassword.getValue());
		if (!validationResults.isEmpty()) {
			newPassword.setInvalid(true);
			passwordValidationNotes.removeAll();
			validationResults.forEach(message -> passwordValidationNotes.add(new Span(message)));
		}
	}

	private boolean validate() {
		validateCurrentPassword();
		validateNewPassword();
		return !currentPassword.isInvalid() && !newPassword.isInvalid();
	}

	public interface Model extends TemplateModel {
		void setContactLink(String contactLink);
	}
}
