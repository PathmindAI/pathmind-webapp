package io.skymind.pathmind.ui.views.account;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.TemplateModel;
import io.skymind.pathmind.bus.PathmindBusEvent;
import io.skymind.pathmind.bus.data.UserUpdateBusEvent;
import io.skymind.pathmind.data.PathmindUser;
import io.skymind.pathmind.security.CurrentUser;
import io.skymind.pathmind.security.Routes;
import io.skymind.pathmind.services.UserService;
import io.skymind.pathmind.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.ui.layouts.MainLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import reactor.core.publisher.UnicastProcessor;

@Tag("account-edit-view")
@JsModule("./src/account/account-edit-view.js")
@Route(value = Routes.ACCOUNT_EDIT_URL, layout = MainLayout.class)
public class AccountEditView extends PolymerTemplate<AccountEditView.Model>
{
	@Id("header")
	private Div header;

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

	private final UnicastProcessor<PathmindBusEvent> publisher;

	@Autowired
	public AccountEditView(CurrentUser currentUser, UnicastProcessor<PathmindBusEvent> publisher,
						   @Value("${pathmind.contact-support.address}") String contactLink)
	{
		getModel().setContactLink(contactLink);
		header.add(new ScreenTitlePanel("ACCOUNT", "Edit"));
//		header.add(new ScreenTitlePanel("ACCOUNT Edit"));
		user = currentUser.getUser();
		this.publisher = publisher;
		initBinder();

		email.setEnabled(false);
		cancelBtn.addClickListener(e -> UI.getCurrent().navigate(AccountView.class));
		updateBtn.addClickListener(e -> {
			userService.update(user);
			publisher.onNext(new UserUpdateBusEvent(user));
			UI.getCurrent().navigate(AccountView.class);
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
