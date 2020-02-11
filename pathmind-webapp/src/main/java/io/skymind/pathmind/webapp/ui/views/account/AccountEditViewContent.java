package io.skymind.pathmind.webapp.ui.views.account;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.templatemodel.TemplateModel;
import io.skymind.pathmind.db.data.PathmindUser;
import io.skymind.pathmind.webapp.security.CurrentUser;
import io.skymind.pathmind.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Tag("account-edit-view-content")
@JsModule("./src/account/account-edit-view-content.js")
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AccountEditViewContent extends PolymerTemplate<AccountEditViewContent.Model> {

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
	public AccountEditViewContent(CurrentUser currentUser,
						   @Value("${pathmind.contact-support.address}") String contactLink) {
		getModel().setContactLink(contactLink);
		user = currentUser.getUser();
		initBinder();

		email.setEnabled(false);

		cancelBtn.addClickShortcut(Key.ESCAPE);

		cancelBtn.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(AccountView.class)));
		updateBtn.addClickListener(e -> {
			userService.update(user);
			getUI().ifPresent(ui -> ui.navigate(AccountView.class));
		});
	}

	private void initBinder() {
		Binder<PathmindUser> binder = new Binder<>(PathmindUser.class);

		binder.forField(email).asRequired().withValidator(new EmailValidator(
				"This doesn't look like a valid email address"))
				.bind(PathmindUser::getEmail, PathmindUser::setEmail);

		binder.forField(firstName).bind(PathmindUser::getFirstname, PathmindUser::setFirstname);
		binder.forField(lastName).bind(PathmindUser::getLastname, PathmindUser::setLastname);
		binder.setBean(user);
	}

	public interface Model extends TemplateModel {
		void setContactLink(String contactLink);
	}
}
