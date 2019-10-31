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
import io.skymind.pathmind.ui.views.StripeView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Objects;

import static io.skymind.pathmind.security.Routes.PAYMENT_URL;

@Tag("payment-view")
@JsModule("./src/account/payment-view.js")
@Route(value=PAYMENT_URL, layout = MainLayout.class)
public class PaymentView extends PolymerTemplate<PaymentView.Model>
{

	private static Logger log = LogManager.getLogger(PaymentView.class);
	private StripeService stripeService;
	private final String publicKey;

	//@Id("header")
	private Div header;

	//@Id("cancelSignUpBtn")
	private Button cancelSignUpBtn;

	//@Id("signUp")
	private Button signUp;

	private PathmindUser user;

	@Autowired
	private UserService userService;


	@Autowired
	public PaymentView(@Value("${pathmind.stripe.public.key}") String publicKey,
					   StripeService stripeService,
					   CurrentUser currentUser,
					   @Value("${pathmind.contact-support.address}") String contactLink)
	{
		this.publicKey = publicKey;
		this.stripeService = stripeService;
		user = currentUser.getUser();
		//header.add(new ScreenTitlePanel("UPGRADE", "Subscription Plans"));

		getModel().setContactLink(contactLink);
		getModel().setPlan("Professional");
		getModel().setKey(publicKey);

		//cancelSignUpBtn.addClickListener(e -> UI.getCurrent().navigate(AccountUpgradeView.class));
	}

	/**
	 * This method is called from the client side
	 *
	 * @param paymentMethod
	 */
	@ClientCallable
	private void paymentMethodCallback(JsonObject paymentMethod)
	{
		Objects.requireNonNull(paymentMethod);
		final String paymentMethodId = paymentMethod.getString("id");
		if (paymentMethodId == null) {
			final String noPaymentId = "Received a payment method call without a payment id";
			log.info(noPaymentId);
			throw new RuntimeException(noPaymentId);
		}
		// TODO: use the following method to get and save all the other interesting fields for the customer in Stripe
		//final String city = paymentMethod.getObject("billing_details").getObject("address").getString("city");
		try {
			Customer customer = stripeService.createCustomer(user.getEmail(), paymentMethodId);
			final Subscription subscription = stripeService.createSubscription(customer);
			Notification.show("A new customer has been created on Stripe. id: " + customer.getId() + ", email: " + customer.getEmail() + ", with subscription id: " + subscription.getId());
		} catch (StripeException e) {
			throw new RuntimeException(e);
		}
	}

	public interface Model extends TemplateModel {
		void setContactLink(String contactLink);
		void setPlan(String plan);
		void setKey(String key);
	}

}
