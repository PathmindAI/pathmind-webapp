package io.skymind.pathmind.ui.views.account;

import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Subscription;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.polymertemplate.EventHandler;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.TemplateModel;
import elemental.json.JsonObject;
import io.skymind.pathmind.data.PathmindUser;
import io.skymind.pathmind.security.CurrentUser;
import io.skymind.pathmind.services.UserService;
import io.skymind.pathmind.services.billing.StripeService;
import io.skymind.pathmind.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.utils.NotificationUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Objects;

import static io.skymind.pathmind.security.Routes.PAYMENT_URL;
import static io.skymind.pathmind.security.Routes.UPGRADE_DONE;

@Tag("upgrade-done-view")
@JsModule("./src/account/upgrade-done-view.js")
@Route(value = UPGRADE_DONE, layout = MainLayout.class)
public class UpgradeDoneView extends PolymerTemplate<UpgradeDoneView.Model>
{

	private static Logger log = LogManager.getLogger(UpgradeDoneView.class);
	@Id("header")
	private Div header;

	@Id("done")
	private Button done;

	private PathmindUser user;

	@Autowired
	private UserService userService;


	@Autowired
	public UpgradeDoneView(CurrentUser currentUser,
					   @Value("${pathmind.contact-support.address}") String contactLink)
	{
		user = currentUser.getUser();
		header.add(new ScreenTitlePanel("UPGRADE", "Subscription Plans"));

		getModel().setContactLink(contactLink);
		getModel().setPlan("Professional");

		done.addClickListener(e -> UI.getCurrent().navigate(AccountView.class));
	}

	public interface Model extends TemplateModel {
		void setContactLink(String contactLink);
		void setPlan(String plan);
	}

}
