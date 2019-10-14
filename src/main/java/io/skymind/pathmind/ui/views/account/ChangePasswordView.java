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
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.TemplateModel;
import io.skymind.pathmind.data.PathmindUser;
import io.skymind.pathmind.security.CurrentUser;
import io.skymind.pathmind.services.UserService;
import io.skymind.pathmind.ui.layouts.MainLayout;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Tag("change-password-view")
@JsModule("./src/account/change-password-view.js")
@Route(value="account/change-password", layout = MainLayout.class)
public class ChangePasswordView extends PolymerTemplate<ChangePasswordView.Model>
{
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

	private Binder<PathmindUser> binder;

	@Autowired
	private UserService userService;

	@Autowired
	public ChangePasswordView(CurrentUser currentUser)
	{
		user = currentUser.getUser();

		passwordValidationNotes.setPadding(false);
		passwordValidationNotes.setSpacing(false);


		cancelBtn.addClickListener(e -> UI.getCurrent().navigate(AccountView.class));
		updateBtn.addClickListener(e -> {
			if (validate())  {
				if (userService.changePassword(user, newPassword.getValue())) {
					UI.getCurrent().navigate(AccountView.class);
//					TODO change to notification center
				} else {
//					TODO change to notification center and make error
					Notification.show("There was an error during changing password, please try again");
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
	}
}
