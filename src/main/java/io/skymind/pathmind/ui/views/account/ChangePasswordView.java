package io.skymind.pathmind.ui.views.account;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Span;
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
import io.skymind.pathmind.security.CurrentUser;
import io.skymind.pathmind.services.UserService;
import io.skymind.pathmind.ui.layouts.MainLayout;
import org.springframework.beans.factory.annotation.Autowired;

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
	private VerticalLayout newPassNotes;

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

		newPassNotes.setPadding(false);
		newPassNotes.setSpacing(false);

		cancelBtn.addClickListener(e -> UI.getCurrent().navigate(AccountView.class));
		updateBtn.addClickListener(e -> {
			if (validate())  {
//				Update password
				UI.getCurrent().navigate(AccountView.class);
			}
		});
	}

	private boolean validate() {
//		Validate current password
//		Validate new password
//		currentPassword.setErrorMessage("* 1 lowercase charter\n * 1 uppercase charter");
//		newPassword.setErrorMessage("<span>* 1 lowercase charter</span>" +
//									"<span>* 1 uppercase charter<span>");

		newPassNotes.removeAll();
		newPassNotes.add(new Span("* 1 lowercase charter"));
		newPassNotes.add(new Span("* 1 uppercase charter"));
		Span warning = new Span("* 6 minimum charter");
		warning.addClassName("secondary");
		newPassNotes.add(warning);

		return false;
	}

	public interface Model extends TemplateModel {
	}
}
