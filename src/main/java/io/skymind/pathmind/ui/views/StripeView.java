package io.skymind.pathmind.ui.views;

import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Subscription;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.TemplateModel;
import elemental.json.JsonObject;
import io.skymind.pathmind.security.CurrentUser;
import io.skymind.pathmind.services.billing.StripeService;
import io.skymind.pathmind.ui.layouts.MainLayout;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.util.Objects;

@Tag("stripe-view")
@JsModule("./src/views/stripe-view.js")
@Route(value = "stripe-view", layout = MainLayout.class)
@CssImport(value = "./styles/views/stripe-view.css")
public class StripeView extends PolymerTemplate<StripeView.Model>
{

	private static Logger log = LogManager.getLogger(StripeView.class);
	private StripeService stripeService;
	private final String publicKey;
	private final CurrentUser currentUser;

	public StripeView(@Value("${pathmind.stripe.public.key}") String publicKey, @Autowired StripeService stripeService, @Autowired CurrentUser currentUser)
	{
		this.publicKey = publicKey;
		this.stripeService = stripeService;
		this.currentUser = currentUser;
	}

	@PostConstruct
	private void init()
	{
		getModel().setKey(publicKey);
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
			Customer customer = stripeService.createCustomer(currentUser.getUser().getEmail(), paymentMethodId);
			final Subscription subscription = stripeService.createSubscription(customer);
			Notification.show("A new customer has been created on Stripe. id: " + customer.getId() + ", email: " + customer.getEmail() + ", with subscription id: " + subscription.getId());
		} catch (StripeException e) {
			throw new RuntimeException(e);
		}
	}

	public interface Model extends TemplateModel
	{
		void setKey(String key);
	}


}
