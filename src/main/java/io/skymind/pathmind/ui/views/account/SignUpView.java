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
import io.skymind.pathmind.ui.views.LoginView;
import org.springframework.beans.factory.annotation.Autowired;

@Tag("sign-up-view")
@JsModule("./src/account/sign-up-view.js")
@Route(value="account/sign-up")
public class SignUpView extends PolymerTemplate<SignUpView.Model>
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

	private Binder<PathmindUser> binder;

	@Autowired
	private UserService userService;


	@Autowired
	public SignUpView(CurrentUser currentUser)
	{
		user = new PathmindUser();
		initBinder();

		cancelBtn.addClickListener(e -> UI.getCurrent().navigate(LoginView.class));
		updateBtn.addClickListener(e -> {
//			userService.update(user);
			System.out.println("TODO: Create user: "
					+ user.getFirstname() + " "
					+ user.getLastname()
					+ " " + user.getEmail());

			if (binder.validate().isOk()) {
				UI.getCurrent().navigate(LoginView.class);
			}
		});
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
	}
}
