package io.skymind.pathmind.ui.views.account;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
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

@Tag("account-edit-view")
@JsModule("src/account/account-edit-view.js")
@Route(value="account/edit", layout = MainLayout.class)
public class AccountEditView extends PolymerTemplate<AccountEditView.Model>
{
	@Id("lastName")
	private TextField lastName;
	@Id("firstName")
	private TextField firstName;
	@Id("email")
	private TextField email;

	@Id("cancelBtn")
	private Button cancelBtn;
	@Id("updateBtn")
	private Button updateBtn;

	private PathmindUser user;

	@Autowired
	private UserService userService;

	@Autowired
	public AccountEditView(CurrentUser currentUser)
	{
		user = currentUser.getUser();

		initBinder();

		cancelBtn.addClickListener(e -> UI.getCurrent().navigate(AccountView.class));
		updateBtn.addClickListener(e -> {

		});
	}

	private void initBinder() {
		Binder<PathmindUser> binder = new Binder<>(PathmindUser.class);

		binder.forField(email).asRequired().withValidator(new EmailValidator(
				"This doesn't look like a valid email address"))
				.bind(PathmindUser::getEmail, PathmindUser::setEmail);

		binder.forField(firstName).bind(PathmindUser::getFirstname, PathmindUser::setFirstname);
		binder.forField(lastName).bind(PathmindUser::getLastname, PathmindUser::setLastname);
	}

	public interface Model extends TemplateModel {
	}
}
