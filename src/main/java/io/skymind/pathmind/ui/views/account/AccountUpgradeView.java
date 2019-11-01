package io.skymind.pathmind.ui.views.account;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.TemplateModel;
import io.skymind.pathmind.data.PathmindUser;
import io.skymind.pathmind.security.CurrentUser;
import io.skymind.pathmind.services.UserService;
import io.skymind.pathmind.services.billing.StripeService;
import io.skymind.pathmind.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.ui.layouts.MainLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import static io.skymind.pathmind.security.Routes.ACCOUNT_UPGRADE_URL;

@Tag("account-upgrade-view")
@JsModule("./src/account/account-upgrade-view.js")
@Route(value=ACCOUNT_UPGRADE_URL, layout = MainLayout.class)
public class AccountUpgradeView extends PolymerTemplate<AccountUpgradeView.Model> implements BeforeEnterObserver
{
	@Id("header")
	private Div header;

	@Id("proBtn")
	private Button proBtn;

	private PathmindUser user;

	@Autowired
	private UserService userService;

	@Autowired
	private StripeService stripeService;

	@Autowired
	public AccountUpgradeView(CurrentUser currentUser,
                              @Value("${pathmind.contact-support.address}") String contactLink)
	{
		getModel().setContactLink(contactLink);
		header.add(new ScreenTitlePanel("UPGRADE", "Subscription Plans"));
		user = currentUser.getUser();

		proBtn.addClickListener(e -> UI.getCurrent().navigate(PaymentView.class));
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event)
	{
		// if user has an ongoing subscription this view shouldn't be shown
		if (stripeService.userHasActiveProfessionalSubscription(user.getEmail())) {
			event.rerouteTo(AccountView.class);
		}
	}

	public interface Model extends TemplateModel {
		void setContactLink(String contactLink);
	}
}
