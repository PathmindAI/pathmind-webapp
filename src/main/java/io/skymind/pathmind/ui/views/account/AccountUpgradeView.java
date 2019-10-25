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
import io.skymind.pathmind.services.UserService;
import io.skymind.pathmind.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.ui.layouts.MainLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import reactor.core.publisher.UnicastProcessor;

@Tag("account-upgrade-view")
@JsModule("./src/account/account-upgrade-view.js")
@Route(value="account/upgrade", layout = MainLayout.class)
public class AccountUpgradeView extends PolymerTemplate<AccountUpgradeView.Model>
{
	@Id("header")
	private Div header;

	private PathmindUser user;

	@Autowired
	private UserService userService;

	private final UnicastProcessor<PathmindBusEvent> publisher;

	@Autowired
	public AccountUpgradeView(CurrentUser currentUser, UnicastProcessor<PathmindBusEvent> publisher,
                              @Value("${pathmind.contact-support.address}") String contactLink)
	{
		getModel().setContactLink(contactLink);
		header.add(new ScreenTitlePanel("UPGRADE", "Subscription Plan"));
		user = currentUser.getUser();
		this.publisher = publisher;
//			publisher.onNext(new UserUpdateBusEvent(user));
	}



	public interface Model extends TemplateModel {
		void setContactLink(String contactLink);
	}
}
